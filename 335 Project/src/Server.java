/**
 * @author Ali Sartaz Khan
 * Course: CSc 335
 * Description: Creates the Server class consisting of client handlers
 */


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.net.InetAddress;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * When a client connects, a new thread is started to handle it.
 */
public class Server 
{
	private ServerSocket serverSocket;
    public Server(ServerSocket serverSocket) {
    	this.serverSocket = serverSocket;
    }
    
    public static void main(String[] args) throws IOException {
    	ServerSocket serverSocket = new ServerSocket(600);
    	Server server = new Server(serverSocket);
    	server.start();
    }
    
    /**
     * Starts server class
     */
    public void start() {
    	System.out.println("Server has started");
    	try {
    		while(!serverSocket.isClosed()) {
    			Socket socket = serverSocket.accept();
    			System.out.println("A new User is connected!");
    			ClientManager clientManager = new ClientManager(socket);
    			Thread thread = new Thread(clientManager);
    			thread.start();
    		}
    	} catch(IOException e) {
    		
    	}
    	
    }
    
    /**
     * Closes server object
     */
    public void closeServer() {
    	try {
    		if(serverSocket != null) {
    			serverSocket.close();
    		}
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    	System.out.println("Server is Closed!");
    }
       
}