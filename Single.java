/**
 * This class is a subclass of the Hand class, and is used to model a hand of Single. 
 * It overrides getTopCard,isValid and getType method that it inherits from Hand class.
 * 
 * @author Goli Smaran
 *
 */
public class Single extends Hand {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for the Single type hand. Calls the constructor of Hand superclass.
	 * 
	 * @param player Player who plays the hand
	 * @param cards  List of cards played by the player
	 */	
	public Single(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * Returns the top card of the hand.
	 * 
	 * @return Card returns top card of the hand.
	 */
	@Override
	public Card getTopCard() {
		return getCard(0);
	}

	/**
	 * Checks whether the hand is a Single.
	 * 
	 * @return boolean true if the hand is a Single else false.
	 */
	@Override
	public boolean isValid() {
		return ((size() == 1) ? true : false);
	}

	/**
	 * Returns type of string.
	 * 
	 * @return String returns the type of the hand.
	 */
	@Override
	public String getType() {
		return new String("Single");
	}

	
}
