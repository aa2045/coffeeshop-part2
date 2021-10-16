package f21as.coursework.coffeshop.data;


public class Internet extends Item {

	public int getExpectedTime() {
		// TODO - implement Internet.getExpectedTime
		throw new UnsupportedOperationException();
	}

	
	protected Internet(String name,  String des, double cost, int time, String staffID)
	{
		super(Category.OTHER, name, des, cost, time, staffID);
	}
	
	/**
	 * 
	 * @param name
	 * @param des
	 * @param cost
	 */
	// method to instantiate Internet class and returns Internet objects
	public static Internet instanciateInternetItem(String name,  String des, double cost, int time, String staffID) {
		// TODO - implement Internet.instanciateInternet
		Internet internet = new Internet(name, des, cost, time, staffID);
		return internet;
	}

}