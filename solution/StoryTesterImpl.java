package solution;

import org.junit.ComparisonFailure;
import provided.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

public class StoryTesterImpl implements StoryTester {

    private Object objectBackup;

    String firstFailedSentence;
    String expected;
    String result;
    int numFails;

    /** If the testClass is a nested class, this function recursively creates it's enclosing classes.
        Returns the instance of the nested class. **/
    public static Object constructEnclosingClasses(Class<?> classObject) throws Exception {
        Constructor<?> classObjectCtor;
        // If the method has reached the top-level class, the method creates an instance of the class and returns it.
        if (classObject.getEnclosingClass() == null) {
            try {
                classObjectCtor = classObject.getDeclaredConstructor();
                classObjectCtor.setAccessible(true);
                return classObjectCtor.newInstance();
            } catch (Exception e) {
                return null;
            }
        }
        // If the method hasn't reached the top-level class, it keeps on looking for it recursively.
        Object enclosingInstance = constructEnclosingClasses(classObject.getEnclosingClass());
        if (enclosingInstance == null)
        {
            return null;
        }
        try {
            if (Modifier.isStatic(classObject.getModifiers())) {
                classObjectCtor = classObject.getDeclaredConstructor();
                classObjectCtor.setAccessible(true);
                return classObjectCtor.newInstance();
            }
            else {
                classObjectCtor = classObject.getDeclaredConstructor(enclosingInstance.getClass());
                classObjectCtor.setAccessible(true);
                return classObjectCtor.newInstance(enclosingInstance);
            }
        } catch (Exception e) {
            return null;
        }
    }

    /** Creates and returns a new instance of testClass **/
    private static Object createTestInstance(Class<?> testClass) throws Exception {
        try {
            // Try constructing a new instance using the default constructor of testClass
            Constructor<?> testClassCtor = testClass.getDeclaredConstructor();
            testClassCtor.setAccessible(true);
            return testClassCtor.newInstance();
        } catch (Exception e) {
            // If the method catches an exception, then testClass is probably a nested class.
            // If it isn't, an error occurred and the method returns null.
            if (e instanceof  NoSuchMethodException) {
                try {
                    testClass.getEnclosingClass();
                }
                catch (Exception exception) {
                    return null;
                }
            }
            // Inner classes case; Need to first create an instance of the enclosing class
            return constructEnclosingClasses(testClass);
        }
    }

    /** Returns true if c has a copy constructor, or false if it doesn't **/
    private boolean copyConstructorExists(Class<?> c){
        try {
            c.getDeclaredConstructor(c);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }


    /** Assigns into objectBackup a backup of obj. **/
    private void backUpInstance(Object obj) throws Exception {
        Object res = createTestInstance(obj.getClass());
        Field[] fieldsArr = obj.getClass().getDeclaredFields();
        for(Field field : fieldsArr){
            field.setAccessible(true);
            Object fieldObject = field.get(obj);
            if (fieldObject == null) {
                field.set(res, null);
                continue;
            }
            Class<?> fieldClass = fieldObject.getClass();
            if(fieldObject instanceof Cloneable){
                // Case1 - Object in field is cloneable
                Method cloneMethod = fieldClass.getDeclaredMethod("clone");
                cloneMethod.setAccessible(true);
                field.set(res, cloneMethod.invoke(fieldObject));
            }
            else if(copyConstructorExists(fieldClass)){
                // Case2 - Object in field is not cloneable but copy constructor exists
                Constructor<?> fieldClassCtor = fieldClass.getDeclaredConstructor(fieldClass);
                fieldClassCtor.setAccessible(true);
                field.set(res, fieldClassCtor.newInstance(fieldObject));
            }
            else{
                // Case3 - Object in field is not cloneable and copy constructor does not exist
                field.set(res, fieldObject);
            }
        }
        this.objectBackup = res;
    }

    /** Assigns into obj's fields the values in objectBackup fields. **/
    private void restoreInstance(Object obj) throws Exception{
        Field[] classFields = obj.getClass().getDeclaredFields();
        for(Field field : classFields) {
            field.setAccessible(true);
            field.set(obj, field.get(this.objectBackup));
        }
    }

    /** Returns the matching annotation class according to annotationName (Given, When or Then). **/
    private static Class<? extends Annotation> GetAnnotationClass(String annotationName){
        switch (annotationName) {
            case "Given": return Given.class;
            case "When": return When.class;
            case "Then": return Then.class;
        }
        return null;
    }

    /** checks if a given method matches a given annotationClass with a given sentence.
        If it does, the method returns true. Else, returns false. **/
    private boolean isMethodMatchingSentence(Method method, Class<? extends Annotation> annotationClass, String sentenceSub)
    {
        Annotation currAnnotation;
        try {
            currAnnotation = method.getAnnotation(annotationClass);
            Method valueMethod = annotationClass.getMethod("value");
            String value = (String)valueMethod.invoke(currAnnotation);
            value = value.substring(0, value.lastIndexOf(' '));
            if (!(value.equals(sentenceSub))) {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    /** Searches for a method in the test class (and through the test class's inheritance tree) that has the given annotation with
        the given sentence. If the matching method was found, the function invokes it. If not, throws an exception. **/
    private void invokeMethodBySentence (Class<? extends Annotation> annotationClass, String sentenceSub, String parameter, Object testInstance,
                                         Class<?> testClass)
            throws ComparisonFailure, WordNotFoundException, InvocationTargetException, IllegalAccessException {
        Method[] testMethods = testClass.getDeclaredMethods();
        for (Method method : testMethods)
        {
            if(isMethodMatchingSentence(method, annotationClass, sentenceSub)) {
                method.setAccessible(true);
                // Invokes the relevant method with the right parameter type.
                if (method.getParameterTypes()[0] == String.class)
                {
                    method.invoke(testInstance, parameter);
                    return;
                }
                else {
                    method.invoke(testInstance, Integer.parseInt(parameter));
                    return;
                }
            }
        }
        // If the matching method hasn't been found in the current class, this method looks for it in the class's super class.
        if (testClass.getSuperclass() != null) {
            invokeMethodBySentence(annotationClass, sentenceSub, parameter, testInstance, testClass.getSuperclass());
        }
        // If the matching method hasn't been found anywhere in the inheritance tree, the methods throws the relevant exception.
        else {
            switch((annotationClass.getName()).substring(annotationClass.getName().lastIndexOf('.') + 1)) {
                case "Given" : throw new GivenNotFoundException();
                case "When": throw new WhenNotFoundException();
                case "Then": throw new ThenNotFoundException();
            }
        }
    }

    @Override
    public void testOnInheritanceTree(String story, Class<?> testClass) throws Exception {
        if((story == null) || testClass == null) throw new IllegalArgumentException();
        this.numFails = 0;
        boolean storyFailed = false;
        int numWhen = 0;
        Object testInstance = createTestInstance(testClass);
        for(String sentence : story.split("\n")) {
            String[] words = sentence.split(" ", 2);
            String annotationName = words[0];
            Class<? extends Annotation> annotationClass = GetAnnotationClass(annotationName);
            if (annotationClass == null)
            {
                continue;
            }
            String sentenceSub = words[1].substring(0, words[1].lastIndexOf(' ')); // Sentence without the parameter and annotation
            String parameter = sentence.substring(sentence.lastIndexOf(' ') + 1);
            try {
                if (annotationName.equals("Then"))
                {
                    // Once the story reached "Then", the "When" sequence ends.
                    numWhen = 0;
                }
                if (annotationName.equals("When") && numWhen == 0) {
                    // Backs up the test instance once a "When" sequence begins.
                    this.backUpInstance(testInstance);
                    numWhen++;
                }
                invokeMethodBySentence(annotationClass, sentenceSub, parameter, testInstance, testClass);
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof ComparisonFailure) {
                    if (this.numFails == 0)
                    {
                        this.firstFailedSentence = "Then " + sentenceSub + " " + parameter;
                        this.expected = ((ComparisonFailure)(e.getCause())).getExpected();
                        this.result = ((ComparisonFailure)(e.getCause())).getActual();
                    }
                }
                storyFailed = true;
                this.restoreInstance(testInstance);
                this.numFails++;
            }
        }
        // Throws StoryTestExceptionImpl if the story failed.
        if (storyFailed) {
            throw new StoryTestExceptionImpl(this.firstFailedSentence, this.expected, this.result, this.numFails);
        }
    }

    @Override
    public void testOnNestedClasses(String story, Class<?> testClass) throws Exception, StoryTestExceptionImpl {
        try {
            testOnInheritanceTree(story, testClass);
        } catch (WordNotFoundException e) {
            // If the matching method hasn't been found in testClass's inheritance tree, the method looks for it inside
            // testClass's nested classes.
            Class<?>[] nestedClasses = testClass.getDeclaredClasses();
            if (!(e instanceof GivenNotFoundException))
            {
                // the "When" and "Then" annotations must be found where "Given" was found.
                throw e;
            }
            for (Class<?> nestedClass : nestedClasses) {
                if (createTestInstance(nestedClass) != null) {
                    testOnNestedClasses(story, nestedClass);
                }
            }
        }
    }
}
