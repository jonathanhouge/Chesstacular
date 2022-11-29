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

import game.Chessboard;
import pieces.Pawn;
import pieces.Piece;

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
	Chessboard boardUI;
	boolean initialized = false;
	int SHELL_WIDTH_OFFSET = 20;
	int SHELL_HEIGHT_OFFSET = 50;
	Piece selectedPiece; //Newly added field
	public boolean whitesTurn; //Newly added field
	public static int BOARD_COORD_OFFSET = 100;

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
		this.whitesTurn = true; //Newly added field
	}

	/*
	 * Starts running the UI
	 */
	public void start() {
		System.out.println("It is currently whites turn!");
		setup();
		canvas.addPaintListener(e -> {
			if (initialized == false) { // create the tiles and initial starting positions
				
				boolean white;
				
				if (client.getPlayer().getColor().equals("White")) { 
					System.out.println("Is White!");
					white = true; }
				
				else { 
					System.out.println("Is not White!");
					white = false; }

				boardUI.createBoardData(e.gc, white);
				initialized = true; }
			
			if (initialized) { boardUI.draw(e.gc); 
				if(whitesTurn) {
					System.out.println("It is currently whites turn!");
				}else {
					System.out.println("It is currently blacks turn!");
				}
			}
		}
		);	

		canvas.addMouseListener(new MouseListener() {
			public void mouseDown(MouseEvent e) {
				//Gather data, convert graphical coordinates into chessboard coordinates
				int coordinates[] = boardUI.getBoardIndex(e.x,e.y);
				if(coordinates == null) {
					return;
				}
				int xCoord = coordinates[0];
				int yCoord = coordinates[1];				
				//Select a piece OR move piece
				if(selectedPiece == null) { // Selecting piece for first time
					selectedPiece = boardUI.selectPiece(xCoord,  yCoord,whitesTurn);
					if(selectedPiece != null) {
						selectedPiece.setSelected();
						canvas.redraw();
					}
				}else {// Determine if move being made or selecting new piece
					Piece possibleSelection;
					possibleSelection = boardUI.selectPiece(xCoord, yCoord,whitesTurn);
					if (possibleSelection !=null) {// player has selected new piece

						selectedPiece.SetNotSelected();
						selectedPiece = possibleSelection;
						selectedPiece.setSelected();
						
						System.out.println("UI - SELECTED NEW PIECE: " + selectedPiece);
						canvas.redraw();
					}else {// player may have moved onto empty space or onto enemy
						if(boardUI.validMoveMade(xCoord,yCoord,selectedPiece,whitesTurn)) {
							boardUI.updateBoard(xCoord,yCoord,selectedPiece);
							whitesTurn = !whitesTurn;
							canvas.redraw();
							selectedPiece.SetNotSelected();
							
						}else {
							System.out.println("UI - INVALID MOVE MADE!");
						}
					}
					
				}
				
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
		shell.setSize(640+BOARD_COORD_OFFSET + SHELL_WIDTH_OFFSET, 640+BOARD_COORD_OFFSET/2 + SHELL_HEIGHT_OFFSET);
		
		canvas = new Canvas(shell, SWT.BACKGROUND);
		canvas.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		canvas.setSize(640+BOARD_COORD_OFFSET, 640+BOARD_COORD_OFFSET/2 + SHELL_HEIGHT_OFFSET);
//		canvas.setBounds(0, 0, 640, 640);
		
		boardUI = new Chessboard(canvas, shell);	
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
						
//						if (msgFromOthers.contains("ID")) { // your own ID
//							System.out.println("My INFO: " + msgFromOthers);
//							String[] list = msgFromOthers.split("[:-]");
//							int ID = Integer.parseInt(list[1]);
//							String color = list[2];
//							client.getPlayer().setID(ID);
//							client.getPlayer().setColor(color);	
//						}
//						
//						else 
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