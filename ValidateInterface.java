/**
 * This interface is used by different hand classes.
 * 
 * @author Goli Smaran
 *
 */
public interface ValidateInterface {
	/**
	 * Checks if it is a valid hand or not
	 * 
	 * @return boolean true if the hand is valid or else false.
	 */
	public abstract boolean isValid();
	
	/**
	 * Specifies type of hand
	 * 
	 * @return String returns the type of hand.
	 */
	public abstract String getType();
}
