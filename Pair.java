/**
 * This class is a subclass of the Hand class, and are used to model a hand of Pair. 
 * It overrides getTopCard,isValid and getType method that it inherits from Hand class.
 * 
 * @author Goli Smaran
 *
 */
public class Pair extends Hand {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for the Pair type hand. Calls the constructor of Hand super class.
	 * 
	 * @param player Player who plays the hand
	 * @param cards  List of card played by the player
	 */
	public Pair(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * Returns the top card of the hand.
	 * 
	 * @return Card returns the top card of the hand.
	 */
	@Override
	public Card getTopCard() {
		sort();
		return getCard(1);
	}
	
	/**
	 * Checks whether the hand is a Pair.
	 * 
	 * @return boolean true if the hand is a Pair or else false.
	 */
	@Override
	public boolean isValid() {
		return ((size() == 2) ? ((getCard(0).getRank() == getCard(1).getRank()) ? true : false) : false);
	}

	/**
	 * Returns type of string.
	 * 
	 * @return String returns the type of the hand.
	 */
	@Override
	public String getType() {
		return new String("Pair");
	}
}
