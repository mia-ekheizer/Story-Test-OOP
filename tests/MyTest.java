package tests;

import provided.*;
import solution.StoryTesterImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MyTest {
	private StoryTester tester;
	private String Story1;
	private String Story2;
	private String Story3;
	private String Story5_1;
	private String Story5_2;
	private String Story5_3;
	private String Story6;
	private String Story7;
	private String Story8;
	private String Story9_1;
	private String Story9_2;
	private String Story9_3;
	private String Story10;
	private String Story11;
	private String Story12;
	private Class<?> testClass;

	@Before
	public void setUp() {
		Story1 = "Given A of x 4\n"
				+ "When A's y is true\n"
				+ "Then A's y is true"; // Should succeed.

		Story2 = "Given A of x 4 and of z Word\n"
				+ "When A's y is true\n"
				+ "Then A's y is true\n" // Should succeed.
				+ "When A's y is false\n"
				+ "Then A's y is true\n" // Should fail.
				+ "When A's z is DOESN'T_MATTER\n"
				+ "Then A's y is true" // This one should succeed, because of the Back-up.
		;

		Story3 = "Given A of x -3\n"
				+ "When A's y is true\n"
				+ "Then A's x is -3"; // Should succeed.

		Story5_1 = "Given A of x 3\n"
				+ "When NOT_TO_BE_FOUND 5\n"
				+ "Then A's x is 7 or A's x is -3"; // Shouldn't get here - should throw an exception.

		Story5_2 = "Given NOT_TO_BE_FOUND 4\n"
				+ "When NOT_TO_BE_FOUND 4\n"
				+ "Then A's x is 4 or A's x is -3"; // Shouldn't get here - should throw an exception.

		Story5_3 = "Given A of x 55\n"
				+ "When A's z is DOESN'T_MATTER\n"
				+ "Then NOT_TO_BE_FOUND 4"; // Should throw an exception.

		Story6 = "Given A of x 54\n"
				+ "When A's y is true\n"
				+ "Then A's x is 54"; // This one should succeed and not throw an exception (In this test evaluation with 1024 throws a NotShortCircuitException).

		Story7 = "Given A of x 4 and of z Word\n"
				+ "When A's conditional x is 50\n" // Sets x to 50, as y is true by default.
				+ "Then A's x is 50\n" // Should succeed.
				+ "When A's y is false\n"
				+ "When A's z is WORD\n"
				+ "Then A's z is Word\n" // Should fail.
				+ "When A's y is false\n"
				+ "Then A's z is Word\n" // Now it should succeed (backed-up).
				+ "When A's y is false\n"
				+ "Then A's z is Word\n" // Should succeed (thanks to the 2nd expression).
				+ "When A's x is -5\n"
				+ "Then A's x is 4" // Should fail.
		;

		Story8 = "Given B of x 1\n"
				+ "When B's y is Word\n"
				+ "Then B's x is 1"; // Should succeed.

		Story9_1 = "Given B DerivedConstructor 4\n" // Shouldn't find this method.
				+ "When B's y is Word\n"
				+ "Then B's x is 1";

		Story9_2 = "Given B DerivedConstructor 4\n"
				+ "When B's x is 4 and B's y is Word\n"
				+ "Then B's y is Word"; // Should succeed.

		Story9_3 = "Given B DerivedConstructor 4\n"
				+ "When B's x is 4\n" // This one should not be found! As this method belongs to StoryTest class and not to the inner classes.
				+ "Then B's y is Word"; // Shouldn't get here - should throw an exception.

		Story10 = "Given C of x 4\n"
				+ "When C's y is 3\n"
				+ "Then C's y is 2\n" // Should fail.
				+ "When DO NOTHING\n"
				+ "Then C's y is 2" // This one should succeed (backed-up).
		;

		Story11 = "Given C of Inner x 4\n"
				+ "When C's y is 3\n" // Shouldn't be found.
				+ "Then DOESN'T_MATTER 8\n" // Shouldn't get here.
		;

		Story12 = "Given C of Inner x 4\n"
				+ "When C's Inner y is 3\n"
				+ "Then C's Inner y is 3\n" // Should succeed.
		;

		testClass = StoryTest.class;
		tester = new StoryTesterImpl();
	}

	/**
	 * Tests 1-7 use class A.
	 * Tests 8-9 use class B.
	 * Test 10 and above use class C.
	 */

	/**
	 * A simple first test that should succeed.
	 */
	@Test
	public void test1() throws Exception {
		try {
			tester.testOnInheritanceTree(Story1, testClass);
			Assert.assertTrue(true);
		} catch (StoryTestException | WrongMethodException e) {
			Assert.fail();
		}
	}

	/**
	 * This test checks the backup.
	 * If you fail this than you might not have implemented your backup as required.
	 */
	@Test
	public void test2() throws Exception {
		try {
			tester.testOnInheritanceTree(Story2, testClass);
			Assert.fail();
		} catch (StoryTestException e) {
			Assert.assertEquals("Then A's y is true", e.getSentance());
			Assert.assertEquals(1, e.getNumFail()); // If this fails, it means your back-up is wrong.
			Assert.assertEquals("true", e.getStoryExpected());
			Assert.assertEquals("false", e.getTestResult());
		}
	}

	/**
	 * This one checks if you parse negative numbers correctly,
	 * and checks that your "or" in the Then sentence is working correctly.
	 */
	@Test
	public void test3() throws Exception {
		try {
			tester.testOnInheritanceTree(Story3, testClass);
			Assert.assertTrue(true);
		} catch (StoryTestException | WrongMethodException e) {
			Assert.fail();
		}
	}

	/**
	 * This one checks invalid arguments handling.
	 */
	@Test
	public void test4() {
		class mini_tests {
			public void test4_1() {
				try {
					tester.testOnInheritanceTree(null, testClass);
					Assert.fail();
				} catch (IllegalArgumentException e) {
					Assert.assertTrue(true);
				} catch (Throwable e) {
					Assert.fail();
				}
			}

			public void test4_2() {
				try {
					tester.testOnInheritanceTree(Story3, null);
					Assert.fail();
				} catch (IllegalArgumentException e) {
					Assert.assertTrue(true);
				} catch (Throwable e) {
					Assert.fail();
				}
			}

			public void test4_3() {
				try {
					tester.testOnInheritanceTree(null, null);
					Assert.fail();
				} catch (IllegalArgumentException e) {
					Assert.assertTrue(true);
				} catch (Throwable e) {
					Assert.fail();
				}
			}
		}
		mini_tests t = new mini_tests();
		t.test4_1();
		t.test4_2();
		t.test4_3();
	}

	/**
	 * This one checks handling of methods that can't be found.
	 */
	@Test
	public void test5() throws Exception {
		class mini_tests {
			public void test5_1() {
				try {
					tester.testOnInheritanceTree(Story5_1, testClass);
					Assert.fail();
				} catch (WhenNotFoundException e) {
					Assert.assertTrue(true);
				} catch (Throwable e) {
					Assert.fail();
				}
			}

			public void test5_2() {
				try {
					tester.testOnInheritanceTree(Story5_2, testClass);
					Assert.fail();
				} catch (GivenNotFoundException e) {
					Assert.assertTrue(true);
				} catch (Throwable e) {
					Assert.fail();
				}
			}

			public void test5_3() {
				try {
					tester.testOnInheritanceTree(Story5_3, testClass);
					Assert.fail();
				} catch (ThenNotFoundException e) {
					Assert.assertTrue(true);
				} catch (Throwable e) {
					Assert.fail();
				}
			}
		}
		mini_tests t = new mini_tests();
		t.test5_1();
		t.test5_2();
		t.test5_3();
	}

	/**
	 * This one checks your "short-circuit evaluation" in the Then sentence.
	 * If you throw an exception (NotShortCircuitException) then your evaluation isn't short-circuit.
	 */
	@Test
	public void test6() throws Exception {
		try {
			tester.testOnInheritanceTree(Story6, testClass);
			Assert.assertTrue(true);
		} catch (NotShortCircuitException e) {
			Assert.fail();
		}
	}

	/**
	 * This one is quite general: checks back-ups, short-circuit evaluations and story-failure handling.
	 */
	@Test
	public void test7() throws Exception {
		try {
			tester.testOnInheritanceTree(Story7, testClass);
			Assert.fail();
		} catch (StoryTestException e) {
			Assert.assertEquals("Then A's z is Word", e.getSentance());
			Assert.assertEquals(2, e.getNumFail());
			Assert.assertEquals("Word", e.getStoryExpected());
			Assert.assertEquals("WORD", e.getTestResult());
		} catch (ThenNotFoundException e) {
			Assert.fail();
		}
	}

	/**
	 * This one checks that your method-finding algorithm is correct (InheritanceTree).
	 */
	@Test
	public void test8() throws Exception {
		try {
			tester.testOnInheritanceTree(Story8, testClass);
			Assert.assertTrue(true);
		} catch (StoryTestException | WrongMethodException e) {
			Assert.fail();
		}
	}

	/**
	 * This one checks that your method-finding algorithm is correct (Nested class).
	 */
	@Test
	public void test9() {
		class mini_tests {
			public void test9_1() {
				try {
					tester.testOnInheritanceTree(Story9_1, testClass);
					Assert.fail();
				} catch (GivenNotFoundException e) {
					Assert.assertTrue(true);
				} catch (Throwable e) {
					Assert.fail();
				}
			}

			public void test9_2() {
				try {
					tester.testOnNestedClasses(Story9_2, testClass);
					Assert.assertTrue(true);
				} catch (Throwable e) {
					Assert.fail();
				}
			}

			public void test9_3() {
				try {
					tester.testOnNestedClasses(Story9_3, testClass);
					Assert.fail();
				} catch (WhenNotFoundException e) {
					Assert.assertTrue(true);
				} catch (Throwable e) {
					Assert.fail();
				}
			}
		}
		mini_tests t = new mini_tests();
		t.test9_1();
		t.test9_2();
		t.test9_3();
	}

	/**
	 * This one checks your back-up for inner classes.
	 */
	@Test
	public void test10() throws Exception {
		try {
			tester.testOnNestedClasses(Story10, testClass);
			Assert.fail();
		} catch (StoryTestException e) {
			Assert.assertEquals("Then C's y is 2", e.getSentance());
			Assert.assertEquals(1, e.getNumFail()); // If this fails, it means your back-up is wrong.
			Assert.assertEquals("2", e.getStoryExpected());
			Assert.assertEquals("3", e.getTestResult());
		}
	}

	/**
	 * This one checks a 4-level nested test class - method matching algorithm.
	 */
	@Test
	public void test11() throws Exception {
		try {
			tester.testOnNestedClasses(Story11, testClass);
			Assert.fail();
		} catch (WhenNotFoundException e) {
			Assert.assertTrue(true);
		}
	}

	/**
	 * This one checks a 4-level nested test class - successful story.
	 */
	@Test
	public void test12() throws Exception {
		try {
			tester.testOnNestedClasses(Story12, testClass);
			Assert.assertTrue(true);
		} catch (StoryTestException e) {
			Assert.fail();
		}
	}
}
