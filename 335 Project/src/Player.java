/* The Player class. Created by client by filling out the options from 
 * PlayerCreateDisplay. Holds onto the player's tank as well as information
 * relative to it, plus finding the player's local bounds (resolution).
 * 
 * AUTHOR: Jonathan Houge
 */

import java.util.Random;
import java.io.Serializable;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.graphics.Rectangle;

public class Player implements Serializable {
	
	private static final long serialVersionUID = 1L; // to appease the java gods
	
	private String name;
	private String[] names = {":^)", ":(", ":)", ";~;", "o~o", "uwu"}; // default names
	private int id;
	
	// maybe alter later
//	int x; 
//	int y;
	
	// constructor. if the player didn't type in a new name, we give them a random 
	public Player(String name) {
		if (name.equals("Enter name here!")) { // randomly assign a name if none chosen
			Random ran = new Random();
			this.name = names[ran.nextInt(6)]; 
			System.out.println("You didn't specify a name, so we're giving you a random one! It's " + this.name); }
		else { this.name = name; } 
		
		this.id = id;
	}
	
	// find the player/client's bounds, could be helpful
//	public void bounds() {
//		Display display = new Display();
//		Shell shell = new Shell(display);
//		
//		Rectangle bounds = shell.getMonitor().getBounds();
//		this.x = bounds.width; this.y = bounds.height;
//		display.dispose(); }
	
	// string method - returns name
	public String toString() {
		return name; }
	
	// setters (maybe keeping)
	public void setID(int id) { this.id = id; }
//	public void setDisplayWidth(int newX) { this.x = newX; }
//	public void setDisplayHeight(int newY) { this.y = newY; }
	
	// getters
	public String getName() { return name; }
}