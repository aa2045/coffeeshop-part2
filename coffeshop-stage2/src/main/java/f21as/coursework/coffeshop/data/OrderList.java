package f21as.coursework.coffeshop.data;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import f21as.coursework.coffeshop.exceptions.CustomerNotFoundException;
import f21as.coursework.coffeshop.exceptions.OrderFileNotFoundException;
import f21as.coursework.coffeshop.exceptions.OrderNotFoundException;
import f21as.coursework.coffeshop.interfaces.Observer;
import f21as.coursework.coffeshop.interfaces.Subject;

public class OrderList implements Subject{
	
	
	//the customerIDs and orders are stored in a linkedhashmap
	private LinkedHashMap<String, Orders> list;
	private List<Observer> registeredObservers = new LinkedList<Observer>();


	
	// a nested class called Orders
	public class Orders{
		
		boolean attended = false;
		boolean billGenerated = false;
		ArrayList<Order> orders;
		boolean online = false;
		
		// setters 
		void setOrders(ArrayList<Order> orders)
		{
			this.orders = orders;
		}
		//setter
		void setOnline()
		{
			this.online=true;
		}
		//setter
		boolean isOnline()
		{
			return this.online;
		}
		//setter
		ArrayList<Order> getOrders()
		{
			return this.orders;
		}
		// adds the order to arraylist
		void addOrder(Order order)
		{
			this.orders.add(order);
		}
		// returns a boolean value depending on the order is attended or not
		boolean isAttended()
		{
			return this.attended;
		}
		
		//returns true if the bill is generated
		boolean isBillGenerated()
		{
			return this.billGenerated;
		}
		
		// sets the billGenerated variable to true
		void billGenerated()
		{
			this.billGenerated=true;
		}
		//assigns the staffID to the order
		void assignStaff(String staffID)
		{
			this.attended = true;
			for(int i=0; i<orders.size(); i++)
			{
				Order order = orders.get(i);
				order.setAssignment(staffID);
			}
		}
		// assigns the order to the staff thats passed as arguments
		void assignStaffToOrder(String staffID, String orderID)
		{
			this.attended = true;
			for(int i=0; i<orders.size(); i++)
			{
				Order order = orders.get(i);
				if(order.getOrderID().equals(orderID))
				{
					order.setAssignment(staffID);
				}
					
			}
		}
		
		//returns an arraylist of processed orders by iterating through the orders
		ArrayList<Order> getProcessingOrders()
		{	
			ArrayList<Order> output = new ArrayList<Order>();
			//iterating through the orders 
			Iterator<Order> orders = this.orders.iterator();
			while(orders.hasNext())
			{
				Order order = orders.next();
				//if the order is processed then it is added to the arraylist
				if (order.isProcessing())
					output.add(order);
				
				
			}
			//arraylist is returned
			return output;
		}
		
	}
	
	
	
	
	
	
	/**
	 * 
	 * @param customer
	 * @throws CustomerNotFoundException 
	 */
	//returns an arraylist when a customerID is passed as argument
	public ArrayList<Order> getCustomerOrdersArrays(String customer) throws CustomerNotFoundException  {
		
		if(this.list.containsKey(customer))
		{
			Orders orders = this.list.get(customer);	 
			return orders.getOrders();
		}
		
				
		else throw new CustomerNotFoundException("There are no orders for "+customer);

	}
	//returns the online orders present in the linkedhashmap when a customerID is passed
	public boolean isOnline(String customer) throws CustomerNotFoundException
	{
		if(this.list.containsKey(customer))
		{
			Orders orders = this.list.get(customer);	 
			return orders.isOnline();
		}
				
		else throw new CustomerNotFoundException("There are no orders for "+customer);
	}
	
// returns a boolean if the orders for that customerID is processed
	public boolean isAttended(String customer) throws CustomerNotFoundException
	{
		if(this.list.containsKey(customer))
		{
			Orders orders = this.list.get(customer);	 
			return orders.isAttended();
		}
				
		else throw new CustomerNotFoundException("There are no orders for "+customer);
	}

	
	//generates the bill for the customerID passed
	public void setFinalizedBill(String customer) throws CustomerNotFoundException
	{
		if(this.list.containsKey(customer))
		{
			Orders orders = this.list.get(customer);	 
			orders.billGenerated();
		}
		
				
		else throw new CustomerNotFoundException("There are no orders for "+customer);
	}
	
	
	/**
	 * 
	 * @param customer
	 * @throws CustomerNotFoundException 
	 */
	
	//returns the customer order when customerID and orderID are passed as argument 
	public Order getCustomerOrder(String customer, String orderID) throws CustomerNotFoundException {
		
		
		if(this.list.containsKey(customer))
		{
			Orders aux = this.list.get(customer);
			
			Iterator<Order> orders = aux.getOrders().iterator();
			while(orders.hasNext())
			{
				Order order = orders.next();
				if(order.getOrderID().equals(orderID))
					return order;
			}
				
			
		}
			
		
		//if you get here meaning that the order specified does not exists		
		throw new CustomerNotFoundException("There are no orders for "+customer);
		
	}
	
	//constructor creates an empty linkedhashmap
	
	public OrderList() throws OrderFileNotFoundException {
		try 
		{
			
			this.list = new LinkedHashMap<String, Orders>();
			
		}
		catch (Exception e) {
			throw new OrderFileNotFoundException("The orders file was not found");
		}
		
		
	}


	//this method adds an order to the list of orders 
	//cutomerID and the order is passed as argument
	public void addOrder(String customer, Order order, boolean online) 
	{
		//ArrayList to store the orders
		ArrayList<Order> orders;
		Orders aux;
		//if the orderlist already contains a customer, then the new order is added into the orderList with the same customerID
		if(this.list.containsKey(customer))
		{
			aux = this.list.get(customer);
			orders = aux.getOrders();
			orders.add(order);
			
		}
		//if not, then a new entry is made into the ArrayList
		else
		{
			
			aux = new Orders();
			orders = new ArrayList<Order>();
			orders.add(order);
			aux.setOrders(orders);
			if(online)
				aux.setOnline();
		}
		//Order is added to the linked hashmap and observer is notified
		synchronized(this)
		{
			this.list.put(customer, aux);
			this.notifyObservers();
		}
		
		
	}

	/**
	 * 
	 * @param customer
	 * @param order
	 * @throws OrderNotFoundException 
	 */
	// removes an order from the list of existing orders
	//takes customer ID and the order to be removed as argument
//	public void removerOrderOLD(String customer, String order) throws OrderNotFoundException {
//		
//		boolean out = false;
//		if(this.list.containsKey(customer))
//		{
//			Orders ords = this.list.get(customer);
//			
//			ArrayList<Order> orders = ords.getOrders(); 
//			
//			Iterator<Order> iter = orders.iterator();
//			while((iter.hasNext() && !out))
//			{
//				Order aux = iter.next();
//				if(aux.getOrderID().equals(order))
//				{
//					orders.remove(aux);
//					out = true;
//				}
//					
//			}
//			
//		}
//		
//		if(!out) 
//		{
//				//if the order to be removed is not found then this exception is thrown
//				throw new OrderNotFoundException(order);		
//		}	
//		this.notifyObservers();
//	}

	// this method removes the order for a customer once it is processed
	public void removerOrder(String customer, String order) throws OrderNotFoundException {
		
	
		if(this.list.containsKey(customer))
		{ //fetch all the orders for the passed customerID
			Orders ords = this.list.get(customer);
			
			ArrayList<Order> orders = ords.getOrders(); 
			// iterating through the orders
			Iterator<Order> iter = orders.iterator();
			while((iter.hasNext()))
			{
				Order aux = iter.next();
				if(aux.getOrderID().equals(order))
				{	//the thread is syncronised and observer is notified to remove the orders
					synchronized(this)
					{ //remove the order
						orders.remove(aux);
						//update the observer
						this.notifyObservers();
						return;
					}
					
				}
					
			}
			
		}
		
		
		
	}
	
	/**
	 * 
	 * @param customer
	 * @throws CustomerNotFoundException 
	 */
	
	//removes the customer from the list of customers after the order is served
	public void removeCustomer(String customer) throws CustomerNotFoundException {
		
		boolean out = false;
		if(this.list.containsKey(customer))
		{
			this.list.remove(customer);
		}
		
		else throw new CustomerNotFoundException(customer); 
		
			
	}
	//gets the LinkedHashMap contaning all the existing orders
	public LinkedHashMap<String, Orders> getList() {
		
		return this.list;
	}
	
	public String toString()
	{
		String output = "";
		Iterator iterC = this.list.keySet().iterator();
		while(iterC.hasNext())
		{
			String customer = (String) iterC.next();
			Orders aux = this.list.get(customer);
			ArrayList<Order> orders = aux.getOrders();
			
			Iterator<Order> iterO =  orders.iterator();
			while(iterO.hasNext())
			{
				Order order = iterO.next();
				String st = "Customer: "+customer;
				output=output+"\n"+st+"\n"+order.toString();
			}
		}
		
		return output;
	}
	
	//this method converts the order details to a string array to be stored in the 
	//final generated report
	
	public String toCSV()
	{
		String output = "";
		Iterator iterC = this.list.keySet().iterator();
		String[] aux = {"Timestamp","CustomerID","OrderID","ItemID","Discount", "Online"};
		output=this.convertToCSV(aux);
		while(iterC.hasNext())
		{
			String customer = (String) iterC.next();
			Orders ords = this.list.get(customer);
			ArrayList<Order> orders = ords.getOrders();
			
			Iterator<Order> iterO =  orders.iterator();
			while(iterO.hasNext())
			{
				Order order = iterO.next();
				String time = Long.toString(order.getCreateDate().getTime());
				String orderID = order.getOrderID();
				String itemID = order.getItemID();
				int discount = order.getDiscount();
				boolean online = order.isOnline();
				String ol="no";
				if(online) ol="yes";
				aux= new String[]{time, customer, orderID, itemID, String.valueOf(discount), ol};
				
				
				output=output+"\n"+this.convertToCSV(aux);
			}
		}
		
		return output;
	}

//	https://www.baeldung.com/java-csv
	//method to handle special characters while writing into csv file
	private String escapeSpecialCharacters(String data) {
	    String escapedData = data.replaceAll("\\R", " ");
	    if (data.contains(",") || data.contains("\"") || data.contains("'")) {
	        data = data.replace("\"", "\"\"");
	        escapedData = "\"" + data + "\"";
	    }
	    return escapedData;
	}
	//Stream is used to join various elements to a single string object
	private  String convertToCSV(String[] data) {
	    return Stream.of(data).map(this::escapeSpecialCharacters).collect(Collectors.joining(","));
	}
	

	//checks if the list of orders is empty or not
	//returns a boolean value
	public boolean isEmpty()
	{
		return this.list.isEmpty();
	}
	
	
	//for stage-2 
	//
	public int getWaiting_Orders_Num() throws CustomerNotFoundException
	{
		int count=0;
		// iterating through the linkedhashmap of the orders
		Iterator<String> iter = this.list.keySet().iterator();
		while(iter.hasNext())
		{
			String customerID = iter.next();
			//fetch the orders for the given customerID and store it an arraylist
			ArrayList<Order> orders = this.getCustomerOrdersArrays(customerID);
			//count the number of orders
			count = count + orders.size();
		}
		
		return count;
	}

	// method returns the customerID to be allocated to the StaffID
	public String getFirstCustomer2BeServed()
	{
		String customer = null;
		// if the queue of orders is not empty
		if(!this.isEmpty())
		{	//iterating through the linkedhashmap of orders
			Iterator<String> iter  = this.list.keySet().iterator();
			
			while(iter.hasNext())
			{	
				customer = iter.next();
				//fetch the orders for that customer
				Orders orders = this.getList().get(customer);
				//if the order has not been allocated to a staffID and the bill is generated then the customer is returned
				if((!orders.attended)&&(orders.billGenerated))
					return customer;
				customer = null;
			}
			
		}
			
		return customer;
	}

	//method returns the online customer to be served
	public String getFirstOnLineCustomer2BeServed()
	{
		String customer = null;
		if(!this.isEmpty())
		{
			Iterator<String> iter  = this.list.keySet().iterator();
			
			while(iter.hasNext())
			{
				customer = iter.next();
				Orders orders = this.getList().get(customer);
				//if the order is not allocated and bill is generated for the customerID and the order is online
				if((!orders.attended)&&(orders.billGenerated)&&(orders.isOnline()))
				{
					System.out.println("ONLine ORDER: "+customer);
					// return that customerID
					return customer;
				}
				//else return null
				customer = null;
			}
			
		}			
		return customer;
	}

	//this method assigns the order to the staff and notifies the observer
	public void assignOrder2Staff(String customer, String staffID )
	{
		Orders orders = this.list.get(customer);
		//thread is synchronised
		synchronized(this)
		{	//order is assgined to staff
			orders.assignStaff(staffID);
			//observer is automatically updated
			this.notifyObservers();
		}
		
	}
	// assigns the customer's orders to the staff and notifies the observer
	public void assignOrder2Staff(String customer, String orderID, String staffID )
	{
		Orders orders = this.list.get(customer);
		synchronized(this)
		{	// each order is allocated to that passed staffID
			orders.assignStaffToOrder(staffID, orderID);;
			//observer is updated
			this.notifyObservers();
		}
		
	}
	// returns an arraylist of the orders that are being processed for the given customerID
	public ArrayList<Order> getAttendedOrders(String customer) {
		// TODO Auto-generated method stub
		
		
		Orders orders = this.list.get(customer);
		
		
		return orders.getProcessingOrders();
	}
	
	
	
	// methods to register, remove and notify observers
	public void registerObserver(Observer obs) {
		registeredObservers.add(obs);
	}
//observer is removed
	public void removeObserver(Observer obs) {
		registeredObservers.remove(obs);
	}
// for every observer in the linkedlist of registeredObservers, the observer is updated 
	public void notifyObservers() {
		for (Observer obs : registeredObservers)
			obs.update();
	}

		
}