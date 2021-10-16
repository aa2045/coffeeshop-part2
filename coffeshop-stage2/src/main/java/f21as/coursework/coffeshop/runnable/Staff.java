package f21as.coursework.coffeshop.runnable;

import f21as.coursework.coffeshop.core.CoffeShopPresenter;

public abstract class Staff implements Runnable{

	protected CoffeShopPresenter presenter;
	protected double timeFactor = 0.2;
	protected String staffID;
	protected boolean active = true;
	
	public Staff(String staffID, CoffeShopPresenter presenter, double time)
	{
		this.staffID = staffID;
		this.presenter = presenter;
		this.timeFactor=time;
	}
	
	public void disable()
	{
		this.active = false;
	}
	
	public void enable()
	{
		this.active = true;
	}
	
	public boolean isActive()
	{
		return this.active;
	}
	
	public void changeSpeed(double time)
	{
		this.timeFactor = time;
	}
	
	
}
