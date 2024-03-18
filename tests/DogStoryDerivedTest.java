package tests;

import solution.Given;
import solution.When;

public class DogStoryDerivedTest extends DogStoryTest {
	@When("the house is cleaned, and the number of hours is &hours")
	private void theHouseCleaned(Integer hours) {
		dog.hoursCleaningFloors(hours);
	}
	
	public class InnerClass extends DogStoryTest {
		@Given("a Dog that his age is &age")
		public void aDog(Integer age) {
			dog = new Dog(age);
		}
	}
}
