import java.util.Arrays;
/**
 * This class is a subclass of the Hand class, and is used to model a hand of Flush. 
 * It overrides getTopCard,isValid and getType method that it inherits from Hand class.
 * 
 * @author Goli Smaran
 *
 */
public class Flush extends Hand {
	private static final long serialVersionUID = 1L;
	
    /**
	 * Constructor for the Flush type hand. Calls the constructor of Hand super class.
	 * 
	 * @param player Player who plays the hand.
	 * @param cards  List of cards played by the player.
	 */
	public Flush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}

	/**
	 * Returns the top card of the hand.
	 * 
	 * @return Card returns the top card of the hand.
	 */
	@Override
	public Card getTopCard() {
		int[] ranks = new int[5];
		for (int currentCard = 0; currentCard < 5; currentCard++) {
			int rank = getCard(currentCard).getRank();
			if (rank == 0) {
				ranks[currentCard] = 13;
			}
			else if (rank == 1) {
				ranks[currentCard] = 14;
			}
			else {
				ranks[currentCard] = rank;
			}
		}
		Arrays.sort(ranks);
		for (int topCard = 0; topCard < ranks.length; topCard++) {
			if (getCard(topCard).getRank() == ranks[4]) {
				return getCard(topCard);
			}
		}
		return null;	
	}

	/**
	 * Checks whether the hand is a Flush.
	 * 
	 * @return boolean true if the hand is a Flush or else false.
	 */
	@Override
	public boolean isValid() {
		if (size() != 5) {
			return false;
		}
		int[] suits = new int[5];
		for (int currentCard = 0; currentCard < 5; currentCard++) {
			suits[currentCard] = getCard(currentCard).getSuit();
		}
		
		int primarySuit = suits[0];
		for (int index = 1; index < suits.length; index++) {
			if (suits[index] != primarySuit) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns type of string.
	 * 
	 * @return String returns the type of the hand.
	 */
	@Override
	public String getType() {
		return new String("Flush");
	}
}
