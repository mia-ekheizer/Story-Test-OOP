package tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import provided.StoryTestException;
import provided.StoryTester;
import solution.StoryTesterImpl;

/**
 * This is a minimal test. Write your own tests!
 * Please don't submit your tests!
 */
public class DogTest {

	private StoryTester tester;
	private String goodStory;
	private String badStory;
	private String derivedStory;
	private String nestedStory;
	private Class<?> testClass;
	private Class<?> derivedTestClass;
	
	@Before
	public void setUp() throws Exception {
		goodStory = "Given a Dog of age 6\n"
				+ "When the dog is not taken out for a walk, and the number of hours is 5\n"
				+ "Then the house condition is clean";
		
		badStory = "Given a Dog of age 6\n"
				+ "When the dog is not taken out for a walk, and the number of hours is 5\n"
				+ "Then the house condition is smelly";
		
		derivedStory = "Given a Dog of age 6\n"
				+ "When the dog is not taken out for a walk, and the number of hours is 15\n"
				+ "When the house is cleaned, and the number of hours is 11\n"
				+ "Then the house condition is clean";
		
		nestedStory = "Given a Dog that his age is 6\n"
				+ "When the dog is not taken out for a walk, and the number of hours is 5\n"
				+ "Then the house condition is clean";
		testClass = DogStoryTest.class;
		derivedTestClass = DogStoryDerivedTest.class;
		tester = new StoryTesterImpl();
	}

	
	@Test
	public void test1() throws Exception {
		try {
			tester.testOnInheritanceTree(goodStory, testClass);
			Assert.assertTrue(true);
		} catch (StoryTestException e) {
			Assert.assertTrue(false);
		}
	}

	@Test
	public void test2() throws Exception {
		try {
			tester.testOnNestedClasses(badStory, testClass);
			Assert.assertTrue(false);
		} catch (StoryTestException e) {
			Assert.assertTrue(true);
			Assert.assertEquals("Then the house condition is smelly", e.getSentance());
			Assert.assertEquals("smelly", e.getStoryExpected());
			Assert.assertEquals("clean", e.getTestResult());
		}
	}
	
	@Test
	public void test3() throws Exception {
		try {
			tester.testOnNestedClasses(derivedStory, derivedTestClass);
			Assert.assertTrue(true);
		} catch (StoryTestException e) {
			Assert.assertTrue(false);
		}
	}
	@Test
	public void test4() throws Exception {		
		try {
			tester.testOnNestedClasses(nestedStory, derivedTestClass);
			Assert.assertTrue(true);
		} catch (StoryTestException e) {
			Assert.assertTrue(false);
		}
	}

}
