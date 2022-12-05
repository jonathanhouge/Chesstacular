/* The Player class. Created by client by filling out the options from 
 * PlayerCreateDisplay.
 * 
 * AUTHOR: Jonathan Houge
 */

import java.util.Random;

public class Player {
	
	private String name;
	private String[] names = {"[uwu]", "[;~;]", "[*u*]", "[#0#]", "[o~o]", "[oWo]"}; // default names
	private int ID;
	
	private String color;
	private String opponent;
	private String fileName;
	private String preferredTime;
	
	private int score;
	
	// constructor. if the player didn't type in a new name, we give them a random one.
	public Player(String name, String color, String opponent, String fileName, String time) {
		if (name.equals("Enter name here!")) { // randomly assign a name if none chosen
			Random ran = new Random();
			this.name = names[ran.nextInt(6)]; 
			System.out.println("You didn't specify a name, so we're giving you a random one! It's " + this.name); }
		else { this.name = name; }
		
		this.color = color; this.opponent = opponent; this.fileName = fileName; this.preferredTime = time;}
	
	//-- setters
	public void setID(int id) { this.ID = id; }
	public void setColor(String color) {this.color = color;}
	public void addPoints(int points) { score += points; }
	public void setUsername(String name) {this.name = name;}
	
	//-- getters
	public String toString() { return name; }
	public String getName() { return name; }
	public int getID() { return ID; }
	public String getColor() { return color; }
	public String getOpponent() { return opponent; }
	public int getScore() { return score; }
	public String getFileName() {return fileName;}
	public String getPreferredTime() {return preferredTime;}

	public void setTime(String time) {this.preferredTime = time;}
}