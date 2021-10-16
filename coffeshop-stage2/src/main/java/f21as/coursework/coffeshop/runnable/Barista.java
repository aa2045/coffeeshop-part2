package f21as.coursework.coffeshop.runnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import f21as.coursework.coffeshop.core.CoffeShopPresenter;
import f21as.coursework.coffeshop.data.Order;
import f21as.coursework.coffeshop.exceptions.CustomerNotFoundException;
import f21as.coursework.coffeshop.exceptions.OrderNotFoundException;

public class Barista extends Staff{

	
	public Barista(String staffID, CoffeShopPresenter presenter, double time) {
		super(staffID, presenter, time);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		this.active=true;
		// thread is active
		while(this.active)
		{
			// gets the next order assigned to the barista
				Order order = presenter.getNextBaristaOrder();
				if(order!=null)
				{
					// gets the time required for each order
					int time = presenter.getMenu().getItem(order.getItemID()).getTime();

					double newTime = time * this.timeFactor;
					System.out.println("time: " +time);
					System.out.println("newTime: " +newTime);
					try 
					{
						// display the waiting time in the console
						long waiting = (long) (newTime * 60000);
						// displays the staffID processing the OrderID and its processing time
						System.out.println(this.staffID+" Processing Order "+order.getOrderID()+"  time: "+waiting);
						presenter.processOrder(order);
						// the thread sleeps until the order is being prepared 
						Thread.sleep((waiting));


					} catch (InterruptedException | OrderNotFoundException e) {
						// thread is stopped and an exception is returned
						Thread.currentThread().interrupt(); 

					}

					try 
					{
						System.out.println(this.staffID+" Removing Order "+order.getOrderID());
						// after the order is processed, its removed from the list of orders
						this.presenter.removeBaristaOrder(order);

					} catch (OrderNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}


					
				}
				
				
				else
				{
					try 
					{
						synchronized (this.presenter.getBaristaOrders()) 
						{
							// once the barista has processed the order. it waits for orders which have 
							//been assigned to it to start processing
							System.out.println("Barista is waiting!!!");
							this.presenter.getBaristaOrders().wait();
							
							
						}
						
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				
				
			
			
		}
	}

	
	
	
}
