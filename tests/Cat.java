package tests;


public class Cat implements Cloneable {
	private int age, hoursCleaningFloors;
	private String name;
	private boolean peeOnFloor;
	private int kaki_size;

	public Cat(int age) {
		this.age = age;
		this.hoursCleaningFloors = 0;
		this.peeOnFloor = false;
		//size is 0,1,2
		name = "";
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		Cat c = new Cat(0);
		c.age = this.age;
		c.hoursCleaningFloors = this.hoursCleaningFloors;
		c.name = this.name;
		c.peeOnFloor = this.peeOnFloor;
		c.kaki_size = this.kaki_size;
		return c;
	}

	public void Kaki(int size) {
		this.kaki_size = size;
	}

	public int GetKaki() {
		return this.kaki_size;
	}

	public Cat(int age, String name) {
		this.age = age;
		this.hoursCleaningFloors = 0;
		this.peeOnFloor = false;
		this.name = name;
	}


	public void notTakenForAWalk(int hours) {
		if (hours > 8 && this.age > 5) {
			this.peeOnFloor = true;
			hoursCleaningFloors = 0;
		}
	}


	public String houseCondition() {
		if (this.peeOnFloor) {
			return "smelly";
		}
		return "clean";
	}
}
