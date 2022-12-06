/**
 * Creates the Client object for the client handler.
 * Makes an instance of UI that allows the client to play Chess.
 * Operates differently depending on selected game mode.
 *  
 *  @authors Ali Sartaz Khan & Jonathan Houge
 */

import game.Robot;
import displays.Player;
import displays.PlayerCreateDisplay;

import java.net.Socket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Client {
	private Socket socket;
	private BufferedWriter out;
	private BufferedReader in;
	private static Player player; // you!
	
	// Creates the player and determines how the Client wishes to play.
	public static void main(String[] args) throws Exception {
		player = (new PlayerCreateDisplay()).start(); // display lets client pick name, color, mode, etc.
		
		//-- if statements determine who client has decided to play chess with
		
		if (player.getOpponent() == "Remote") { // remotely, need to connect to the server
			Socket socket = new Socket("localhost", 600);
			new Client(socket); }
		
		//-- if we're going against someone locally or a robot, no need for a server
		else if (player.getOpponent() == "Robot") { // playing against the robot
			Robot opponent; String robotColor;
			
			if (player.getColor().equals("White")) { robotColor = "Black"; }
			else { robotColor = "White"; }
			
			opponent = new Robot(robotColor); 
			new Client(opponent); }
		
		else { // playing locally 
	        UI ui;
	        boolean again = true;
	        
			while (again) { // keeps running until the client doesn't want to play another game
				ui = new UI(player);
				again = ui.start(); } }
    }
	
	/**
	 * Client for robot play. Makes a new robot and UI instance
	 * whenever the user wishes to play again.
	 * 
	 * @param opponent: an instance of Robot
	 */
	public Client(Robot opponent) {
		UI ui;
        boolean again = true;
		while (again) { // keeps running until the client doesn't want to play another game
			ui = new UI(this, opponent);
			again = ui.start();
			opponent = new Robot(opponent.getColor()); }
	}

	/**
	 * Creates new input output streams and takes in a new socket
	 * 
	 * @param socket: Socket object
	 */
	public Client(Socket socket) throws IOException {
		try {
			this.socket = socket;
			this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} 
		catch (IOException e) {
			close();
		}
		
		out.write(player.getName() + "-" + player.getColor() + "-" + player.getPreferredTime()); // sends username to client manager
		out.newLine();
		out.flush();
		String msgFromOthers = in.readLine();
		System.out.println("My INFO: " + msgFromOthers);
		String[] list = msgFromOthers.split("[:-]");
		int ID = Integer.parseInt(list[1]);
		System.out.println("Client" + ID + " Connected!");

		String color = list[2];
		player.setID(ID);
		System.out.println(color.equals("White"));
		player.setColor(color);	
		String username = list[3];
		player.setUsername(username);
		String time = list[4]; // minutes
		time += (":" + list[5]); // seconds
		player.setTime(time);
		
		// CREATES UI OBJECT AND STARTS GAME
		UI ui;
        boolean again = true;
		while (again) { // keeps running until the client doesn't want to play another game
			ui = new UI(this, in, out, socket);
			again = ui.start(); }
	}
	
	/**
	 * Closes server when user leaves the game
	 */
	public void close() {
     	try {
     		socket.close(); in.close(); out.close(); } 
        catch (IOException e1) {}
     	System.exit(0);
     	System.out.println("Server is closed!");
 	}
	
	/**
	 * player getter. returns client's associated player.
	 * 
	 * @return player: client's associated player
	 */
	public Player getPlayer() { return player; }
}