package tests;

import solution.Given;
import solution.When;

public class NoThen {
	protected Cat cat;

	@Given("a Cat of age &age")
	public void aCat(Integer age) {
		cat = new Cat(age);
	}


	@Given("a Cat of age &age and name &name")
	public void aCatWithName(Integer age, String name) {
		cat = new Cat(age, name);
	}

	@When("the Cat is not taken out for a walk, the number of hours is &hours")
	public void catNotTakenForAWalk(Integer hours) {
		cat.notTakenForAWalk(hours);
	}


}
