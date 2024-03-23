package tests2;

import org.junit.Assert;
import org.junit.Test;
import solution.Given;
import solution.Then;
import solution.When;

import java.lang.reflect.Method;
import solution.StoryTesterImpl;

public class TestAnnotate
{
    class Annotated
    {
        @Then("thenAnnotate value")
        public void thenAnnotate()
        {}

        @Given("givenAnnotate value")
        public void givenAnnotate()
        {}

        @When("whenAnnotate value")
        public void whenAnnotate()
        {}

    }


    @Test
    public void hasInfo() throws NoSuchMethodException
    {
        Method then = Annotated.class.getDeclaredMethod("thenAnnotate");
        Assert.assertEquals(then.getAnnotation(Then.class).value(), "thenAnnotate value");
        Assert.assertNull(then.getAnnotation(When.class));
        Assert.assertNull(then.getAnnotation(Given.class));

        Method when = Annotated.class.getDeclaredMethod("whenAnnotate");
        Assert.assertEquals(when.getAnnotation(When.class).value(), "whenAnnotate value");
        Assert.assertNull(when.getAnnotation(Then.class));
        Assert.assertNull(when.getAnnotation(Given.class));

        Method given = Annotated.class.getDeclaredMethod("givenAnnotate");
        Assert.assertEquals(given.getAnnotation(Given.class).value(), "givenAnnotate value");
        Assert.assertNull(given.getAnnotation(When.class));
        Assert.assertNull(given.getAnnotation(Then.class));
    }

    
}
