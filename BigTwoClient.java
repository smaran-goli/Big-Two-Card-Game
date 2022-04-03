import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

/**
 * The BigTwoClient class implements the NetworkGame interface. 
 * It is used to model a Big Two card game that supports 4 players playing over the internet.
 * 
 * @author Goli Smaran
 */
public class BigTwoClient implements NetworkGame {
	
	private BigTwo bigTwo;
	
	/*
	 * A BigTwoUI object for providing the user interface.
	 */
	private BigTwoGUI gui;
	
	private Socket socket;
	
	private ObjectOutputStream oos;
	
	private int playerID;
	
	private String playerName;
	
	private String serverIP;
	
	private int serverPort;
	
	private ObjectInputStream ois;
	
	/**
	 *  A constructor for creating the BigTwo client. 
	 */
	public BigTwoClient(BigTwo bigTwo, BigTwoGUI gui) {
		this.bigTwo = bigTwo;
		this.gui = gui;
		
		String name = JOptionPane.showInputDialog("Enter Your Name: ");
		if (name == null) {
			gui.printMsg("Enter your name.\n");
			gui.disable();
		} else {
			// networking
			this.setPlayerName(name);
			this.setServerIP("127.0.0.1");
			this.setServerPort(2396);
			connect();
			gui.disable();
		}
	}

	/** 
     * Method to get the player ID (getter)
     * 
     * @return Integer stating ID of the player
     */
	@Override
	public int getPlayerID() {
		return playerID;
	}

	/** 
     * Method to set the player ID (getter)
     */
	@Override
	public void setPlayerID(int playerID) {
		this.playerID = playerID;		
	}

	/** 
     * Method to get the player name (getter)
     * 
     * @return String returning the name of the player
     */
	@Override
	public String getPlayerName() {
		return playerName;
	}

	/** 
     * Method to set the player name (getter)
     * 
     */
	@Override
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	/** 
     * Method to get the serverIP (getter)
     * 
     * @return String returning the serverIP
     */
	@Override
	public String getServerIP() {
		return serverIP;
	}

	/** 
     * Method to set the serverIP (getter)
     * 
     */
	@Override
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
		
	}

	/** 
     * Method to get the serverPort (getter)
     * 
     * @return int returning the serverPort
     */
	@Override
	public int getServerPort() {
		return serverPort;
	}

	/** 
     * Method to get the serverPort
     * 
     */
	@Override
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;		
	}

	/** 
     * Method to connect to the server using IP address and port number
     * 
     */
	@Override
	public void connect() {
		if (socket==null) {
			try {
				socket = new Socket(this.getServerIP(),this.getServerPort());
				oos = new ObjectOutputStream(socket.getOutputStream());
				
				Thread thread = new Thread(new ServerHandler());
				thread.start();
				
				CardGameMessage enterGame = new CardGameMessage(CardGameMessage.JOIN,-1,this.getPlayerName());
				sendMessage(enterGame);
				
				CardGameMessage getReady = new CardGameMessage(CardGameMessage.READY,-1,null);
				sendMessage(getReady);
			} 
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		else if (socket!=null && !socket.isConnected()) {
			try {
				socket = new Socket(this.getServerIP(),this.getServerPort());
				oos = new ObjectOutputStream(socket.getOutputStream());
				
				Thread thread = new Thread(new ServerHandler());
				thread.start();
				
				CardGameMessage enterGame = new CardGameMessage(CardGameMessage.JOIN,-1,this.getPlayerName());
				sendMessage(enterGame);
				
				CardGameMessage getReady = new CardGameMessage(CardGameMessage.READY,-1,null);
				sendMessage(getReady);
			} 
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
	}

	/**
     * Method to connect to the server using IP address and port number
     * 
     */
	@Override
	public void parseMessage(GameMessage message) {
		if (CardGameMessage.MOVE==message.getType()) {
			bigTwo.checkMove(message.getPlayerID(),(int[]) message.getData());
		} 
		else if (CardGameMessage.PLAYER_LIST==message.getType()) {					
			 for(int i=0;i<bigTwo.getNumOfPlayers();i++) {
				 if(((String[])message.getData())[i] != null) {
					 this.setPlayerID(message.getPlayerID());
					 bigTwo.getPlayerList().get(i).setName(((String[]) message.getData())[i]);
				 }
			 }
			this.gui.repaint();
		}
		else if (CardGameMessage.QUIT == message.getType() ) {
			gui.printMsg("Player "+ bigTwo.getPlayerList().get(message.getPlayerID()).getName()+" has left game.\n");
			bigTwo.getPlayerList().get(message.getPlayerID()).setName("");
			if (!bigTwo.endOfGame()) {
				CardGameMessage ready = new CardGameMessage(CardGameMessage.READY,-1,null);
				gui.disable();
				sendMessage(ready);
			}
		} 
		else if (CardGameMessage.FULL == message.getType()) {
			gui.printMsg("Server cannot handle any further new player's. Sorry try later!\n");
			try {
				socket.close();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		} 
		else if (CardGameMessage.READY == message.getType()) {
			gui.printMsg("Player " + message.getPlayerID() + " is ready for the game!\n");
		} 
		else if (CardGameMessage.JOIN == message.getType()) {
			gui.printMsg("Player "+ bigTwo.getPlayerList().get(message.getPlayerID()).getName() + " has joined game.\n");
			bigTwo.getPlayerList().get(message.getPlayerID()).setName((String)message.getData());
			gui.repaint();
		}
		else if (CardGameMessage.START == message.getType()) {
			Deck deck = (Deck) message.getData();
			gui.enable();
			bigTwo.start(deck);
			gui.repaint();
			
		} 
		else if (CardGameMessage.MSG  == message.getType()) {
			//gui.printChatMessage((String) message.getData());
		}		
	}

	/** 
     * Method to connect to the server using IP address and port number
     * 
     */
	@Override
	public void sendMessage(GameMessage message) {
		try {
			oos.writeObject(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** 
     * an inner class that implements the Runnable interface. 
     * 
     * @author Goli Smaran
     */
	class ServerHandler implements Runnable {
		@Override
		public void run() {	
			try {
				ois = new ObjectInputStream(socket.getInputStream());
				CardGameMessage gameMessage;
				while (!socket.isClosed()) {
					if ((gameMessage = (CardGameMessage) ois.readObject()) != null) {
						parseMessage(gameMessage);
					}
				}
				ois.close();
			} 
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
