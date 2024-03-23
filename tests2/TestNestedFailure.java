package tests2;

import org.junit.Assert;
import org.junit.Test;
import provided.StoryTestException;
import provided.StoryTester;
import solution.Given;
import solution.StoryTesterImpl;
import solution.Then;
import solution.When;
import tests.Dog;

public class TestNestedFailure
{
    static class Base
    {
        Base(){

        }

        Integer counter = 100;

        @Given("a number &number")
        public void aDog(Integer number)
        {
            counter = number;
        }

        @When("the number increase by &amount")
        public void dogNotTakenForAWalk(Integer amount) {
            counter += amount;
        }

        @Then("the number is &value")
        public void theHouseCondition(Integer value) {
            Assert.assertEquals(value.toString(), counter.toString());
        }

    }


    @Test
    public void failureAndBackup() throws Exception
    {
        String story = "Given a number 100\n"
                + "When the number increase by 10\n"
                + "Then the number is 110\n" //TRUE
                + "When the number increase by 5\n"
                + "When the number increase by 15\n"
                + "When the number increase by 10\n"
                + "Then the number is 140\n" //TRUE
                + "Then the number is 140\n" //TRUE
                + "Then the number is 140\n" //TRUE
                + "When the number increase by -10\n"
                + "When the number increase by -15\n"
                + "When the number increase by -5\n"
                + "Then the number is 110\n" // TRUE
                + "Then the number is 666\n" //FAILE
                + "Then the number is 110\n" //GO BACK, TRUE
                + "Then the number is 140\n" //FAILE
                + "Then the number is 110\n" //STAY BACK TRUE
                + "When the number increase by 10\n"
                + "When the number increase by 10\n"
                + "Then the number is 160\n" //TRUE
                + "Then the number is 160\n"; //TRUE

        try
        {
            StoryTester tester = new StoryTesterImpl();
            tester.testOnInheritanceTree(story, Base.class);
        }
        catch (StoryTestException e)
        {
            Assert.assertEquals("666", e.getStoryExpected());
            Assert.assertEquals("110", e.getTestResult());
            Assert.assertEquals("Then the number is 666", e.getSentance());
            Assert.assertEquals(3, e.getNumFail());
        }

    }

}
