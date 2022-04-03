/** 
 * This class is a subclass of the Deck class, and is used to model a deck of cards used in a Big Two card game. 
 * It overrides the initialize() method it inherited from the Deck class to create a deck of Big Two cards.
 *  
 * @author Goli Smaran
 *
 */
public class BigTwoDeck extends Deck {
	private static final long serialVersionUID = 1L;

	/**
	 * Initializes the deck of cards
	 */
	@Override
	public void initialize() {
		removeAllCards();
		for (int suit = 0; suit < 4; suit++) {
			for (int rank = 0; rank < 13; rank++) {
				BigTwoCard bigTwoCard = new BigTwoCard(suit, rank);
				addCard(bigTwoCard);
			}
		}
	}
}
