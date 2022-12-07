package displays;

import java.util.Random;

/**
 * The Player class. Created by client by filling out the options from 
 * PlayerCreateDisplay. Holds onto various things that the user has 
 * picked for themselves as well as external / internal information.
 * 
 * @author Jonathan Houge
 */
public class Player {
	
	private String name;
	private String[] names = {"[uwu]", "[;~;]", "[*u*]", "[#0#]", "[o~o]", "[oWo]"}; // default names
	private int ID; // assigned by server
	
	private String color;
	private String opponent;
	private String fileName;
	private String preferredTime;
	
	private int score;
	
	/**
	 * Player Constructor. Takes in everything entered by the client from the 
	 * PlayerCreateDisplay and sets up the Player object.
	 * 
	 * @param name: name entered by client. if it was never changed, we'll give them a default one.
	 * @param color: their chosen color, either black or white. may be changed by the server.
	 * @param opponent: the type of mode they've picked ("Local", "Remote" or "Robot").
	 * @param fileName: optional. if they've chosen to play a previously saved game.
	 * @param time: optional. if they've chosen to play with a time constraint.
	 */
	public Player(String name, String color, String opponent, String fileName, String time) {
		if (name.equals("Enter name here!")) { // randomly assign a name if none chosen
			Random ran = new Random();
			this.name = names[ran.nextInt(6)]; 
			System.out.println("You didn't specify a name, so we're giving you a random one! It's " + this.name); }
		else { this.name = name; }
		
		this.color = color; this.opponent = opponent; 
		this.fileName = fileName; this.preferredTime = time; }
	
	//-- setters
	
	/**
	 * setting the Player's ID. This is done to make sure the first player
	 * joined gets the color they picked. 
	 * @param id: the ID set by the server.
	 */
	public void setID(int id) { this.ID = id; }
	
	/**
	 * setting the Player's color. If their ID isn't '1', their color will be changed.
	 * @param color: the color set by the server.
	 */
	public void setColor(String color) { this.color = color; }
	
	/**
	 * setting the Player's username.
	 * @param name: username to be set.
	 */
	public void setUsername(String name) {this.name = name;}
	
	/**
	 * setting the Player's time.
	 * @param time: time string.
	 */
	public void setTime(String time) { this.preferredTime = time; }
	
	/**
	 * adding to the player's score. currently unimplemented. 
	 * @param points: points added from taken piece.
	 */
	public void addPoints(int points) { score += points; }
	
	//-- getters
	
	/**
	 * toString for the Player. returns the name.
	 * @return name of Player
	 */
	public String toString() { return name; }
	
	/**
	 * getter for name. 
	 * @return name of Player
	 */
	public String getName() { return name; }
	
	/**
	 * getter for the ID.
	 * @return ID of Player
	 */
	public int getID() { return ID; }
	
	/**
	 * getter for piece color.
	 * @return piece color of Player
	 */
	public String getColor() { return color; }
	
	/**
	 * getter for the opponent.
	 * @return Player's opponent
	 */
	public String getOpponent() { return opponent; }
	
	/**
	 * getter for the file name.
	 * @return name of file to load
	 */
	public String getFileName() {return fileName;}
	
	/**
	 * getter for the time.
	 * @return Player's time.
	 */
	public String getPreferredTime() {return preferredTime;}
	
	/**
	 * getter for Player score. currently unimplemented.
	 * @return ID of Player
	 */
	public int getScore() { return score; }
}