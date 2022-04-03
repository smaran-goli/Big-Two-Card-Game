/**
 * This class is a subclass of the Hand class, and is used to model a hand of Triple. 
 * It overrides getTopCard,isValid and getType method that it inherits from Hand class.
 * 
 * @author Goli Smaran
 *
 */

public class Triple extends Hand {
    private static final long serialVersionUID = 1L;
	
    /**
	 * Constructor for the Triple type hand. Calls the constructor of Hand super class.
	 * 
	 * @param player Player who plays the hand
	 * @param cards  List of cards played by the player
	 */
	public Triple(CardGamePlayer player, CardList cards) {
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
		return getCard(2);
	}

	/**
	 * Checks whether the hand is a Triple.
	 * 
	 * @return boolean true if the hand is a Triple or else false.
	 */
	@Override
	public boolean isValid() {
		if (size() == 3) {
			if (getCard(0).getRank() == getCard(1).getRank()) {
				if (getCard(1).getRank() == getCard(2).getRank()) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
		return false;
	}

	/**
	 * Returns type of string.
	 * 
	 * @return String returns the type of the hand
	 */
	@Override
	public String getType() {
		return new String("Triple");
	}
}
