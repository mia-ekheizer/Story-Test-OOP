package tests;

public class Dog {
	private int age,hoursCleaningFloors;
	private boolean peeOnFloor;
	
	public Dog(int age) {
		this.age = age;
		this.hoursCleaningFloors=0;
		this.peeOnFloor = false;
	}
	
	public void notTakenForAWalk(int hours) {
		if(hours>8&& this.age > 5){
			this.peeOnFloor=true;
			hoursCleaningFloors=0;
		}
	}
	
	public void hoursCleaningFloors(int hours) {
		if(this.peeOnFloor){
			this.hoursCleaningFloors=this.hoursCleaningFloors+hours;
		}
		if(this.hoursCleaningFloors>10){
			this.peeOnFloor=false;
			this.hoursCleaningFloors=0;
		}
	}
	
	public String houseCondition() {
		if(this.peeOnFloor){
			return "smelly";
		}
		return "clean";
	}
}
