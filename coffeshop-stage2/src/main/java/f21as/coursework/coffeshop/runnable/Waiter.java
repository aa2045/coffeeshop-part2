package f21as.coursework.coffeshop.runnable;

import java.util.ArrayList;


import f21as.coursework.coffeshop.core.CoffeShopPresenter;
import f21as.coursework.coffeshop.data.Item;
import f21as.coursework.coffeshop.data.Order;
import f21as.coursework.coffeshop.exceptions.CustomerNotFoundException;
import f21as.coursework.coffeshop.exceptions.OrderNotFoundException;

public class Waiter extends Staff{

	public Waiter(String staffID, CoffeShopPresenter presenter, double time) {
		super(staffID, presenter, time);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		//every x customers, takes an online order
		int priority=2;
		int counter = 0;
		// the thread is active
		while(this.active)
		{
			
			try 
			{
				boolean online = false;
				if(counter>=priority)
					online=true;
					
				
				//this exception need to be thrown somehow
				//thread process the order and returns the customerID for that order
				String customer = this.presenter.processNextOrders(staffID, online);
				System.out.println("Processing customer "+customer+" counter:"+counter);
				counter = counter+1;
				//arraylist of orders for that customerID
				ArrayList<Order> orders = this.presenter.getordertList().getCustomerOrdersArrays(customer);
				// if the picked up order is online reset the counter
				if(this.presenter.getordertList().isOnline(customer))
					counter = 0;
				ArrayList<Order> localCopy = new ArrayList<Order>();
				Item item=null;
				for(int i =0; i<orders.size(); i++)
				{	//get a particular order
					Order order = orders.get(i);
					//fetch the item details for that order
					item = presenter.getMenu().getItem(order.getItemID());
					//get the staffID
					String staffID = item.getStaffID().trim();
				//order is assigned to chef
					if(staffID.equals("C"))
					{
						
						System.out.println("Order "+order.getOrderID()+" is assigned to the Chef!!");
						//adds the order to the chef
						presenter.addChefOrder(order, customer);
						System.out.println("Order "+order.getOrderID()+" has been assigned to the Chef!!");
					}
					// order is assigned to barista
					else if(staffID.equals("B"))
					{
						System.out.println("Order "+order.getOrderID()+" is assigned to the Barista!!");
						presenter.addBaristaOrder(order, customer);
						System.out.println("Order "+order.getOrderID()+" has been assigned to the Barista!!");
					}
					
					else
					localCopy.add(orders.get(i));
				}
				
				for(int i =0; i<localCopy.size(); i++)
				{	//get the orderID
					Order order = localCopy.get(i);
					
					//get prep time for the item
						int time = item.getTime();
						
						double newTime = time * this.timeFactor;
						
						try 
						{	//processing time
							long waiting = (long) (newTime * 60000);
							System.out.println(this.staffID+" Processing Order "+order.getOrderID()+"  time: "+waiting);
							//	processes the order
							presenter.processOrder(order);
							// once the order is processed, the thread is put into sleep
			                Thread.sleep((waiting));
			                
			                
			            } catch (InterruptedException | OrderNotFoundException e) {
			                //when there is an exception, the current running thread is interrupted 
			            	Thread.currentThread().interrupt(); 
			                 
			            }
						
						try 
						{	//once the order is processed, it is removed
							System.out.println(this.staffID+" Removing Order "+order.getOrderID());
							
							presenter.removeOrder(customer, order.getOrderID());
							
							
						} catch (OrderNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
				
				}
			
			} catch (CustomerNotFoundException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.out.println("There are no more customers in the queue!!!!");
				
				try 
				{//the list of orders is synchronised
					synchronized (this.presenter.getordertList()) {
						//the thread waits here until it is notified
						this.presenter.getordertList().wait();
					}
					
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		this.active=true;
	}

	
	
	
}
