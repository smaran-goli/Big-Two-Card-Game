/**
 * The BigTwoCard class is a subclass of the Card class, and is used to model a card used in a Big Two card game.
 * It overrides the compareTo() method it inherited from the Card class to reflect the ordering of cards 
 * used in a Big Two card game. 
 * 
 * @author Goli Smaran
 *
 */
public class BigTwoCard extends Card {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates an instance of BigTwoCard by calling the constructor(super) of Card class.
	 * 
	 * @param suit int value between 0 and 3 representing the suit
	 * @param rank int value between 0 and 12 representing the rank
	 */
	public BigTwoCard(int suit, int rank) {
		super(suit, rank);
	}
	
	/**
	 * Overriding compareTo of card class. 
	 * Locally makes adjustments to incorporate the change in the rank order for Big Two Game.
	 * Sets rank of A and 2 higher than rest of the cards.
	 * 
	 * @param card The card with which user wants to compare. 
	 * @return int returns -1,0,1 in each of the cases
	 */
	public int compareTo(Card card) {
		int currentRank = this.rank;
		int cardRank = card.rank;
		if (currentRank == 0) {
			currentRank = 13;
		}
		else if (currentRank == 1) {
			currentRank = 14;
		}
		
		if (cardRank == 0) {
			cardRank = 13;
		}
		else if (cardRank == 1) {
			cardRank = 14;
		}
		
		if (currentRank > cardRank) {
			return 1;
		} 
		else if (currentRank < cardRank) {
			return -1;
		} 
		else if (this.suit > card.suit) {
			return 1;
		} 
		else if (this.suit < card.suit) {
			return -1;
		} 
		else {
			return 0;
		}
	}	
}
