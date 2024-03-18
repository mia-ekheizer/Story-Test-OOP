package provided;


public abstract class StoryTestException extends Exception {
	private static final long serialVersionUID = 95576353840828036L;
	
	/**
	 * Returns a string representing the sentence 
	 * of the first Then sentence that failed
	 */
	public abstract String getSentance();
	
	/**
	 * Returns a string representing the expected value from the story 
	 * of the first Then sentence that failed.
	 */
	public abstract String getStoryExpected();
	
	/**
	 * Returns a string representing the actual value.
	 * of the first Then sentence that failed.
	 */
	public abstract String getTestResult();
	
	/**
	 * Returns an int representing the number of Then sentences that failed.
	 */
	public abstract int getNumFail();
}
