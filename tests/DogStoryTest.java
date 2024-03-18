package tests;

import org.junit.Assert;

import solution.Given;
import solution.Then;
import solution.When;

public class DogStoryTest {
	protected Dog dog;
	@Given("a Dog of age &age")
	public void aDog(Integer age) {
		dog = new Dog(age);
	}
	
	@When("the dog is not taken out for a walk, and the number of hours is &hours")
	public void dogNotTakenForAWalk(Integer hours) {
		dog.notTakenForAWalk(hours);
	}
	
	@Then("the house condition is &condition")
	public void theHouseCondition(String condition) {
		Assert.assertEquals(condition, dog.houseCondition());
	}
}
