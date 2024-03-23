package tests2;


import org.junit.Assert;
import org.junit.Test;
import provided.StoryTestException;
import solution.Given;
import solution.StoryTesterImpl;
import solution.Then;
import solution.When;
import org.junit.Before;


public class TestNested
{
    Class<StarWars> starWars = StarWars.class;
    StoryTesterImpl tester = new StoryTesterImpl();
    String goodStory, badStory, goodStory2, badStory2;


    public static class StarWars
    {
        class Jedi
        {
            public Integer strength;
            public String name;
            public Jedi()
            {
                this.name = "Jedi";
                this.strength = 100;
            }

            @Given("a Jedi named &name")
            public void setJediName(String name)
            {
                this.name = name;
            }

            @When("the Jedi fights a Sith &name")
            public void fightSith(String name)
            {
                if(name.equals("DarthVader"))
                    this.strength = 0;
                else
                    this.strength += 10;

            }

            @Then("the Jedi strength is &strength")
            public void checkStrength(String strength)
            {
                Assert.assertEquals(strength, this.strength.toString());
            }


        }

        static class Sith
        {
            public Integer strength;
            public String name;
            public Sith()
            {
                this.name = "Sith";
                this.strength = 100;
            }

            @Given("a Sith named &name")
            public void setSithName(String name)
            {
                this.name = name;
            }

            @When("the Sith fights a Jedi &name")
            public void fightJedi(String name)
            {
                if(name.equals("LukeSkywalker"))
                    this.strength = 0;
                else
                    this.strength += 10;

            }

            @Then("the Sith strength is &strength")
            public void checkStrength(String strength)
            {
                Assert.assertEquals(strength, this.strength.toString());
            }


        }
    }

    @Before
    public void testNestedClass()
    {
        goodStory = "Given a Jedi named JarJar";
        goodStory += "\nWhen the Jedi fights a Sith DarthVader";
        goodStory += "\nThen the Jedi strength is 0";

        badStory = "Given a Jedi named JarJar";
        badStory += "\nWhen the Jedi fights a Sith DarthVader";
        badStory += "\nThen the Jedi strength is 100";

        goodStory2 = "Given a Sith named DarthVader";
        goodStory2 += "\nWhen the Sith fights a Jedi LukeSkywalker";
        goodStory2 += "\nThen the Sith strength is 0";

        badStory2 = "Given a Sith named DarthVader";
        badStory2 += "\nWhen the Sith fights a Jedi LukeSkywalker";
        badStory2 += "\nThen the Sith strength is 100";

    }

    @Test
    public void test1()
    {

        try {
            tester.testOnNestedClasses(goodStory, starWars);
        } catch (Exception testException)
        {
            Assert.fail();
        }
    }

    @Test
    public void test2()
    {
        try {
            tester.testOnNestedClasses(badStory, starWars);
            Assert.fail();
        } catch (StoryTestException testException)
        {
            Assert.assertEquals("Then the Jedi strength is 100", testException.getSentance());
        } catch (Exception e)
        {
            Assert.fail();
        }
    }

    @Test
    public void test3()
    {
        try {
            tester.testOnNestedClasses(goodStory2, starWars);
        } catch (Exception testException)
        {
            Assert.fail();
        }
    }


    @Test
    public void test4()
    {
        try
        {
            tester.testOnNestedClasses(badStory2, starWars);
            Assert.fail();
        } catch (StoryTestException testException)
        {
            Assert.assertEquals("Then the Sith strength is 100", testException.getSentance());
        } catch (Exception e)
        {
            Assert.fail();
        }
    }


}
