/*
 * Name: Ali Sartaz Khan
 * Course: CSc 335
 * Description: Creates the Client object for the client handler
 */

import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Client 
{
	private Socket socket;
	private BufferedWriter out;
	private BufferedReader in;
	private static String username;



//	XTankUI ui;
	
	public static void main(String[] args) throws Exception 
    {
		System.out.println("Pick your username: ");
		Scanner s = new Scanner(System.in);
		username = s.nextLine();
		
		Socket socket = new Socket("localhost", 600);
		Client client = new Client(socket);
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

		} catch (IOException e) {
			close();
		}

		out.write(username); // sends username to client manager
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
     	try {socket.close();
     	in.close(); out.close();} 
        catch (IOException e1) {}
     	System.exit(0);
     	System.out.println("Server is closed!");
 	}
	 
	
	/*
	 * Returns username
	 */
	 public String getName() {
		 return username;
	 }
	 
	 
}
