package tests;

import org.junit.Assert;

import solution.Given;
import solution.Then;
import solution.When;
import org.junit.ComparisonFailure;

/**
 * Notice: Some of the methods are to be found up the hierarchy tree.
 */
public class StoryTest extends StoryTestParent1 {
	private A a;

	@Given("A of x &x")
	public void A1(Integer x) {
		a = new A(x);
	}

	@Then("A's x is &x")
	public void isX(Integer x) throws NotShortCircuitException {
		if (x.equals(1024)) throw new NotShortCircuitException();
		try {
			Assert.assertEquals(x, a.getX());
		} catch (Throwable e) {
			throw new ComparisonFailure(null, x.toString(), a.getX().toString());
		}
	}


	@Given("A of x &x and of z &z")
	public void A2(Integer x, String z) {
		a = new A(x, z);
	}

	@Given("A of x 4 and of z &z")
	public void A2(String z) {
		a = new A(4, z);
	}

	@When("A's y is &y")
	private void A_y(String y) {
		if (y.equals("true")) {
			a.setY(true);
		} else {
			a.setY(false);
		}
	}

	@When("A's x is &x")
	public void A_x(Integer x) {
		a.setX(x);
	}

	@When("A's z is &z")
	private void A_z(String z) throws NotShortCircuitException {
		if (z.equals("Exception")) throw new NotShortCircuitException();
		a.setZ(z);
	}

	@When("A's x is &x and y is &y")
	public void A_xy(Integer x, String y) {
		A_x(x);
		A_y(y);
	}

	@When("A's x is &x and y is &y and z is &z")
	public void A_xyz(Integer x, String y, String z) throws NotShortCircuitException {
		A_xy(x, y);
		A_z(z);
	}

	@Then("A's y is &y")
	private void isY(String y) {
		try {
			Assert.assertEquals(y.equals("true"), a.getY());
		} catch (Throwable e) {
			throw new ComparisonFailure(null, y, a.getY().equals(true) ? "true" : "false");
		}
	}

	@Then("A's z is &z")
	public void isZ(String z) {
		try {
			Assert.assertEquals(z, a.getZ());
		} catch (Throwable e) {
			throw new ComparisonFailure(null, z, a.getZ());
		}
	}

	@When("A's conditional x is &num")
	public void setXifY(Integer x) {
		if (a.getY()) a.setX(x);
	}

	@When("B's x is &num")
	public void setX_B(Integer num) {
		b.setX(num);
	}

	class StoryTestInner1 extends StoryTestInner1Parent1 {

		@Given("B DerivedConstructor &param")
		private void derivedB1(Integer param) {
			b = new B(param);
		}

		@When("B's x is 4 and B's y is &str")
		protected void setXY_B56(String str) {
			b.setX(4);
			b.setY(str);
		}

	}

	class StoryTestInner2 {
		private C c;

		@Given("C of x &num")
		private void C(Integer num) {
			c = new C(num);
		}

		@When("C's y is &num")
		protected void setY(Integer num) {
			c.setY(num);
		}

		@Then("C's x is &x")
		void isX(Integer x) {
			try {
				Assert.assertEquals(x, c.getX());
			} catch (Throwable e) {
				throw new ComparisonFailure(null, x.toString(), c.getX().toString());
			}
		}

		@Then("C's y is &y")
		void isY(Integer y) {
			try {
				Assert.assertEquals(y, c.getY());
			} catch (Throwable e) {
				throw new ComparisonFailure(null, y.toString(), c.getY().toString());
			}
		}

		@When("DO &NOTHING")
		private void nothing(String NOTHING) {
			return;
		}
	}

	class Inner1 {
		protected C c;

		class i1 {
			class i2 {
				class i3 {

				}
			}
		}

		class Inner2 {
			class Inner3 {
				class Inner4 {
					@Given("C of Inner x &num")
					private void C654(Integer num) {
						c = new C(num);
					}

					@When("C's Inner y is &num")
					protected void setY654(Integer num) {
						c.setY(num);
					}

					@Then("C's Inner y is &y")
					void isY654(Integer y) {
						try {
							Assert.assertEquals(y, c.getY());
						} catch (Throwable e) {
							throw new ComparisonFailure(null, y.toString(), c.getY().toString());
						}
					}

					@Then("C's Inner x is &y")
					void isX654(Integer x) {
						try {
							Assert.assertEquals(x, c.getX());
						} catch (Throwable e) {
							throw new ComparisonFailure(null, x.toString(), c.getX().toString());
						}
					}
				}
			}
		}
	}

	class j1 {
		class j2 {
			class j3 {

			}
		}
	}

}
