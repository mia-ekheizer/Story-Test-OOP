package tests;

import solution.Given;
import solution.Then;
import solution.When;
import org.junit.Assert;
import org.junit.ComparisonFailure;


public class StoryTestParent1 extends StoryTestParent2 {

    /**
     * This one should never be invoked!
     */
    @Given("A of x &num")
    public void A3_2(Integer _x) throws WrongMethodException {
        throw new WrongMethodException();
    }

    @Given("B of x &num")
    public void B2(Integer _x) {
        b = new B(_x);
    }


    @When("B's x is &num and B's y is &str")
    protected void setXY_B5(Integer num, String str) {
        b.setX(num);
        b.setY(str);
    }

    @Then("B's x is &num")
    private void isX_B(Integer num) {
        try {
            Assert.assertEquals(num, b.getX());
        }
        catch (Throwable e) {
            throw new ComparisonFailure(null, num.toString(), b.getX().toString());
        }
    }

    /**
     * This one should never be invoked!
     */
    @When("B's x is &num")
    private void setX_B3(Integer num) throws WrongMethodException {
        throw new WrongMethodException();
    }

    class StoryTestInner1Parent1 extends StoryTestInner1Parent2 {

        @Then("B's y is &str")
        private void isY_B6(String str) {
            try {
                Assert.assertEquals(str, b.getY());
            }
            catch (Throwable e) {
                throw new ComparisonFailure(null, str, b.getY());
            }
        }

    }

}
