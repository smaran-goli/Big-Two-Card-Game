/**
 * This class is a subclass of the Hand class, and is used to model a hand of Full House. 
 * It overrides getTopCard,isValid and getType method that it inherits from Hand class.
 * 
 * @author Goli Smaran
 *
 */
public class FullHouse extends Hand {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for the FullHouse type hand. Calls the constructor of Hand super class.
	 * 
	 * @param player Player who plays the hand
	 * @param cards  List of cards played by the player
	 */
	public FullHouse(CardGamePlayer player, CardList cards) {
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
		if (getCard(0).getRank() == getCard(2).getRank()) {
			return getCard(2);
		}
		else {
			return getCard(4);
		}
	}

	/**
	 * Checks whether the hand is a FullHouse.
	 * 
	 * @return boolean true if the hand is a FullHouse or else false.
	 */
	@Override
	public boolean isValid() {
		if (size() != 5) {
			return false;
		}
		sort();
		if (getCard(0).getRank() == getCard(2).getRank()) {
			if ((getCard(0).getRank() == getCard(1).getRank()) && 
					(getCard(1).getRank() == getCard(2).getRank()) && 
					    (getCard(3).getRank() == getCard(4).getRank())) {
				return true;
			}
		}
		
		else if (getCard(2).getRank() == getCard(4).getRank()) {
			if ((getCard(2).getRank() == getCard(3).getRank()) && 
				    (getCard(3).getRank() == getCard(4).getRank()) && 
						(getCard(0).getRank() == getCard(1).getRank())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns type of string.
	 * 
	 * @return String returns the type of the hand.
	 */
	@Override
	public String getType() {
		return new String("FullHouse");
	}
}
