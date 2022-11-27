/*
 * Name: Ali Sartaz Khan & Jonathan Houge
 * Course: CSc 335
 * Description: Creates the Client object for the client handler
 */

import java.net.Socket;
import java.util.ArrayList;
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
		
		if (player.getOpponent() == "Human") {
			Socket socket = new Socket("localhost", 600);
			Client client = new Client(socket); }
		
		else { // if we're going against a robot, no need for a server
			Robot opponent;
			if (player.getOpponent() == "Robot (Easy)") {
				opponent = new RandomAI(); }
			else {
				opponent = new AlgorithmAI(); }
			// play against the robot!
		}
    }
	
	/*
	 * Creates new input output streams and takes in a new socket
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

		out.write(player.getName() + "-" + player.getColor()); // sends username to client manager
		out.newLine();
		out.flush();
        UI ui = new UI(this, in, out, socket);
        ui.start();
		
		// CREATES UI OBJECT AND STARTS GAME

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
	 
	 public Player getPlayer() { return player; }
}