package f21as.coursework.coffeshop.data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

public class Order {

	//private data members
	private Date createDate;
	private String orderID;
	private String itemID;
	private int discount = 0;
	private String assignedTo;
	private boolean online = false;
	private boolean processing = false;
	
	//constructor which takes OrderID and ItemID as parameters
	public Order(String orderID, String itemID) {
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		this.createDate  = new Date(timestamp.getTime());
		this.itemID = itemID;
		this.orderID = orderID;
		this.assignedTo=null;

	}

	//constructor which takes Date, OrderID and itemID as parameters
	public Order(Date date, String orderID, String itemID) {
		
		this.createDate = date;
		this.itemID = itemID;
		this.orderID = orderID;
		this.assignedTo = null;
		
	}
	
	//constructor which takes timestamp, orderID and itemID as paramters
	public Order(String timestamp, String orderID, String itemID, boolean online) {
		
		this.createDate = new Date(Long.parseLong(timestamp));
		this.itemID = itemID;
		this.orderID = orderID;
		this.assignedTo = null;
		this.online = online;
	}
	
	
	public void setOnline()
	{
		this.online=true;
	}
	
	public void setAssignment(String staffID)
	{
		this.assignedTo = staffID;
	}
	
	public String getAssignment()
	{
		return this.assignedTo;
	}
	
	public String getItemID() {
		return this.itemID;
	}

	public Date getCreateDate() {	
		return this.createDate;
	}

	public String getOrderID() {	
		return this.orderID;
	}

	//sets the discount value 
	public void setDiscount(int value) {
		this.discount = value;
	}

	//returns a discount value 
	public int getDiscount() {
		return this.discount;
	}

	//returns if is online 
	public boolean isOnline() {
			return this.online;
		}
	
	//This method is called in the Orderlist class
	//used to print the order details of the customer 	
	public String toString()
	{
		String date = "Date: "+this.createDate;
		String order = "Order: "+this.orderID;
		String item = "Item: "+this.itemID;
		String discount = "Discount: "+this.discount;
		
		return date+"\n"+order+"\n"+item+"\n"+discount+"\n";
	}
	
	
	public void setProcessing()
	{
		this.processing = true;
	}
	
	// returns true if the order is processed
	public boolean isProcessing()
	{
		return this.processing;
	}
}