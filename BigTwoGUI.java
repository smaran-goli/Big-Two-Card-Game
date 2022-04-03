import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * The BigTwoGUI class implements the CardGameUI interface. 
 * It is used to build a GUI for the Big Two card game and handle all user actions.
 * 
 * @author Goli Smaran
 */
public class BigTwoGUI implements CardGameUI {
	
	/*
	 * A Big Two card game associates with this GUI.
	 */
	private BigTwo game;
	
	/*
	 * A boolean array indicating which cards are being selected.
	 */
	private boolean[] selected;
	
	/*
	 * An integer specifying the index of the active player.
	 */
	private int activePlayer;
	
	/*
	 * The main window of the application.
	 */
	private JFrame frame;
	
	/*
	 * A panel for showing the cards of each player and the cards played on the table.
	 */
	private JPanel bigTwoPanel;
	
	/*
	 * A  button for the active player to play the selected cards
	 */
	private JButton playButton;
	
	/*
	 * A button for the active player to pass his/her turn to the next player
	 */
	private JButton passButton;
	
	/*
	 * A text area for showing the current game status as well as end of game messages
	 */
	private JTextArea msgArea;
	
	/*
	 * A text area for showing chat messages sent by the players.
	 */
	private JTextArea chatArea;
	
	/*
	 * A text field for players to input chat messages.
	 */
	private JTextField chatInput;
	
	/*
	 * An array to store card images
	 */
	private Image[][] cardImages;
	
	/*
	 * An Image object
	 */
	private Image flippedCardImage;
	
	/*
	 * An array to store player avatars
	 */
	private Image[] playerAvatars;
	
	/**
	 * A constructor for creating a BigTwoGUI The parameter game is a reference to a card game associates with this GUI.
	 * 
	 * @param game A Card Game of BigTwo type to play through this GUI.
	 */
	public BigTwoGUI(BigTwo game) {
		this.game =  game;
		selected = new boolean[13];
		setActivePlayer(game.getCurrentPlayerIdx());
		designGUI();
	}

	private void designGUI() {
		
		// frame
		frame = new JFrame("Big Two");
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// avatars and cards
		playerAvatars = new Image[4];
		playerAvatars[0] = new ImageIcon("Images/batman.jpg").getImage();
		playerAvatars[1] = new ImageIcon("Images/flash.png").getImage();
		playerAvatars[2] = new ImageIcon("Images/green.png").getImage();
		playerAvatars[3] = new ImageIcon("Images/superman.png").getImage();
		
		cardImages = new Image[4][13];
		final char[] suit = {'d','c','h','s'};
		final char[] rank = {'a', '2', '3', '4', '5', '6', '7', '8', '9', 't', 'j', 'q', 'k'};
		
		for(int suitValue = 0; suitValue < 4; suitValue++) {
			for(int rankValue = 0; rankValue < 13; rankValue++) {
				String loc = "Images/cards/" + rank[rankValue] + suit[suitValue] + ".gif";
				cardImages[suitValue][rankValue] = new ImageIcon(loc).getImage();
			}
		}
		
		flippedCardImage = new ImageIcon("Images/back.gif").getImage();
		
		// play area
		bigTwoPanel = new BigTwoPanel();
		bigTwoPanel.setPreferredSize(new Dimension(700,800));
		
		
	    JPanel messages = new JPanel();
	    messages.setLayout(new BoxLayout(messages, BoxLayout.PAGE_AXIS));
	    messages.setFont(new Font("Arial", Font.BOLD, 15));
	    
	    // texts
	    msgArea = new JTextArea(20,24);
	    msgArea.setEnabled(false);
	    DefaultCaret caret = (DefaultCaret) msgArea.getCaret();
	    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	    JScrollPane scrollPane = new JScrollPane();
	    scrollPane.setViewportView(msgArea);
	    messages.add(scrollPane);
	    
	    // chats
	    chatArea = new JTextArea(21,24);
	    chatArea.setEnabled(false);
	    DefaultCaret caretChat = (DefaultCaret) chatArea.getCaret();
	    caretChat.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	    JScrollPane scrollPaneChat = new JScrollPane();
	    scrollPaneChat.setViewportView(chatArea);
	    messages.add(scrollPaneChat);
	    
	    JPanel chat = new JPanel();
	    chat.setLayout(new FlowLayout());
	    chat.add(new JLabel("Message:"));
	    chatInput = new JTextField();
	    chatInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);
	    chatInput.addActionListener(new ChatMessageListener());
	    chatInput.setPreferredSize(new Dimension( 400, 24 ));
	    chat.add(chatInput);
	    messages.add(chat);
		
	    // Menu Bar
	    JMenuBar menuBar = new JMenuBar();
	    JMenu menu = new JMenu("Game");
	    menuBar.add(menu);
	    
	    // Game: Menu Item(Quit)
	    JMenuItem quit = new JMenuItem("Quit");
	    quit.addActionListener(new QuitMenuItemListener());
	    menu.add(quit);
	    
	    JMenu connectListener = new JMenu("Connect");
	    
		// Game: Menu Item(Connect)
	    JMenuItem connect = new JMenuItem("Connect");
	    connect.addActionListener(new ConnectMenuItemListener());
	    connectListener.add(connect);
	    menuBar.add(connectListener);
		
	    // South
	    JPanel southPanel = new JPanel();
	    playButton = new JButton("Play");
	    playButton.addActionListener(new PlayButtonListener());
	    passButton = new JButton("Pass");
	    passButton.addActionListener(new PassButtonListener());
	    southPanel.add(playButton);
	    southPanel.add(passButton);
	    
	    frame.add(menuBar, BorderLayout.NORTH);
	    frame.add(bigTwoPanel,BorderLayout.WEST);
	    frame.add(messages, BorderLayout.EAST);
	    frame.add(southPanel, BorderLayout.SOUTH);
	    frame.setResizable(true);
		
		frame.setResizable(true);
		frame.setSize(1200, 700);		
		frame.setVisible(true);
		
	}

	/**
	 * Sets the index of the active player (setter)
	 * 
	 * @param activePlayer int value, denotes index of the active player
	 */
	@Override
	public void setActivePlayer(int activePlayer) {
		if (activePlayer < 0 || activePlayer >= game.getNumOfPlayers()) {
			this.activePlayer = -1;
		} else {
			this.activePlayer = activePlayer;
		}	
	}
	
	/**
	 * Returns array of indices of selected cards
	 * 
	 * @return int[] returns indices of selected cards
	 * 
	 */
	public int[] getSelected() {
		ArrayList<Integer> allSelected = new ArrayList<Integer>();
		
		for (int index = 0; index < selected.length; index++) {
			if (selected[index]) {
				allSelected.add(index);
			}
		}
		if (allSelected.size() == 0) {
			return null;
		}
		int totalSize = allSelected.size();
		int[] result = new int[totalSize];
		for(int index = 0; index < totalSize; index++)
			result[index] = allSelected.get(index);
		return result;	
	}

	/**
	 * Repaints the GUI
	 */
	@Override
	public void repaint() {
		frame.repaint();
		
	}

	/**
	 * A method that prints the specified string to the message area of the card game table.
	 * 
	 * @param msg the string to be printed to the message area of the card game table.
	 */
	@Override
	public void printMsg(String msg) {
		msgArea.append(msg);		
	}

	/**
	 * Clears the message area
	 */
	@Override
	public void clearMsgArea() {
		msgArea.setText("");
	}
	
	/**
	 * Resets the list of selected cards to an empty list
	 */
	public void resetSelected() {
		this.selected = new boolean[13];
		this.repaint();
	}

	/**
	 * Resets the GUI
	 */
	@Override
	public void reset() {
		resetSelected();
		clearMsgArea();
		enable();
	}

	/**
	 * Enables interactions
	 */
	@Override
	public void enable() {
		playButton.setEnabled(true);
		passButton.setEnabled(true);		
	}

	/**
	 * Disables interactions
	 */
	@Override
	public void disable() {
		playButton.setEnabled(false);
		passButton.setEnabled(false);
	}

	/**
	 * Method for prompting the active player to select cards and make his/her move.
	 */
	@Override
	public void promptActivePlayer() {
		printMsg(game.getPlayerList().get(activePlayer).getName() + "'s turn: \n");
		resetSelected();
	}
	
	
	
	/**
	 * Inner class that extends JPanel class, implements the MouseListener interface
	 * Overrides paintComponent() to draw table
	 * Implements mouseReleased()to handle mouse click events
	 * 
	 * @author Goli Smaran
	 */
	class BigTwoPanel extends JPanel implements MouseListener{
		private static final long serialVersionUID = 1L;
		
		/**
		 * BigTwoPanel default constructor which adds the Mouse Listener and sets background of the table
		 */
		public BigTwoPanel() {
	        setBackground(new Color(50,150,50)); 
	        this.addMouseListener(this);
		}
		
		/**
		 * Draws avatars, text, cards
		 * 
		 * @param g Provided by system to allow drawing.
		 */
		@Override
		public void paintComponent(Graphics graphics) {
			// background
			super.paintComponent(graphics);
			graphics.setColor(Color.BLACK);
			Font font1 = new Font("Arial", Font.BOLD, 10);
			Font font2 = new Font("Arial", Font.BOLD, 10);
			
			//painting the four players avatars and their cards
			for (int index = 0; index < game.getNumOfPlayers(); index++) {
				if (index == game.getCurrentPlayerIdx()) {
					graphics.setColor(Color.WHITE);
					graphics.setFont(font1);
					graphics.drawString(game.getPlayerList().get(index).getName() + "(You)", 15, ((index*130)+15));
					graphics.setColor(Color.BLACK);
				}  
				else {
					graphics.setColor(Color.BLACK);
					graphics.setFont(font2);
					graphics.drawString(game.getPlayerList().get(index).getName(), 15,index*130+15);
					graphics.setColor(Color.BLACK);
				}
				graphics.drawImage(playerAvatars[index], 5, 30 + (index * 130), this);
				
				// displaying cards accordingly
				for(int k = 0; k < 4; k++) {
					graphics.drawLine(0, 110+130*k, 1600, 110+130*k);
				}
				for (int j = 0; j < game.getPlayerList().get(index).getNumOfCards(); j++) {
					if (index == game.getCurrentPlayerIdx()) {
						int suitValue = game.getPlayerList().get(index).getCardsInHand().getCard(j).getSuit();
						int rankValue = game.getPlayerList().get(index).getCardsInHand().getCard(j).getRank();
						if (selected[j] == true) 
							graphics.drawImage(cardImages[suitValue][rankValue], (80 + (j * cardImages[0][0].getWidth(this)/2)), (10 + (index * 130) - 10), this);
						else 
							graphics.drawImage(cardImages[suitValue][rankValue], (80 + (j * cardImages[0][0].getWidth(this)/2)), (10 + index * 130), this);
					} 
					else 
						graphics.drawImage(flippedCardImage, (80 + (j * cardImages[0][0].getWidth(this) / 2)), (10 + (index * 130)), this);
				}
			}
			
			if (game.getHandsOnTable().size()-1 > -1) {
				Hand previousHand  = game.getHandsOnTable().get(game.getHandsOnTable().size()-1);
				graphics.drawString("Played by " + previousHand.getPlayer().getName(), 5, (60 + (4 * 130) - 10));
				for (int index = 0; index < previousHand.size(); ++index) {
					int suitValue = previousHand.getCard(index).getSuit();
					int rankValue = previousHand .getCard(index).getRank(); 
					graphics.drawImage(cardImages[suitValue][rankValue], (150 + (index * 20)), (10 + (4 * 130)), this);
				}
			}
		}

		/** 
		 * Method used to control all mouse release events
		 * Overrides mouseReleased method of JPanel
		 * 
		 * @param e MouseEvent object which has been used to get the coordinates of the mouseRelease
		 */
		@Override
		public void mouseReleased(MouseEvent event) {
			if (activePlayer == game.getCurrentPlayerIdx()) {
				Image temp= cardImages[0][0];
				int width = temp.getWidth(this);
				int height = temp.getHeight(this);
				int totalNumberOfCards = game.getPlayerList().get(activePlayer).getNumOfCards();
								
				if (event.getX() >= 80 && event.getX() <= 80 + (width/2) * totalNumberOfCards + width && event.getY() >= activePlayer*130 && event.getY() <= (10 + (activePlayer * 130) + height)) {	
					int cardNumber = (int) Math.ceil((event.getX() - 80) / (width / 2));
					if(cardNumber / totalNumberOfCards > 0) 
						cardNumber = totalNumberOfCards-1;
					if (selected[cardNumber]) {
						if (event.getY() > (10+activePlayer*130+height - 10) && event.getX() < ( 80 + (width/2) * cardNumber + width/2) && selected[cardNumber-1] == false) {
							if (cardNumber != 0) 
								cardNumber = cardNumber - 1;
							selected[cardNumber] = true;
						} 
						else if (event.getY() < (10 + (activePlayer * 130) + height - 10)) {
							selected[cardNumber] = false;
						}						
					} 
					else if (event.getY() > (activePlayer * 130 + 10)) {
						selected[cardNumber] = true;
					}
					else if (selected[cardNumber - 1] && event.getX() < (80+(width/2)*cardNumber + width/2)) { 
						selected[cardNumber-1] = false;
					}
					
					this.repaint();
				}
			}
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			
		}		
	}
	
	/**
	 * Inner Class that implements the ActionListener interface
	 * Implements the actionPerformed() method from the ActionListener interface for "play" button 
	 * Calls the makeMove() method of BigTwo object to make a move on click
	 * 
	 * @author Goli Smaran
	 */
	class PlayButtonListener implements ActionListener {
		/**
		 * Overriddes from the ActionListener Interface 
		 * Performs requisite function on click
		 * 
		 * @param e ActionEvent object to detect user interaction
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if (activePlayer == game.getCurrentPlayerIdx()) {
				if (getSelected() == null) {
					printMsg("No Cards Selected\n");
					
				} else {
					game.makeMove(activePlayer, getSelected());
				}
			}
		}
	}
	
	/**
	 * Inner Class that implements the ActionListener interface
	 * Implements the actionPerformed() method from the ActionListener interface for "pass" button 
	 * Calls the makeMove() method of BigTwo object to make a move on click
	 * 
	 * @author Goli Smaran
	 */
	class PassButtonListener implements ActionListener {
		/**
		 * Overriddes from the ActionListener Interface 
		 * Performs requisite function on click
		 * 
		 * @param e ActionEvent object to detect user interaction
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if (activePlayer == game.getCurrentPlayerIdx()) {
				game.makeMove(activePlayer, null);
			}		
		}
	}
	
	
	/**
	 * Inner Class that implements the ActionListener interface
	 * Implements the actionPerformed() method from the ActionListener interface for "quit" button
	 * 
	 * @author Goli Smaran
	 */
	class QuitMenuItemListener implements ActionListener {
		/**
		 * Overrides from the ActionListener Interface 
		 * Performs requisite function on click
		 * 
		 * @param e ActionEvent object to detect user interaction
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);			
		}		
	}
	
	/**
	 * Inner Class that implements the ActionListener interface
	 * Implements the actionPerformed() method from the ActionListener interface for "connect" button
	 * 
	 * @author Goli Smaran
	 */
	class ConnectMenuItemListener implements ActionListener
	{
		/**
		 * Overrides from the ActionListener Interface 
		 * Performs requisite function on click
		 * 
		 * @param e ActionEvent object to detect user interaction
		 */
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if (game.getCurrentPlayerIdx() == -1) 
			{
				game.bigTwoClient.connect();
			} 
			
			else if (game.getCurrentPlayerIdx() >= 0 && game.getCurrentPlayerIdx() <= 3)
			{
				printMsg("Connection already established!\n");
			}
				
		}
	}
	
	/**
	 * Inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface for message(chat) field in the game
	 * @author Goli Smaran
	 */
	class ChatMessageListener implements ActionListener{
		/**
		 * Overrides from the ActionListener Interface 
		 * Performs requisite function on click
		 * 
		 * @param e ActionEvent object to detect user interaction
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			String message = chatInput.getText();
			chatInput.setText("");
			chatArea.append(game.getPlayerList().get(activePlayer).getName() + " : " + message + "\n");
		}
	}
}
