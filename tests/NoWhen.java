package tests;

import org.junit.Assert;
import solution.Given;
import solution.Then;

public class NoWhen {
	protected Cat cat;

	@Given("a Cat of age &age")
	public void aCat(Integer age) {
		cat = new Cat(age);
	}


	@Given("a Cat of age &age and name &name")
	public void aCatWithName(Integer age, String name) {
		cat = new Cat(age, name);
	}


	@Then("the house condition is &condition")
	public void theHouseCondition(String condition) {
		Assert.assertEquals(condition, cat.houseCondition());
	}
}