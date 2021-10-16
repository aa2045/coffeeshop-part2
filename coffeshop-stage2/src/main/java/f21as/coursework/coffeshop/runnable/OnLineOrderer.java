package f21as.coursework.coffeshop.runnable;

import java.util.Random;
import java.util.Vector;

import f21as.coursework.coffeshop.core.CoffeShopPresenter;
import f21as.coursework.coffeshop.core.Utils;
import f21as.coursework.coffeshop.data.Category;

public class OnLineOrderer implements Runnable{

	private CoffeShopPresenter presenter;
	
	
	public OnLineOrderer(CoffeShopPresenter presenter)
	{
		this.presenter = presenter;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int time = 1;
		
		while(true)
		{
			
			try 
			{
				//generating a random time for online orders to appear
				Random rand = new Random();

				time = rand.nextInt(5) + 1;
				long waiting = (long) (time * 30000);
				System.out.println("Next Online order in "+(time * 30)+" secs.");
				Thread.sleep((waiting));
				// initialize vector datastructure to store the food and beverage category
				Vector<String> beverages = presenter.getMenu().getItemsPerCategory(Category.BEVERAGE);
				Vector<String> food = presenter.getMenu().getItemsPerCategory(Category.FOOD);
				//generating a random customerID starting with "OL" to indicate an Online Order
				String customerId = "OL"+Utils.randomCodeGenerator(5);
				
				// get the size of the array beverages present in the Menu
				int max = beverages.size();
				int min = 0;
				
				int quantity = rand.nextInt(2) + 1;
				
				int code;
				String itemID;
				
				for(int i=0; i<quantity; i++)
				{
					 
					code = rand.nextInt(max);
					//gets the itemID from the beverages data structure
					itemID = beverages.get(code);
					// adds the generated customer ID and the itemID as a new online order
					presenter.addNewOnLineOrder(customerId, itemID);
				}
					
				 
				max = food.size();
				quantity = rand.nextInt(2) + 1;
				
				for(int i=0; i<quantity; i++)
				{
					code = rand.nextInt(max);
					//gets the itemID from the food data structure
					itemID = food.get(code);
					// adds the generated customer ID and the itemID as a new online order
					presenter.addNewOnLineOrder(customerId, itemID);
				}
				
				// gets the total bill for the customer
				presenter.getCustomerBill(customerId);
				
			} catch (InterruptedException e) {
				// stops the current thread and throws an exception
				Thread.currentThread().interrupt(); 

			}

			
			
		}
		
		
		
	}
	
	

}
