package solution;

import org.junit.ComparisonFailure;
import provided.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
public class StoryTesterImpl implements StoryTester {

    private Object objectBackup;

    String firstFailedSentence;
    String expected;
    String result;
    int numFails;

    /** if the testClass is a nested class, this function recursively creates it's enclosing classes **/
    public static Object constructEnclosingClasses(Class<?> classObject) throws Exception {
        if (classObject.getEnclosingClass() == null) {
            try {
                return classObject.getConstructor().newInstance();
            } catch (Exception e) {
                return null;
            }
        }
        constructEnclosingClasses(classObject.getEnclosingClass());
        try {
            return classObject.getConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    /** Creates and returns a new instance of testClass **/
    private static Object createTestInstance(Class<?> testClass) throws Exception {
        try {
            // TODO: Try constructing a new instance using the default constructor of testClass
            return testClass.getConstructor().newInstance();
        } catch (Exception e) {
            // TODO: Inner classes case; Need to first create an instance of the enclosing class
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

    /** Assigns into objectBackup a backup of obj.
    /** See homework's pdf for more details on backing up and restoring **/
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
                field.set(res, fieldClass.getDeclaredConstructor(fieldClass).newInstance(fieldObject));
            }
            else{
                // Case3 - Object in field is not cloneable and copy constructor does not exist
                field.set(res, fieldObject);
            }
        }
        this.objectBackup = res;
    }

    /** Assigns into obj's fields the values in objectBackup fields.
    /** See homework's pdf for more details on backing up and restoring **/
    private void restoreInstance(Object obj) throws Exception{
        Field[] classFields = obj.getClass().getDeclaredFields();
        for(Field field : classFields) {
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

    /** checks if a given method matches a given annotationClass with a given sentence**/
    private boolean isMethodMatchingSentence(Method method, Class<? extends Annotation> annotationClass, String sentenceSub)
    {
        Annotation currAnnotation;
        try {
            currAnnotation = method.getAnnotation(annotationClass);
            Method valueMethod = annotationClass.getMethod("value");
            String value = (String)valueMethod.invoke(currAnnotation);
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

    /** Searches for a method in the test class (and through it's inheritance tree) that has the given annotation with
        the given sentence. If the matching method was found, the function invokes it. If not, throws an exception. **/
    private void invokeMethodBySentence (Class<? extends Annotation> annotationClass, String sentenceSub, String parameter, Object testInstance,
                                         Class<?> testClass)
            throws Exception
    {
        int parameterInt = 0;
        boolean isParamInt;
        try {
            parameterInt = Integer.parseInt(parameter);
            isParamInt = true;
        }
        catch (NumberFormatException e)
        {
            isParamInt = false;
        }

        Method[] testMethods = testClass.getDeclaredMethods();
        try {
            for (Method method : testMethods)
            {
                if(isMethodMatchingSentence(method, annotationClass, sentenceSub)) {
                    if (isParamInt) {
                        method.invoke(testInstance, parameterInt);
                        return;
                    }
                    else {
                        method.invoke(testInstance, parameter);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
        if (testClass.getSuperclass() != null) {
            invokeMethodBySentence(annotationClass, sentenceSub, parameter, testInstance, testClass.getSuperclass());
        }
        else {
            switch(annotationClass.getName()) {
                case "Given": throw new GivenNotFoundException();
                case "When": throw new WhenNotFoundException();
                case "Then": throw new ThenNotFoundException();
            }
        }
    }

    @Override
    public void testOnInheritanceTree(String story, Class<?> testClass) throws Exception {
        if((story == null) || testClass == null) throw new IllegalArgumentException();
        this.numFails = 0;
        Object testInstance = createTestInstance(testClass);
        for(String sentence : story.split("\n")) {
            boolean methodFound = false;
            String[] words = sentence.split(" ", 2);

            String annotationName = words[0];
            Class<? extends Annotation> annotationClass = GetAnnotationClass(annotationName);
            if (annotationClass == null)
            {
                continue;
            }
            String sentenceSub = words[1].substring(0, words[1].lastIndexOf(' ')); // Sentence without the parameter and annotation
            String parameter = sentence.substring(sentence.lastIndexOf(' ') + 1);
            if (annotationName.equals("Given")) {
                this.backUpInstance(testInstance);
            }
            try {
                invokeMethodBySentence(annotationClass, sentenceSub, parameter, testInstance, testClass);
            } catch (ComparisonFailure e) {
                if (this.numFails == 0)
                {
                    this.firstFailedSentence = sentenceSub;
                    this.expected = e.getExpected();
                    this.result = e.getActual();
                }
                this.restoreInstance(testInstance);
                this.numFails++;
            }
        }
        // Throws StoryTestExceptionImpl if the story failed.
        if (this.numFails > 0) {
            throw new StoryTestExceptionImpl(this.firstFailedSentence, this.expected, this.result, this.numFails);
        }
    }

    @Override
    public void testOnNestedClasses(String story, Class<?> testClass) throws Exception {
        try {
            testOnInheritanceTree(story, testClass);
        } catch (WordNotFoundException e) {
            try {
                Class<?>[] nestedClasses = testClass.getDeclaredClasses();
                for (Class<?> nestedClass : nestedClasses) {
                    createTestInstance(nestedClass);
                    testOnNestedClasses(story, nestedClass);
                }
            }
            catch (Exception except) {
                return;
            }
        }
    }
}
