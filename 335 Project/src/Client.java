/*
 * Name: Ali Sartaz Khan & Jonathan Houge
 * Course: CSc 335
 * Description: Creates the Client object for the client handler
 */

import java.net.Socket;
import java.util.ArrayList;

import displays.Player;
import displays.PlayerCreateDisplay;
import game.AlgorithmAI;
import game.RandomAI;
import game.Robot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Client {
	private Socket socket;
	private BufferedWriter out;
	private BufferedReader in;
	private static Player player;
	
	//	Chess ui
	public static void main(String[] args) throws Exception {
		player = (new PlayerCreateDisplay()).start();
		
		if (player.getOpponent() == "Remote") {
			Socket socket = new Socket("localhost", 600);
			Client client = new Client(socket); 
			}
		
		else if (player.getOpponent() == "Robot"){ // if we're going against someone locally or a robot, no need for a server
			Robot opponent;
			String robotColor;
			if (player.getColor().equals("White"))
				robotColor = "Black";
			else
				robotColor = "White";
			
			opponent = new Robot(robotColor); 
			Client client = new Client(opponent);
			
		}
		else {
			// play locally
	        UI ui;
	        boolean again = true;
			while (again) {
				ui = new UI(player);
				again = ui.start(); }
		}
		
    }
	
	/*
	 * Creates new UI class when playing against the robot
	 * 
	 * opponent: robot object
	 */
	public Client(Robot opponent) {
		UI ui;
        boolean again = true;
		while (again) {
			ui = new UI(this, opponent);
			again = ui.start(); }
	}
	
	
	/*
	 * Creates new input output streams and takes in a new socket
	 * 
	 * socket: Socket object
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
		while (again) {
			ui = new UI(this, in, out, socket);
			again = ui.start(); }

        
		

	}
	
	/*
	 * Closes server when user leaves the game
	 */
	public void close() {
     	try {
     		socket.close(); in.close(); out.close(); } 
        catch (IOException e1) {}
     	System.exit(0);
     	System.out.println("Server is closed!");
 	}
	 
	/*
	 * Returns player object
	 */
	 public Player getPlayer() { return player; }
}