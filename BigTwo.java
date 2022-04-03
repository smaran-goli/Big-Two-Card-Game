import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * The BigTwo class implements the CardGame interface.
 * It is used to model a Big Two card game that supports 4 players to play.
 * 
 * @author Goli Smaran
 *
 */
public class BigTwo implements CardGame {
	/*
	 * An integer specifying the number of players.
	 */
	private int numOfPlayers;
	
	/*
	 * A deck object for the BigTwo game.
	 */
	private Deck deck;
	
	/*
	 * A list of all players in the game 
	 */
	private ArrayList<CardGamePlayer> playerList;
	
	/*
	 * A list of hands played on the table.
	 */
	private ArrayList<Hand> handsOnTable;
	
	/*
	 * An integer specifying the playerID (i.e., index) of the local player.
	 */
	private int currentPlayerIdx;
	
	/*
	 * A BigTwoUI object for providing the user interface.
	 */
	private BigTwoGUI gui;
	
	/**
	 * A constructor for creating the BigTwo game.
	 * creates 4 players and add them to the player list.
	 * create a BigTwoUI object for providing the user interface.
	 */
	
	/**
	 * A BigTwoClient object
	 */
	public BigTwoClient bigTwoClient;
	
	/**
	 * A constructor for BigTwo class
	 */
	public BigTwo() {
		playerList = new ArrayList<>(4);
		playerList.add(new CardGamePlayer());
		playerList.add(new CardGamePlayer());
		playerList.add(new CardGamePlayer());
		playerList.add(new CardGamePlayer());
		numOfPlayers = playerList.size();
		
		handsOnTable = new ArrayList<>();
		gui = new BigTwoGUI(this);
		bigTwoClient = new BigTwoClient(this, gui);
	}

    /** 
     * Method to get the number of players (getter)
     * 
     * @return Integer stating number of players.
     */
	@Override
	public int getNumOfPlayers() {
		return numOfPlayers;
	}

    /**
     * Method to retrieve the deck (getter)
     * 
     * @return Deck object with the current deck of cards
     */
	@Override
	public Deck getDeck() {
		return deck;
	}

    /**
     * Method to retrieve the players list (getter)
     * 
     * @return ArrayList containing the list of players
     */
	@Override
	public ArrayList<CardGamePlayer> getPlayerList() {
		return playerList;
	}

	 /**
     * Method to show current hand on table (getter)
     * 
     * @return ArrayList having the cards played
     */
	@Override
	public ArrayList<Hand> getHandsOnTable() {
		return handsOnTable;
	}

	 /**
     * Method getting the index of the active player (getter)
     * 
     * @return int returns the index of the active player
     */
	@Override
	public int getCurrentPlayerIdx() {
		return currentPlayerIdx;
	}

    /**
     * Starts game with shuffled deck of cards
     * 
     * @param deck BigTwoDeck object: the shuffled deck of cards required
     */
	@Override
	public void start(Deck deck) {
		this.deck = deck;
		
		// removing cards from players hands
		for (int i = 0; i < numOfPlayers; i++) {
			playerList.get(i).getCardsInHand().removeAllCards();
		}
		// removing all cards from the table
		handsOnTable.clear();
		
		// allocating cards to players
		for (int i = 0; i < 52; i++) {
			playerList.get(i % numOfPlayers).addCard(deck.getCard(i));
		}
		// sorting cards
		for (int i = 0; i < numOfPlayers; i++) {
			playerList.get(i).getCardsInHand().sort();
		}
		
		// identification of 3 of diamonds
		Card threeOfDiamondCard = new Card(0, 2);
		for (int i = 0; i < numOfPlayers; i++) {
			if (playerList.get(i).getCardsInHand().contains(threeOfDiamondCard)) {
				currentPlayerIdx = i;
				gui.setActivePlayer(i);
			}
		}
		gui.repaint();
		gui.promptActivePlayer();
	}

	/**
	 * Makes a move
	 * 
	 * @param playerIdx playerIdx of the player making move
	 * @param cardIdx Indices of the cards selected
	 */
	@Override
	public void makeMove(int playerIdx, int[] cardIdx) {
		CardGameMessage move = new CardGameMessage(CardGameMessage.MOVE, -1, cardIdx);
		checkMove(playerIdx, cardIdx);
		// if game does not end, the player is prompted to play the card.
		if (!endOfGame()) {
			gui.promptActivePlayer();
		}
	}
	
	
    /**
	 * Checks whether move is valid or not
	 * 
	 * @param playerIdx playerIdx of the player making move
	 * @param cardIdx Indices of the cards selected
	 */
	public void checkMove(int playerIdx, int[] cardIdx) {
		CardList cardList;
		Card threeOfDiamonds = new Card(0, 2);
		boolean flag = true;
		if (cardIdx != null) {
			cardList = playerList.get(playerIdx).play(cardIdx);
			Hand hand = BigTwo.composeHand(playerList.get(playerIdx), cardList);
			if (handsOnTable.isEmpty()) {
				//checks if beginning move contains 3 of diamonds
				if (hand != null && hand.contains(threeOfDiamonds) && hand.isEmpty() == false && hand.isValid() == true) {
					flag = true;
				}
				else {
					flag = false;
				}
			}
			else {
				// checks if the previous player and the current player are the same or not
				if (hand != null && handsOnTable.get(handsOnTable.size() - 1).getPlayer() != playerList.get(playerIdx)) {
					flag = (hand.isEmpty() == false) ? hand.beats(handsOnTable.get(handsOnTable.size() - 1)) : false;  
				}
				else {
					flag = (hand != null && hand.isEmpty() == false) ? true : false; 
				}	
			}
			// if the hand is valid, remove the cards from player's hand and add it to the table and move to the next player
			if (flag == true && hand.isValid()) {
				for (int i = 0; i < cardList.size(); i++) {
					playerList.get(playerIdx).getCardsInHand().removeCard(cardList.getCard(i));
				}
				handsOnTable.add(hand);
				gui.printMsg("{" + hand.getType() + "} " + handsOnTable.get(handsOnTable.size()-1).toString() + "\n");
				currentPlayerIdx = (currentPlayerIdx + 1) % 4;
				gui.setActivePlayer(currentPlayerIdx);
			}
			else { // if hand is invalid, allow the player to play again.
				gui.printMsg("Not a legal move!!!\n");
			}
		}
		else { 
			// checks if the player passes his move; if he passes - move to the next player
			if (handsOnTable.isEmpty() == false && handsOnTable.get(handsOnTable.size() - 1).getPlayer() != playerList.get(playerIdx)) {
				currentPlayerIdx = (currentPlayerIdx + 1) % 4;
				gui.setActivePlayer(currentPlayerIdx);
				gui.printMsg("{Pass}\n");
				flag = true;
			}
			else {
				flag = false;
				gui.printMsg("Not a legal move!!!\n");	
			}	
		} 
		if (!endOfGame()) {
			gui.repaint();
		}
		
		// if the game ends, display the winner and number of cards each player holds.
		if (endOfGame()) {
			gui.setActivePlayer(-1);
			gui.repaint();
			String msg = new String("Game ends\n");

			for (int i = 0; i < playerList.size(); i++) {
				if (playerList.get(i).getCardsInHand().size() == 0) {
					msg = msg + "Player " + i + " wins the game.\n";
				}
				else {
					msg = msg + "Player " + i + " has " + playerList.get(i).getCardsInHand().size() + " cards in hand.\n";
				}
				playerList.get(i).removeAllCards();
			}
			gui.printMsg(msg);
			gui.disable();
			CardGameMessage ready = new CardGameMessage(CardGameMessage.READY,-1,null);
		}
	}

    /**
     * Method checking for end of game
     * 
     * @return boolean true if the game ends else false
     */
	@Override
	public boolean endOfGame() {
		for (int i = 0; i < numOfPlayers; i++) {
			if (playerList.get(i).getNumOfCards() == 0) {
				return true;
			}
		}
		return false;
	}
	
    /**
     * Main creates BigTwo object.
     * 
     * @param args Not used
     */
	public static void main(String[] args) {
		BigTwo bigTwo = new BigTwo();
	}
	
	/**
     * Method returning a valid hand from all the list of cards played
     * 
     * @param player CardGamePlayer object: stores the list of players
     * @param cards CardList object: stores list of cards played
     * 
     * @return Hand returns Type of hand
     */
	public static Hand composeHand (CardGamePlayer player, CardList cards) {
		Hand hand;
		hand = new Single(player, cards);
		if (hand.isValid()) {
			return hand;
		}
		hand = new Pair(player, cards);
		if (hand.isValid()) {
			return hand;
		}
		hand = new Triple(player, cards);
		if (hand.isValid()) {
			return hand;
		}
		hand = new Quad(player, cards);
		if (hand.isValid()) {
			return hand;
		}
		hand = new Straight(player, cards);
		if (hand.isValid()) {
			return hand;
		}
		hand = new Flush(player, cards);
		if (hand.isValid()) {
			return hand;
		}
		hand = new FullHouse(player, cards);
		if (hand.isValid()) {
			return hand;
		}
		hand = new StraightFlush(player, cards);
		if (hand.isValid()) {
			return hand;
		}
		return null;
	}
	
}
