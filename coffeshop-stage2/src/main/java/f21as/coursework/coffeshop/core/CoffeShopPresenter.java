package f21as.coursework.coffeshop.core;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Vector;
import java.util.Scanner;

import f21as.coursework.coffeshop.data.Category;
import f21as.coursework.coffeshop.data.Discount;
import f21as.coursework.coffeshop.data.DiscountList;
import f21as.coursework.coffeshop.data.Item;
import f21as.coursework.coffeshop.data.Menu;
import f21as.coursework.coffeshop.data.Order;
import f21as.coursework.coffeshop.data.OrderList;
import f21as.coursework.coffeshop.data.OrderList.Orders;
import f21as.coursework.coffeshop.exceptions.CustomerNotFoundException;
import f21as.coursework.coffeshop.exceptions.DiscountFileNotFoundException;
import f21as.coursework.coffeshop.exceptions.FailedInitializationException;
import f21as.coursework.coffeshop.exceptions.FrequencyException;
import f21as.coursework.coffeshop.exceptions.MenuFileNotFoundException;
import f21as.coursework.coffeshop.exceptions.OrderFileNotFoundException;
import f21as.coursework.coffeshop.exceptions.OrderNotFoundException;
import f21as.coursework.coffeshop.exceptions.OrdersToFileException;
import f21as.coursework.coffeshop.interfaces.Observer;
import f21as.coursework.coffeshop.interfaces.Subject;

public class CoffeShopPresenter {
	// private class components and attributes
	
	public static String server1Name = "staff1";
	public static String server2Name = "staff2";
	public static String server3Name = "staff3";
	public static String server4Name = "staff4";
	
	public static String chefName = "the Chef";
	public static String baristaName = "the Barista";
	
	private OrderList myordertList;
	private Menu myMenu;
	private DiscountList mydiscouts;
	private String report="";
	private String discountPolicy = MAX_DISCOUNT;
	private double totalIncome=0;
	
	private LinkedHashMap<Order, String> chefOrders = new LinkedHashMap<Order, String>();
	private LinkedHashMap<Order, String> baristaOrders = new LinkedHashMap<Order, String>();
	
	private static String ORDERS_FILE="files/orderfile.csv";
	private static String MENU_FILE="files/menu.csv";
	private static String DISCOUNTS_FILE="files/discount.csv";
	private static String MAX_DISCOUNT="Max_discount";
	private static String MIN_DISCOUNT="Min_discount";
	
	//private Constructor, never directly called
	private CoffeShopPresenter(OrderList myorders, Menu mymenu, DiscountList mydiscount) throws CustomerNotFoundException
	{
		this.mydiscouts = mydiscount;
		this.myMenu = mymenu;
		this.myordertList = myorders;
		initializeNumbers();
		
	}
	
	//returns the linkedhashmap of OrderList object containing the orders
	public OrderList getordertList() {
		
		return this.myordertList;
	}

	// returns a Menu object containing the details of the menu stored in treemap
	public Menu getMenu() {
		
		return this.myMenu;
	}
	
	// returns DiscountList object containing all details of the discounts
	public DiscountList getMydiscouts() 
	{
		return this.mydiscouts;
	}

	

	/**
	 * 
	 * @param customer
	 */
//	public void removeCustomer(String customer) {
//
//	}
	
	
	
	//method to remove an order when customerID and OrderID is passed
	public void removeOrder(String customer, String order) throws OrderNotFoundException
	{
		this.myordertList.removerOrder(customer, order);
		System.out.println("Order "+order+" removed");
	}
	
	/**
	 * 
	 * @param order
	 * @param menu
	 * 
	 */
	//method updates the quantity of the item for the given customerId and orderId
	public void updateItemCounter(String customer, String orderID) {
		
		Order order;
		try 
		{
			order = this.myordertList.getCustomerOrder(customer, orderID);
			
			String itemID = order.getItemID();
			
			updateItemCounter(itemID);
			
			
		} catch (CustomerNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	//Calls the updateCounter() of Item class to update the frequency of the items ordered
	 public void updateItemCounter(String itemID) {
		
		Item item = this.myMenu.getItem(itemID);
		item.updateCounter();
		
	}
	
	 
// when initializing the engine, it reads all the existing orders from the file. The number of items and the discounts applied to the orders need to be recalculated
	private void initializeNumbers() throws CustomerNotFoundException
	{
		
		LinkedHashMap<String, OrderList.Orders> orders = this.myordertList.getList();
		Iterator<String> iter = orders.keySet().iterator();
		while(iter.hasNext())
		{
			String customerID = iter.next();
			try 
			{
				Iterator<Order> ordersL = this.myordertList.getCustomerOrdersArrays(customerID).iterator();
				while(ordersL.hasNext())
				{
					Order order = ordersL.next();
					Item item = this.myMenu.getItem(order.getItemID());
					item.updateCounter();
				}
				
			} catch (CustomerNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			getCustomerBill(customerID);
		}
	}
	 
	
	//static method returns an object for CoffeShopEngine class; this method is called in GUI
	public static CoffeShopPresenter istanciateEngine() throws FailedInitializationException, MenuFileNotFoundException, OrderFileNotFoundException, DiscountFileNotFoundException, CustomerNotFoundException {
		
		OrderList orders = null;
		Menu menu = null;
		DiscountList discounts = null;
		
		try 
		{	//assigining the OrderList object to orders
			orders = Utils.instanciateOrders(ORDERS_FILE);
			//assiging the Menu object to menu
			menu = Utils.instanciateMenu(MENU_FILE);
			//assiging the DiscountList object to discounts
			discounts = Utils.instanciateDiscounts(DISCOUNTS_FILE);
			
		} catch (FrequencyException e) {
			// TODO Auto-generated catch block
			//need to catch this exception and handle it
			e.printStackTrace();
			throw new FailedInitializationException(e.toString());
		}
		
		CoffeShopPresenter engine = new CoffeShopPresenter(orders, menu, discounts);
		return engine;
		
				
	}

	/**
	 * 
	 * @param customer
	 * @param item
	 */
	//instanciate a new Order object and add it to the list of existing orders, the method takes itemID and customerID to instanciate the order
	public void addNewOrder(String customer, String item) 
	{
		
		this.addOrder(customer, item, false);
		
	}

	
	public void addNewOnLineOrder(String customer, String item) 
	{
		
		this.addOrder(customer, item, true);
		
	}
	
	private void addOrder(String customer, String item, boolean online)
	{
		int count;
		try 
		{	//size of the arraylist is assigned to count
			count = this.myordertList.getCustomerOrdersArrays(customer).size();
		} 
		//catch the exception when customer is not found
		catch (CustomerNotFoundException e) 
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//this is not an error, it happens when the customer is ordering the first item
			count=0;
		}
		//creates an OrderID for the customer
		String num = String.format("%03d", count);
		String orderID= ""+customer+num;
		//adding the new order to the order object
		Order order = new Order(orderID, item);
		//if the order is online
		if(online) order.setOnline();
		
		System.out.println("Adding order "+orderID+" for customer "+customer);
		//online order is added to the orderlist and to the
		this.myordertList.addOrder(customer, order, online);
		this.updateItemCounter(item);

	}
	
	
	/**
	 * 
	 * @param code
	 */
	//get the item description of the passed item code
	public String getItemDescription(String code) 
	{	
		Item item = this.myMenu.getItem(code);
		return item.getDescription();
	}

	//the method stores existing orders queue into orderfile.csv file
	public void saveOrders() throws OrdersToFileException
	{	//converts the OrderList object to a single string 
		String content = this.myordertList.toCSV();
		
		try {
			FileManager.overWriteFile(ORDERS_FILE, content);
		} catch (FrequencyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//exception is thrown if it cant be written to file
			throw new OrdersToFileException(e);
		}
	
	}
	
	//returns a string with the items name and counter (sold items) per category, the method is used during the generation of the report.
	private String getSoldItems(Category category)
	{
		String output ="\n**************** "+ category +" *******************\n";
		Iterator<String> items = this.myMenu.getItemsPerCategory(category).iterator();
		while(items.hasNext())
		{
			String itemID = items.next();
			Item item = this.myMenu.getItem(itemID);
			if(item.getCounter()>0)
				output=output+item.getName()+"  ("+itemID+")  : "+item.getCounter()+"\n";
		}
		
		
		return output;
		
	}
	
	//method to parse the menu to string, this is called in generateReport()  
	private String menuToString()
	{
		String menu="";
		try {
			Scanner scanner = new Scanner(new File(MENU_FILE));
//			if(scanner.hasNext()==true)
//			{
//			   scanner.nextLine();
//			}
//			else
//			{
//			    System.out.println("Error: File is empty");
//			    return null;
//			}
			while (scanner.hasNextLine()) {
				//System.out.println(scanner.nextLine());
				menu+=scanner.nextLine() + "\n";
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return menu;
	}
		
		
	//generates the final report that contains details of the menu, customers orders and the counter of the items ordered
	public String generateReport()
	{
		String output="";
		String customerbill = "";
		for (Category category : Category.values()) { 
		     output=output+this.getSoldItems(category);
		}
		//iterates through the key of the linkedhashmap myorderList
		Iterator<String> customersIter = this.myordertList.getList().keySet().iterator();
		//menuread contains a string of all the menu items
		String menuread = menuToString();
		String stars = "***********************************"+"\n";
		String mennuu = "\t\tMENU" + "\n";
		String stars1 = "**********************************" + "\n";
		String starss = "**********************************"+"\n";
		String ord = "\t\tCustomer Orders" + "\n";
		String starss2 = "*********************************" + "\n";
				
		this.report = this.report + stars+ mennuu + stars1 + menuread + "\n\n" + starss + ord + starss2;
		
		//while there is a next customerID
		while(customersIter.hasNext())
		{
			String customerID = customersIter.next();
			try {
				//appending the Bill for the customerID to the report
				this.report=this.report+customerBilltoString(customerID, true);
				
				//catch the exception and print the error message when the customer is not found
			} catch (CustomerNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
		String ss = "**********************************"+"\n";
		String freq = "\t\tItem frequency" + "\n";
		String ss2 = "**********************************" + "\n";
		//report is returned as a string
		return this.report+"\n\n"+ss+freq+ss2 +output;
		
	}
	
	
	//the method takes customerID as a argument,
	//it return the Prolog code, the facts and the rules representing the menu, the discount rules and the orders for customerID
	public String toPrologString(String customerID)
	{
		String output="";
		//generate the Prolog rules representing the Menu
		output=output+this.myMenu.toPrologString()+"\n\n";
		//generate the Prolog rules representing the Discounts
		output=output+this.mydiscouts.toPrologString();
				
		OrderList ordersL = this.myordertList;
		
		ArrayList<Order> orders;
		try {
			// the arraylist of a customerID's order is stored in order
			orders = ordersL.getCustomerOrdersArrays(customerID);
			//exception is caught when the customer is not found
		} catch (CustomerNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//create an empty arraylist of type Order
			orders = new ArrayList<Order>();
		}
		
		String aux="";
		//iterating through the orders arraylist
		Iterator<Order> iterO = orders.iterator();
		while(iterO.hasNext())
		{	
			Order order = iterO.next();
			//find the item of the order
			Item item = this.myMenu.getItem(order.getItemID());
			//generate the Prolog fatc representing the specific order and append it to the rest
			aux=aux+"order("+order.getOrderID()+", "+item.getName().toLowerCase()+").\n";
		}
		
		//appending the string orderID and itemname to the output string
		output=aux+"\n\n"+output;
		//return the Prolog code as a String
		return output;
	}
	
	
	//method that round the value of a double 
	private double round(double d, int numbersAfterDecimalPoint) {
	    long n = (long) Math.pow(10, numbersAfterDecimalPoint);
	    double d2 = d * n;
	    long l = (long) d2;
	    return ((double) l) / n;
	}
	
	
	//The method returns the string representing the bill for the specified customer 
	//if updateBill==true then, the total income and is updated with this bill value. The value is set to 'true' when the method is called to generate the report. 
	//It is set to 'false' when the call is triggered by the GUI to show the bill for the customer once the orders are submitted. 
	private String customerBilltoString(String customer, boolean updateBill) throws CustomerNotFoundException
	{	
		
		double bill=0;
		String billString="\ncustomer "+customer+": \n";
		//arraylist of orders for the given customerID
		ArrayList<Order> orders = this.myordertList.getCustomerOrdersArrays(customer);
		//iterating through the orders
		Iterator<Order> i = orders.iterator();
		String assignTo = "";
		while(i.hasNext())
		{	
			Order order = i.next();
			assignTo = order.getAssignment();
			// get the item for that order and assigning it to Item object
			Item item = this.myMenu.getItem(order.getItemID());
			//appending itemID, itemName that was ordered 
			billString=billString+"Item: "+order.getItemID()+"  "+item.getName()+"\n";
			//appending cost of the item to bill string
			billString=billString+"Cost: "+item.getCost()+"AED" + "\n";
			
			//if the order has some discount
			if(order.getDiscount()>0)
			{	//calculate the discounted price for the item
				double discounted = item.getCost() * ((100 - order.getDiscount())*0.01); 
				discounted = round(discounted, 2);
				//append it to the bill
				billString=billString+"\nafter "+order.getDiscount()+"% discount: "+discounted+"AED";
				// true then add the discount to the totalIncome and add it to the bill for the customer
				if(updateBill) this.totalIncome=totalIncome+discounted;// totalIncome is for report
				bill=bill+discounted;
			}
			else {
				// true then add the itemcost to the totalIncome and add it to the bill for the customer
				if(updateBill) this.totalIncome=totalIncome+item.getCost();
				bill=bill+item.getCost();
			}
			billString=billString+"\n-------------------------------\n";	
			
		}
		billString=billString+"Total AED: "+bill + "\n";
		billString = billString+ "Assigned to: " + assignTo;
		///////////
		if(updateBill) billString=billString+"\n"+"Partial Income: "+this.totalIncome+"\n\n\n";
		
		//returning the billstring that contains the bill for the customer
		return billString;	
		
	}
	
	/**
	 * 
	 * @param customer
	 * @throws InterruptedException 
	 */
	//returns the final customer bill when a customerID is passed
	//the method instanciate an object from the class Discount Calculator which is used to query the Prolog engine and retrive te results
	public String getCustomerBill(String customer) 
	{
		
		ArrayList<String> goals = new ArrayList<String>();
		//calls the private method toPrologString() to generate the Prolog code
		String prologProgram = this.toPrologString(customer);
//		System.out.println("#############################################");
//		System.out.println(prologProgram);
//		System.out.println("#############################################");
		//iterates through the discount vector
		Iterator<Discount> iter = this.mydiscouts.getList().iterator();
		while(iter.hasNext())
		{	
			Discount discount = iter.next();
			//discount vector is converted to the Prolog rule 
			String toProlog = discount.toPrologString();
			// the toProlog rule is split because is only need the head of the rule whic corresponds to our goal
			String goal = toProlog.split(":-")[0].trim();
			goals.add(goal);
			
		}
		
		
		//instanciate the DiscountCalculator, the interface to the Prolog Engine
		DiscountCalculator calculator=null;
		try 
		{
			calculator = DiscountCalculator.instanciateDiscountCalculator(prologProgram, goals);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			// in case there is a problem in calculating the discount, no discount is applied!!! the bill calculation continues.
			e.printStackTrace();
			//FrequencyException is thrown if an error is encounterd
		} catch (FrequencyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(calculator!=null)
		{
			//the discountCalculator object queries about all the goals and return all the matrix of results. 
			//For each discount rule corresponds all the possible combination of orders matching the rule 
			HashMap<String, ArrayList<ArrayList<String>>> maps = calculator.getMatchingMatrix();
			
			Iterator<String> iterG = maps.keySet().iterator();
			String finalDiscountGoal=null;
			ArrayList<String> finaldiscountedOrders=null;
			double finaldiscountedBill=0;
			
			//it cycles through all the results and fins the most suitable solution according with the choosen policy (MAX or MIN Discount)
			while(iterG.hasNext())
			{
				String goal = iterG.next();
				ArrayList<ArrayList<String>> solutions = maps.get(goal);
				
				Iterator<ArrayList<String>> iterS = solutions.iterator();
				while(iterS.hasNext())
				{
					ArrayList<String> aux = iterS.next();
//					System.out.println("printing aux");
//					System.out.println(aux);
					double discount = calculateDiscount(customer, aux, goal);
					if(this.discountPolicy.equals(MAX_DISCOUNT))
					{
						if(discount>finaldiscountedBill)
						{
							finaldiscountedBill=discount;
							finaldiscountedOrders=aux;
							finalDiscountGoal=goal;
						}
							
					}
					else
						if(this.discountPolicy.equals(MIN_DISCOUNT))
						{
							if (finaldiscountedBill==0)
								finaldiscountedBill=(discount+0.001);
							if(discount<finaldiscountedBill)
							{
								finaldiscountedBill=discount;
								finaldiscountedOrders=aux;
								finalDiscountGoal=goal;
							}
								
						}
					
				}
			}	
				System.out.println("The final discounted orders are: "+finaldiscountedOrders);
				System.out.println("Saving: "+finaldiscountedBill);
				
				if(finaldiscountedOrders!=null)
				{
					Iterator<String> orders = finaldiscountedOrders.iterator();
					while(orders.hasNext())
					{
						String orderID = orders.next();
						try 
						{
							Order order = this.myordertList.getCustomerOrder(customer, orderID);
							int discount = this.getDiscountValue(finalDiscountGoal);
							order.setDiscount(discount);
							
							
						} catch (CustomerNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
				
				
				synchronized (this.myordertList) {
					try {
						this.myordertList.setFinalizedBill(customer);
						this.myordertList.notify();
					} catch (CustomerNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				synchronized(this.myordertList)
				{
					this.myordertList.notifyObservers();
				}
						
//			}
		}
		

		try {
			return customerBilltoString(customer, false);
		} catch (CustomerNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		
		
		
	}
	
	
	//return the % of the discount for a specific discount, the serach use the head of the Prolog rule. 
	
	private int getDiscountValue(String discountRule)
	{
		Iterator<Discount> iter = this.mydiscouts.getList().iterator();
		Discount discount=null;
		while(iter.hasNext())
		{
			Discount d = iter.next();
			String toProlog = d.toPrologString();
			String goal = toProlog.split(":-")[0].trim();
			if(goal.equals(discountRule))
			{
				discount = d;
				break;
			}
			
		}
		int value=0;
		if(discount!=null)
			//contains the discount to apply to the selected orders
			value = discount.getDiScount();
		return value;
	}

	
	//This method is called once the orders matching the discount rule are found, to finalized the actual value of the discount.
	//the ordersID matching the discount rule, the discount rule and the customer are the parameters.
	public double calculateDiscount(String customerID, ArrayList<String> orders, String discountRule)
	{
		System.out.print(orders);

		
		//return the % of the discount
		int value=this.getDiscountValue(discountRule);
		double total=0;
		Iterator<String> oIter = orders.iterator();
		while (oIter.hasNext())
		{
			String orderID = oIter.next();
			try 
			{
				Order order = this.myordertList.getCustomerOrder(customerID, orderID);
				double price = this.myMenu.getPriceItem(order.getItemID());
				total = total + price;
				
			} catch (CustomerNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// total contain the total cost for the selected items before discount
		System.out.println("-----------------------------------------------");
		System.out.println(orders);
		System.out.println(discountRule);
		System.out.println("Total before discount: "+total);
		double discounted = total * ((100 - value)*0.01);
		System.out.println("Total after discount: "+discounted);
		System.out.println("-----------------------------------------------");
		double savings = total - discounted;
		System.out.println("Saving value: "+savings);
		return savings;
	}
	
	//synchronised methods called by the Staff threads in the run() method and returns customerID
	synchronized public String processNextOrders(String staffID, boolean online) throws CustomerNotFoundException
	{
		String customer=null;
		if(online)
			customer = this.myordertList.getFirstOnLineCustomer2BeServed();
		
		if(customer==null)
			customer = this.myordertList.getFirstCustomer2BeServed();
			
		if(customer == null)
			throw new CustomerNotFoundException("");
		try 
		{
			
			ArrayList<Order> orders = this.myordertList.getCustomerOrdersArrays(customer);
			this.myordertList.assignOrder2Staff(customer, staffID);
			
			return customer;
		} catch (CustomerNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	


	public LinkedHashMap<Order, String> getChefOrders()
	{
		return this.chefOrders;
	}

	
	public LinkedHashMap<Order, String> getBaristaOrders()
	{
		return this.baristaOrders;
	}

	//method used to add orders to chef, barista
	private void addSpecialOrder(Order order, String customerID, LinkedHashMap<Order, String> queue, String staffID)
	{
		//add the order, customerID to the queue
		queue.put(order, customerID); 
		//synchronized the queue
		synchronized (queue)
		{
			//assigns the order to the given staffID
			this.myordertList.assignOrder2Staff(customerID, order.getOrderID(), staffID);
			//wakes up the single thread
			queue.notify();
			//the observer is notified
			this.myordertList.notifyObservers();
		}
		
		
	}
	// returns the next order in the queue
	private Order getNextSpecialOrder(LinkedHashMap<Order, String> queue)
	{
		//the linkedhashmap of orders is synchronised
		synchronized (queue)
		{
			if(!queue.isEmpty())
				//returns the next order
				return queue.keySet().iterator().next();
		}
		return null;
	}
	
	//remove order
	private void removeSpecialOrder(Order order, LinkedHashMap<Order, String> queue) throws OrderNotFoundException
	{	//get the customerID from an order
		String customer = queue.get(order);
		//remove that orderID for the customerID
		this.myordertList.removerOrder(customer, order.getOrderID());
		//synchronise the linkedhashmap
		synchronized (queue)
		{	// remove the order from the linkedhashmap
			queue.remove(order);
		}
		
	}
	
	//add the order to the chefOrders
	public void addChefOrder(Order order, String customerID)
	{
		this.addSpecialOrder(order, customerID, chefOrders, this.chefName);
	}
	//add the order to the Barista Orders
	public void addBaristaOrder(Order order, String customerID)
	{
		this.addSpecialOrder(order, customerID, baristaOrders, this.baristaName);
	}
	//get the next order for Chef
	public Order getNextChefOrder()
	{
		return this.getNextSpecialOrder(this.chefOrders);
	}
	//get the next order for barista
	public Order getNextBaristaOrder()
	{
		return this.getNextSpecialOrder(this.baristaOrders);
	}
	// removes the chef order
	public void removeChefOrder(Order order) throws OrderNotFoundException
	{
		this.removeSpecialOrder(order, this.chefOrders);
	}
	//barista is removed after
	public void removeBaristaOrder(Order order) throws OrderNotFoundException
	{
		this.removeSpecialOrder(order, this.baristaOrders);
	}
	//synchronised method to process the order passed in argument
	public void processOrder(Order order) throws OrderNotFoundException
	{
		
		synchronized (this.myordertList)
		{	//order processing is set true
			order.setProcessing();
			//observer is notified
			this.myordertList.notifyObservers();
		}
		
	}

	//method returns the arraylist of orders that is under process
	public ArrayList<Order> getOrdersUnderProcess() throws CustomerNotFoundException
	{	//arraylist of orders
		ArrayList<Order> output = new ArrayList<Order>();
		LinkedHashMap<String, Orders> aux = this.myordertList.getList();
		//iterating through the customerIDs/keys of the linkedhashmap
		Iterator<String> iter = aux.keySet().iterator();
		while(iter.hasNext())
		{
			String customer = iter.next();
			//if the customer is allocated a staffID
			if(this.myordertList.isAttended(customer))
			{	//fetching the arraylist of orders that are already attended
				ArrayList<Order> orders = this.myordertList.getAttendedOrders(customer);
				//iterating through those orders
				Iterator<Order> iter2 = orders.iterator();
				while(iter2.hasNext())
				{	//adding that order to the arraylist output
					output.add(iter2.next());
				}
				
					
			}
		}
		
		return output;
	}
	
	
	
	public void registerObserver(Observer observer)
	{
		this.myordertList.registerObserver(observer);
		
	}
	
}