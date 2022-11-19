/*
 * Name: Ali Sartaz Khan
 * Course: CSc 335
 * Description: Creates the Client handler for the server to input and output data
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;


public class ClientManager implements Runnable 
    {   
        public static ArrayList<ClientManager> users = new ArrayList<>();
        private Socket socket;
        BufferedWriter out;
        BufferedReader in;
        String username;
        String newUserInput;
        int count = 0;
        
        /*
         * Constructor takes in socket and creates new input and output streams
         * socket: socket
         */
        public ClientManager(Socket socket) {
        	try {
            	this.socket = socket; 
				this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				this.newUserInput = in.readLine();
				username = newUserInput;
				System.out.println(newUserInput);
				users.add(this);
	        	broadcastToOthers("Joined:" +username);
	        	othersBroadcastToYou();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	
        }

        /*
         * Run method
         */
        public void run() {
//        	sq.add(out);
            String msgFromUser = "";
            while (socket.isConnected())
            {
            	try {
            		msgFromUser = in.readLine(); //AVOID THE FIRST IN
	            	System.out.println(msgFromUser);
	            	broadcastToOthers(msgFromUser);
            	} catch(Exception e) {
                    close();  
                } 

            }
        } 
        
        
        /*
         * Method to broadcast string from others to yourself 
         */
        public void othersBroadcastToYou() {
        	for (ClientManager user: users) {
				try {
					if (user.username != this.username) {
						this.out.write("Joined:" +user.username);
						this.out.newLine();
						this.out.flush();}
				}catch (Exception e) {
					close();
				}
				
    		}
        }
        
        /*
         * Method to broadcast string from you to others:
         * msgToOThers: string to broadcast 
         */
        public void broadcastToOthers(String msgToOthers) {
    		for (ClientManager user: users) {
				try {
					if (user.username != this.username) {
						System.out.println("Inside broadcast");
						user.out.write(msgToOthers);
						user.out.newLine();
						user.out.flush();}
				}catch (Exception e) {
					close();
				}
				
    		}
    		}
        
        
        /*
         * Closes server
         */
        public void close() {
         	try {socket.close();
         	in.close(); out.close();} 
            catch (IOException e1) {}
         	System.out.println("Socket is closed!");
         	System.exit(0);
     	}
        
        
        /*
         * String representation of This object
         */
        public String toString() {
        	return username;
        }
    	 
            
    }