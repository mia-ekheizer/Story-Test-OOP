package tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import provided.*;
import solution.StoryTesterImpl;


public class CatTest {


	/**
	 * This is a minimal test. Write your own tests!
	 * Please don't submit your tests!
	 */


	private StoryTester tester;
	private String goodStory;
	private String badStory;
	private Class<?> testClass;

	@Before
	public void setUp() {
		goodStory = "Given a Cat of age 6\n"
				+ "When the Cat is not taken out for a walk, the number of hours is 5\n"
				+ "Then the house condition is clean";

		badStory = "Given a Cat of age 6\n"
				+ "When the Cat is not taken out for a walk, the number of hours is 5\n"
				+ "Then the house condition is smelly";

		testClass = CatStory.class;
		//derivedTestClass = DogStoryDerivedTest.class;
		tester = new StoryTesterImpl();
	}


	@Test
	public void errors() throws Exception {

		try {
			tester.testOnInheritanceTree(null, testClass);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertTrue(true);
		}

		try {
			tester.testOnInheritanceTree(goodStory, null);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertTrue(true);
		}

		testClass = EmptyB.class;
		try {
			tester.testOnInheritanceTree(goodStory, testClass);
			Assert.fail();
		} catch (GivenNotFoundException e) {
			Assert.assertTrue(true);
		}

		testClass = NoWhen.class;
		try {
			tester.testOnInheritanceTree(goodStory, testClass);
			Assert.fail();
		} catch (WhenNotFoundException e) {
			Assert.assertTrue(true);
		}

		testClass = NoThen.class;
		try {
			tester.testOnInheritanceTree(goodStory, testClass);
			Assert.fail();
		} catch (ThenNotFoundException e) {
			Assert.assertTrue(true);
		}


	}

	@Test
	public void test1() throws Exception {


		try {
			tester.testOnInheritanceTree(goodStory, testClass);
			Assert.assertTrue(true);
		} catch (StoryTestException e) {
			Assert.fail();
		}

		try {
			tester.testOnInheritanceTree(badStory, testClass);
			Assert.fail();
		} catch (StoryTestException e) {
			Assert.assertTrue(true);
		}


		goodStory = "Given a Cat of age 6\n"
				+ "When the Cat is not taken out for a walk, the number of hours is 5\n"
				+ "Then the house condition is clean";
		try {
			tester.testOnInheritanceTree(goodStory, testClass);
			Assert.assertTrue(true);
		} catch (StoryTestException e) {
			Assert.fail();
		}

		badStory = "Given a Cat of age 6\n"
				+ "When the Cat is not taken out for a walk, the number of hours is 5\n"
				+ "Then the house condition is smelly";
		try {
			tester.testOnInheritanceTree(badStory, testClass);
			Assert.fail();
		} catch (StoryTestException e) {
			Assert.assertEquals(e.getNumFail(), 1);
			Assert.assertTrue(true);
		}

		goodStory = "Given a Cat of age 6\n"
				+ "When the Cat is not taken out for a walk, the number of hours is 5\n"
				+ "Then the house condition is clean\n"
				+ "When the Cat is not taken out for a walk, the number of hours is 5\n"
				+ "Then the house condition is clean";

		try {
			tester.testOnInheritanceTree(goodStory, testClass);
			Assert.assertTrue(true);
		} catch (StoryTestException e) {
			Assert.fail();
		}

		badStory = "Given a Cat of age 6\n"
				+ "When the Cat is not taken out for a walk, the number of hours is 5\n"
				+ "Then the house condition is smelly\n"
				+ "When the Cat is not taken out for a walk, the number of hours is 5\n"
				+ "Then the house condition is smelly";

		try {
			tester.testOnInheritanceTree(badStory, testClass);
			Assert.fail();
		} catch (StoryTestException e) {
			Assert.assertEquals(e.getNumFail(), 2);
			Assert.assertTrue(true);
		}

		goodStory = "Given a Cat of age 6\n"
				+ "When the Cat is not taken out for a walk, the number of hours is 5\n"
				+ "Then the house condition is clean\n"
				+ "When the Cat is not taken out for a walk, the number of hours is 5\n"
				+ "Then the house condition is clean\n"
				+ "When the Cat is not taken out for a walk, the number of hours is 5\n"
				+ "Then the house condition is smelly";
		try {
			tester.testOnInheritanceTree(goodStory, testClass);
			Assert.fail();
		} catch (StoryTestException e) {
			Assert.assertTrue(true);
		}


		goodStory = "Given a Cat of age 6\n"
				+ "When the Cat is not taken out for a walk, the number of hours is 10\n"
				+ "Then the house condition is clean\n"
				+ "When the Cat is not taken out for a walk, the number of hours is 5\n"
				+ "Then the house condition is clean";
		try {
			tester.testOnInheritanceTree(goodStory, testClass);
			Assert.fail();
		} catch (StoryTestException e) {
			Assert.assertTrue(true);
		}

		goodStory = "Given a Cat of age 6 and name laki\n"
				+ "When the Cat is not taken out for a walk, the number of hours is 10\n"
				+ "Then the house condition is clean\n"
				+ "When the Cat is not taken out for a walk, the number of hours is 5\n"
				+ "Then the house condition is clean";
		try {
			tester.testOnInheritanceTree(goodStory, testClass);
			Assert.fail();
		} catch (StoryTestException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void test2() throws Exception {
		goodStory = "Given a Cat of age 6 and name laki\n"
				+ "When the Cat did kaki of size 1\n"
				+ "Then the kaki size is 1";

		try {
			tester.testOnInheritanceTree(goodStory, testClass);
			Assert.assertTrue(true);
		} catch (StoryTestException e) {
			Assert.fail();
		}

		badStory = "Given a Cat of age 6 and name laki\n"
				+ "When the Cat did kaki of size 1\n"
				+ "Then the kaki size is 0";

		try {
			tester.testOnInheritanceTree(badStory, testClass);
			Assert.fail();
		} catch (StoryTestException e) {
			Assert.assertTrue(true);
			Assert.assertEquals("Then the kaki size is 0", e.getSentance());
			Assert.assertEquals(e.getStoryExpected(), "0");
			Assert.assertEquals(e.getTestResult(), "1");
			Assert.assertEquals(e.getNumFail(), 1);
		}

		try {
			tester.testOnInheritanceTree(badStory, testClass);
			Assert.fail();
		} catch (StoryTestException e) {
			Assert.assertTrue(true);
		}

		badStory = "Given a Cat of age 6 and name laki\n"
				+ "When the Cat did kaki of size 1\n"
				+ "Then the kaki size is 0\n" //fail
				+ "When the Cat did kaki of size 1\n"
				+ "Then the kaki size is 1\n"
				+ "When the Cat did kaki of size 2\n"
				+ "Then the kaki size is 0\n" //fail
				+ "Then the kaki size is 1";
		try {
			tester.testOnInheritanceTree(badStory, testClass);
			Assert.fail();
		} catch (StoryTestException e) {
			Assert.assertTrue(true);

			Assert.assertEquals("Then the kaki size is 0", e.getSentance());
			Assert.assertEquals(e.getStoryExpected(), "0");
			Assert.assertEquals(e.getTestResult(), "1");
			Assert.assertEquals(e.getNumFail(), 2);
		}
	}
}

