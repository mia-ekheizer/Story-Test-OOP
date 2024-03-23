package tests;

import solution.Given;
import solution.Then;
import solution.When;
import org.junit.Assert;
import org.junit.ComparisonFailure;

public class StoryTestParent2 {
    protected B b;

    /**
     * This one should never be invoked!
     */
    @Given("B of x &num")
    public void B2(Integer _x) throws WrongMethodException {
        throw new WrongMethodException();
    }

    @When("B's y is &str")
    public void setY_B1(String str) {
        b.setY(str);
    }

    @Then("B's y is &str")
    public void isY_B(String str) {
        try {
            Assert.assertEquals(str, b.getY());
        }
        catch (Throwable e) {
            throw new ComparisonFailure(null, str, b.getY());
        }
    }

    /**
     * This one should never be invoked!
     */
    @Then("B's x is &num")
    public void isX_B2(Integer num) throws WrongMethodException {
        throw new WrongMethodException();
    }

    /**
     * This one should never be invoked!
     */
    @When("B's x is &num and B's y is &str")
    public void setXY_B2(Integer num, String str) throws WrongMethodException {
        throw new WrongMethodException();
    }

    class StoryTestInner1Parent2 {

    }

}
