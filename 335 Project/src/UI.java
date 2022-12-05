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
import game.GameStatus;
import game.Tile;
import game.TimedMode;
import pieces.Pawn;
import pieces.Piece;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
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
	GameStatus gameStatus;
	boolean initialized = false;
	int SHELL_WIDTH_OFFSET = 20;
	int SHELL_HEIGHT_OFFSET = 50;
	Piece selectedPiece; //Newly added field
	public boolean whitesTurn; //Newly added field
	public static int BOARD_COORD_OFFSET = 100;
	public boolean yourTurn;
	protected Menu menuBar, fileMenu;
	protected MenuItem fileMenuHeader,fileSaveItem,fileExitItem;
	boolean loadOldGame;
	private String fileName;
	private Composite upperComposite;
	private Composite middleComposite;
	private Composite lowerComposite;
	private TimedMode yourTimer;
	private TimedMode opponentsTimer;
	public boolean isOpponentConnected;
	String opponent;
	String username;
	String opponentsPreferedTime;
	
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
		this.fileName = client.getPlayer().getFileName();
		this.username = client.getPlayer().getName();;
		if (in == null || client.getPlayer().getColor().equals("White")) {
			this.whitesTurn = true; //Newly added field
			this.yourTurn = true;
		}
		else {
			this.whitesTurn = false;
			this.yourTurn = false;
		}
		
		isOpponentConnected = false;
	}

	/*
	 * Starts running the UI
	 */
	public void start() {
		
		System.out.println("It is currently whites turn!");
		setup();
		gameStatus = new GameStatus(shell);
		loadOldGame = false;
		validateFileName();
		
		shell.addListener(SWT.Close, new Listener() {

			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				System.out.println("Closing the shell!");
				String fileToSaveGame = gameStatus.getFileName("Do You Wanna Save the Game?", "Yes, please", "No, don't");
				gameStatus.saveGame(boardUI.getBoard(), yourTurn, whitesTurn);
				
				shell.dispose();
				
			}
			
		});
		
		canvas.addPaintListener(e -> {
			if (initialized == false) { // create the tiles and initial starting positions
				
				boolean white;
				
				if (client.getPlayer().getColor().equals("White")) { 
					white = true; }
				
				else { 
					white = false; }
				boardUI.createBoardData(e.gc);
				   
				if(!loadOldGame) {
					boardUI.setAllPieces();
				}else {
					gameStatus.setFileName(fileName);
					boolean[] playerTurnData = gameStatus.loadGame(boardUI.getBoard());
//					yourTurn = playerTurnData[0];
//					whitesTurn = playerTurnData[1];
					
				}
				

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
				if (!yourTurn || yourTimer.isTimerOver()) // thinking here?
					return;
				//Gather data, convert graphical coordinates into chessboard coordinates
				int coordinates[] = boardUI.getBoardIndex(e.x,e.y);
				if(coordinates == null) {
					return;
				}
				int xCoord = coordinates[0];
				int yCoord = coordinates[1];
				//Select a piece OR move piece
				if(selectedPiece == null) { // Selecting piece for first time
					selectedPiece = boardUI.selectPiece(xCoord, yCoord, whitesTurn);
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
							int xCoordBefore = selectedPiece.getX();
							int yCoordBefore = selectedPiece.getY();
							boardUI.updateBoard(xCoord,yCoord,selectedPiece);
							if (in != null) {
								try {
									out.write("MOVE:"+xCoordBefore+"-"+yCoordBefore+"-"+xCoord+"-"+yCoord);
									out.newLine();
									out.flush();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} 
								yourTurn = !yourTurn;
							}
							else {
								whitesTurn = !whitesTurn; }
							
							canvas.redraw();
							selectedPiece.SetNotSelected();
							selectedPiece = null;
							
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
				if (in != null) { notifyOtherUsers(); }
				canvas.redraw();
				} 
			public void keyReleased(KeyEvent e) {}
		});
		
		if (in != null) {
			Runnable runnable = new Runner();
			display.asyncExec(runnable); }
		shell.open(); 
		boolean isJustConnected = true;
		long start = System.currentTimeMillis();
		while (!shell.isDisposed()) 
			if (!display.readAndDispatch()) {
				long end = System.currentTimeMillis();
				int diff = (int) ((end-start)/1000);
				if(diff==1) { // the timers start when both players are connected
					
					if(isOpponentConnected) {
						if(isJustConnected) {
							System.out.println("adding OPPONENT'S TIME ... ");
							if(opponentsTimer == null) {return;}
							opponentsTimer.setTimeLimit(opponentsPreferedTime);
							opponentsTimer.setPlayer(opponent);
							isJustConnected = false;
							}
						
						
						if(yourTurn && yourTimer != null) {
							yourTimer.update();}
						else if(opponentsTimer != null) {opponentsTimer.update();}
						
					}
					
					
					start = end;
				}
			
					
					
					
					
					}
				
				
				display.sleep();

		display.dispose();		
	}
	
	

	void setup() {
		
		
		display = new Display();
		
		shell = new Shell(display);
		shell.setSize(640+BOARD_COORD_OFFSET + SHELL_WIDTH_OFFSET, 800+BOARD_COORD_OFFSET/2 + SHELL_HEIGHT_OFFSET);

		shell.setLayout(new GridLayout());
		defineComposites();
		
		defineYourTimer(this.client.getPlayer().getPreferredTime());
		defineOpponentsTimer("00:00");
		
		if (client != null) {
			shell.setText("Chess: " + client.getPlayer().getName() + " (" + client.getPlayer().getColor() + ")"); }
		else {
			shell.setText("Chess"); }
		
		
		canvas = new Canvas(middleComposite, SWT.FOCUSED);
		canvas.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		canvas.setSize(640+BOARD_COORD_OFFSET, 600+BOARD_COORD_OFFSET/2 + SHELL_HEIGHT_OFFSET);
		

//		canvas.setBounds(0, 0, 640, 640);
		
		createMenuBar();
		
		shell.setMenuBar(menuBar);
//		shell.setLayoutData(new RowLayout());
		boardUI = new Chessboard(canvas, shell);	
		
	}
	
	public void setConnected() {this.isOpponentConnected = true;}

	
	private void defineYourTimer(String time) {
		// TODO Auto-generated method stub
		if(time.contains("M") || time.contains("S")) {return;}
		yourTimer = new TimedMode(shell, lowerComposite);
		yourTimer.setPlayer(username);
		yourTimer.setTimeLimit(time);
		
		
	}
	
	private void defineOpponentsTimer(String time) {
		// TODO Auto-generated method stub
		if(time.contains("M") || time.contains("S")) {return;}
		opponentsTimer = new TimedMode(shell, upperComposite);
		
		
		
	}

	private void defineComposites() {
		// TODO Auto-generated method stub
		RowLayout rowLayout = new RowLayout();
		rowLayout.type = SWT.HORIZONTAL;
		rowLayout.marginLeft = 0;
		rowLayout.marginRight = 0;
		
		
		upperComposite = new Composite(shell, SWT.NO_FOCUS);
		upperComposite.setLayoutData(opponentsTimer);
		upperComposite.setLayout(rowLayout);
		
		middleComposite = new Composite(shell, SWT.NO_FOCUS);
		middleComposite.setLayoutData(canvas);
		
		lowerComposite = new Composite(shell, SWT.NO_FOCUS);
		lowerComposite.setLayoutData(yourTimer);
		lowerComposite.setLayout(rowLayout);
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
	
	/**
	 * Checks if the entered game file exists.
	 * If not, it generates a new game instead.
	 */
	private void validateFileName(){
		if(fileName.equals("Enter .txt file")) {return;}
		String cwd = System.getProperty("user.dir") + "\\Saved Games\\" + fileName;
		 File dir = new File(cwd);
		 if(dir.exists()) {
			 loadOldGame = true;
		 }else {
			 loadOldGame = false;
			 System.out.println("Sorry, the entered file could not be found!");
		 }
		
		
	}
	/*
	 * establishes the menu bar at the top of shell
	 * 
	 */
	protected void createMenuBar() {
		menuBar = new Menu(shell, SWT.BAR);
		createFileMenu(); //file ops
		
	}
	
	/*
	 * creates the file menu
	 */
	protected void createFileMenu() {
		fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		fileMenuHeader.setText("File");
		fileMenu = new Menu(shell, SWT.DROP_DOWN);
		fileMenuHeader.setMenu(fileMenu);
		createFileSaveItem();
		createFileExitItem();
		
		
	}
	
	/*
	 * file save menu.
	 */
	protected void createFileSaveItem() {
		fileSaveItem = new MenuItem(fileMenu, SWT.PUSH);
		fileSaveItem.setText("Save");
		fileSaveItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				
				gameStatus.getFileName("Wanna Take a Break?", " Save and Exit "," Cancel "); //prompts the player for a file name
				gameStatus.saveGame(boardUI.getBoard(), yourTurn, whitesTurn); //saves the game status to a .txt file
				shell.dispose();
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
	}
	
	/*
	 * file exit option. when selected, exits the program
	 */
	protected void createFileExitItem() {
		fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
		fileExitItem.setText("Exit");
		fileExitItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				shell.close();
				display.dispose();
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				shell.close();
				display.dispose();
			}
		});
		
		
	}
	
	
	
	
	class Runner implements Runnable
	{
		public void run() 
		{
			String msgFromOpponent;
			try {
				if (in.ready())
				{
					try {
						msgFromOpponent = in.readLine();
						String[] list = msgFromOpponent.split("[:-]");
						if (msgFromOpponent.contains("MOVE")){
							
							int xBefore = Integer.parseInt(list[1]);
							int yBefore = Integer.parseInt(list[2]);
							int xAfter = Integer.parseInt(list[3]);
							int yAfter = Integer.parseInt(list[4]);
							selectedPiece = boardUI.selectPiece(xBefore, yBefore, !whitesTurn);
							boardUI.updateBoard(xAfter,yAfter,selectedPiece);
//							whitesTurn = !whitesTurn;
							yourTurn = !yourTurn;
							canvas.redraw();
							selectedPiece.SetNotSelected();
							selectedPiece = null;
							System.out.println(msgFromOpponent);
						}else if(msgFromOpponent.contains("PLAYER")) {
							System.out.println(msgFromOpponent);
							opponent = list[3];
							opponentsPreferedTime = list[4];
							opponentsPreferedTime += (":" + list[5]);
							isOpponentConnected = true;}
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