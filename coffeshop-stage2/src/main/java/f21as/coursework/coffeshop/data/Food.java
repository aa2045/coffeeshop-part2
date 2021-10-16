package f21as.coursework.coffeshop.data;


public class Food extends Item {

	private boolean hot;

	
	public int getExpectedTime() {
		// TODO - implement Food.getExpectedTime
		throw new UnsupportedOperationException();
	}

	//inherited class calls the constructor of the parent Item class
	protected Food(String name, String des, double cost, int time, String staffID)
	{
		super(Category.FOOD, name,  des, cost, time, staffID);
	}
	
	/**
	 * 
	 * @param name
	 * @param desc
	 * @param cost
	 */
	// method to instantiate food class and returns food objects
	public static Food instanciateFoodItem(String name, String desc, double cost, int time, String staffID) {
		// TODO - implement Food.instanciateFood
		Food food = new Food(name, desc, cost, time, staffID);
		return food;
	}

}