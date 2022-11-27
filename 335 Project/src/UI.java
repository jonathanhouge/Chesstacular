/*
 * Name: Ali Sartaz Khan
 * Course: CSc 335
 * Description: Creates the UI for the client
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UI {

	private Canvas canvas;
	private Display display;
	private Shell shell;
	
	private BufferedWriter out;
	private BufferedReader in;
	
	Socket socket;

	Client client;
	ChessBoard boardUI;
	boolean initialized = false;
	int SHELL_WIDTH_OFFSET = 20;
	int SHELL_HEIGHT_OFFSET = 50;
	/*
	 * Constructor that assigns values
	 * client: client object
	 * in: input stream
	 * out: output stream
	 * socket: Socket object
	 */
	public UI(Client client, BufferedReader in, BufferedWriter out, Socket socket) {
		this.in = in;
		this.out = out;
		this.socket = socket;
		this.client = client;
	}

	/*
	 * Starts running the UI
	 */
	public void start() {
		setup();
		canvas.addPaintListener(e -> {
			if (initialized == false) { // create the tiles and initial starting positions
				boolean white;
				//System.out.println(client.getPlayer().getColor());
				if (client.getPlayer().getColor() == "White") { white = true; }
				else { white = false; }

				boardUI.createBoardData(e.gc, white);
				initialized = true; }
			
			if (initialized) { boardUI.draw(e.gc); }
		}
		);	

		canvas.addMouseListener(new MouseListener() {
			public void mouseDown(MouseEvent e) {
				System.out.println(e.x + ":" + e.y);
				boardUI.mouseClickUpdate(e.x, e.y);
				canvas.redraw();
				
			} 
			public void mouseUp(MouseEvent e) {} 
			public void mouseDoubleClick(MouseEvent e) {} 
		});

		canvas.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				notifyOtherUsers();
				canvas.redraw();
				} 
			public void keyReleased(KeyEvent e) {}
		});

		Runnable runnable = new Runner();
		display.asyncExec(runnable);
		shell.open(); 
		while (!shell.isDisposed()) 
			if (!display.readAndDispatch())
				display.sleep();

		display.dispose();		
	}
	
	void setup() {
		display = new Display();
		
		shell = new Shell(display);
		shell.setText("Chess (" + client.getPlayer().getName() + ")");
		shell.setLayout(new FillLayout());
		shell.setSize(640 + SHELL_WIDTH_OFFSET, 640 + SHELL_HEIGHT_OFFSET);
		
		canvas = new Canvas(shell, SWT.BACKGROUND);
		canvas.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		canvas.setSize(640, 640 + SHELL_HEIGHT_OFFSET);
//		canvas.setBounds(0, 0, 640, 640);
		
		boardUI = new ChessBoard(canvas, shell);	
	}
	
	void notifyOtherUsers() {
		
		try {
			out.write(" " + client.getPlayer().getName() + ": I pressed a key hehe!");
			out.newLine();
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	class Runner implements Runnable
	{
		public void run() 
		{
			String msgFromOthers;
			try {
				if (in.ready())
				{
					try {
						msgFromOthers = in.readLine();
						
						if (msgFromOthers.contains("ID")) { // your own ID
							System.out.println("My INFO: " + msgFromOthers);
							String[] list = msgFromOthers.split("[:-]");
							int ID = Integer.parseInt(list[1]);
							String color = list[2];
							client.getPlayer().setID(ID);
							client.getPlayer().setColor(color);	
						}
						
						else 
							System.out.println("OPPOSITION: " + msgFromOthers);
						}
							
					 catch(IOException e) {
						client.close();
					}
					
				}
			}
			catch(IOException ex) {
				System.out.println("The server did not respond (async).");
			}				
            display.timerExec(0, this);
		}
	}	
	
	
	
	
}