/**
 *This class is a subclass of the CardList class, and is used to models hand of cards used in game.
 * 
 * 
 * @author Goli Smaran
 *
 */
public abstract class Hand extends CardList implements ValidateInterface {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Player who plays the hand.
	 */
	private CardGamePlayer player;
	
	/**
	 * Constructor for Hand class that gives value to player and cards.
	 * 
	 * @param player Player who played the hand.
	 * @param cards list of card that the player played.
	 */
	public Hand(CardGamePlayer player, CardList cards) {
		this.player = player;
		for (int currentCard = 0; currentCard < cards.size(); currentCard++) {
			addCard(cards.getCard(currentCard));
		}
	}
	
	/**
	 * Getter function for player who played this hand.
	 * 
	 * @return CardGamePlayer returns the player of the current hand object.
	 */
	public CardGamePlayer getPlayer() {
		return player;
	}
	
	/**
	 * This method returns the top card of the hand.
	 * 
	 * @return Card returns the top card of the particular hand formed.
	 */
	public Card getTopCard() {
		return null;
	}
	
	/**
	 * This method compares two hands and checks whether this hand beats the hand send through the argument.
	 * 
	 * @param hand - hand that needs to be compared with this hand.
	 * @return boolean true if this hand beats the hand send as an argument, false otherwise.
	 */
	public boolean beats(Hand hand) {
		if (hand.size() == 1) {
			if (this.size() == hand.size() && this.isValid() && this.getTopCard().compareTo(hand.getTopCard()) == 1) {
				return true;
			}
		}
		else if (hand.size() == 2) {
			if (this.size() == hand.size() && this.isValid() && this.getTopCard().compareTo(hand.getTopCard()) == 1) {
				return true;
			}
		}
		else if (hand.size() == 3) {
			if (this.size() == hand.size() && this.isValid() && this.getTopCard().compareTo(hand.getTopCard()) == 1) {
				return true;
			}
		}
		else if (hand.size() == 5) {
			if (this instanceof StraightFlush) {
				if (this.size() == hand.size()) {
					if (this.getType().equals(hand.getType()) && this.getTopCard().compareTo(hand.getTopCard()) == 1) {
						return true;
					}
					else if (this.getType().equals(hand.getType()) && this.getTopCard().compareTo(hand.getTopCard()) == -1) {
						return false;
					}
					else {
						return true;
					}
				}
			}
			if (this instanceof Quad) {
				if (this.size() == hand.size()) {
					if (this.getType().equals(hand.getType()) && this.getTopCard().compareTo(hand.getTopCard()) == 1) {
						return true;
					}
					else if (this.getType().equals(hand.getType()) && this.getTopCard().compareTo(hand.getTopCard()) == -1) {
						return false;
					}
					else {
						if (hand.getType().equals("Straight") || hand.getType().equals("Flush") || hand.getType().equals("FullHouse")) {
							return true;
						}
					}
				}
			}
			
			if (this instanceof FullHouse) {
				if (this.size() == hand.size()) {
					if (this.getType().equals(hand.getType()) && this.getTopCard().compareTo(hand.getTopCard()) == 1) {
						return true;
					}
					else if (this.getType().equals(hand.getType()) && this.getTopCard().compareTo(hand.getTopCard()) == -1) {
						return false;
					}
					else {
						if (hand.getType().equals("Straight") || hand.getType().equals("Flush")) {
							return true;
						}
					}
				}
			}
			if (this instanceof Flush) {
				if (this.size() == hand.size()) {
					if (this.getType().equals(hand.getType()) && this.getTopCard().compareTo(hand.getTopCard()) == 1) {
						return true;
					}
					else if (this.getType().equals(hand.getType()) && this.getTopCard().compareTo(hand.getTopCard()) == -1) {
						return false;
					}
					else {
						if (hand.getType().equals("Straight")) {
							return true;
						}
					}
				}
			}
			if (this instanceof Straight) {
				if (this.size() == hand.size()) {
					if (this.getType().equals(hand.getType()) && this.getTopCard().compareTo(hand.getTopCard()) == 1) {
						return true;
					}
					else if (this.getType().equals(hand.getType()) && this.getTopCard().compareTo(hand.getTopCard()) == -1) {
						return false;
					}
					else {
						return false;
					}
				}
			}
		}
		return false;
	} 
	
}
