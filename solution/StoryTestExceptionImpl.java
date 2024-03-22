package solution;

import provided.StoryTestException;

public class StoryTestExceptionImpl extends StoryTestException {
    String firstFailedSentence;
    String expected;
    String result;
    int numFails;

    StoryTestExceptionImpl(String sentence, String expected, String result, int numFails) {
        this.firstFailedSentence = sentence;
        this.expected = expected;
        this.result = result;
        this.numFails = numFails;
    }

    public String getSentance() {
        return firstFailedSentence;
    }

    public String getStoryExpected()
    {
        return expected;
    }

    public String getTestResult()
    {
        return result;
    }

    public int getNumFail()
    {
        return numFails;
    }
}
