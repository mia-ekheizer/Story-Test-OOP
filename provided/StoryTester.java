package provided;

/**
 * The behaviour driven development test unit.
 */
public interface StoryTester {
	
	/**
	 * Runs a given story on an instance of a given class, or an instances of its 
	 * ancestors. before running the story use must create an instance of the given
	 * class.
	 * 
	 * @param story contains the text of the story to test, the string is 
	 * divided to line using '\n'. each word in a line is separated by space 
	 * (' ').
	 * @param testClass the test class which the story should be run on.
	 */
	public void testOnInheritanceTree(String story, Class<?> testClass) 
			throws Exception;
	/**
	 * Runs a given story on an instance of a given class, or an instances of its 
	 * ancestors, or its nested class (and their ancestors) as described by the 
	 * the assignment document. before running the story use must create an instance 
	 * of the given correct class to run story on.
	 * 
	 * @param story contains the text of the story to test, the string is 
	 * divided to line using '\n'. each word in a line is separated by space 
	 * (' ').
	 * @param testClass the test class which the story should be run on.
	 */
	public void testOnNestedClasses(String story, Class<?> testClass) 
			throws Exception;
}
