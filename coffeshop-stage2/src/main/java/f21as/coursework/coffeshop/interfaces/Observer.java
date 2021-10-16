package f21as.coursework.coffeshop.interfaces;

/**
 * The Observer part of the Observer pattern.
 * All classes implementing this interface MUST have this method.
 */
public interface Observer {
	
	/**
	 * Tell Observer to update itself
	 */
	public void update();
}