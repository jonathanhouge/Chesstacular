/**
 * this class enables the user to save the current game to a text file and also 
 * allows loading an old game.
 * 
 * 
 * @author Khojiakbar Yokubjonov 
 */
package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.util.Scanner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

public class GameStatus {
	
	Shell parent;
	String fileName;
	public GameStatus(Shell shell) {
		this.parent = shell;
		fileName = "";
	}
	
	/*
	 * Creates a display that prompts the player for a file name
	 * Returns the file name entered by the player
	 * 
	 */
	public String getFileName() {
		 final Shell shell = new Shell(parent, SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
		    shell.setText("Do You Wanna Take a Break?");

		    GridLayout layout = new GridLayout(2, true);
		    Color shellColor = new Color(255, 221, 153);
		    layout.marginWidth = 20;
		    shell.setLayout(layout);
		    shell.setSize(350, 200);
		    shell.setBackground(shellColor);
		    
		    Label label = new Label(shell, SWT.NULL);
		    label.setText("Enter File Name Here:");
		    label.setLayoutData(new GridData(SWT.TOP, SWT.CENTER,true,true,1,1));
		    label.setBackground(new Color(255, 221, 153));

		    final Text text = new Text(shell, SWT.SINGLE | SWT.BORDER);
		    Color buttonColor = new Color(204, 136, 0);
		    final Button saveButton = new Button(shell, SWT.PUSH);
		    saveButton.setText("Save and Exit");
		    saveButton.setLayoutData(new GridData(SWT.END, 1,false,false));
		    saveButton.setBackground(buttonColor);
		    
		    text.addListener(SWT.Modify, new Listener() {
			      public void handleEvent(Event event) {
			        try {
			          fileName = text.getText();
			          saveButton.setEnabled(true);
			        } catch (Exception e) {
			          saveButton.setEnabled(false);
			        }
			      }

				
			    });
		    Button cancelButton = new Button(shell, SWT.PUSH);
		    cancelButton.setText("Cancel");
		    cancelButton.setBackground(buttonColor);
		    
		    saveButton.addListener(SWT.Selection, new Listener() {
			      public void handleEvent(Event event) {
			    	if(fileName.equals(".txt") || fileName.equals("")) {return;}
			        shell.dispose();
			        parent.dispose();
			      }
			    });

			    cancelButton.addListener(SWT.Selection, new Listener() {
			      public void handleEvent(Event event) {
			        shell.close();
			      }
			    });
			    
			    shell.addListener(SWT.Traverse, new Listener() {
			      public void handleEvent(Event event) {
			        if(event.detail == SWT.TRAVERSE_ESCAPE)
			          event.doit = false;
			      }
			    });
		    text.setText("");
		    shell.open();
	    
	    Display display = parent.getDisplay();
	    while (!parent.isDisposed()) {
	      if (!display.readAndDispatch())
	        display.sleep();
	    }
	    
	    parent.dispose();
	    
		return fileName;
		
		
	}
	
	/*
	 * Prompts the player for a file name to save the game when he/she tries to exit
	 * Returns the file name entered by the player
	 */
	public String promptsFileWhileExiting() {
		final Shell shell = new Shell(parent, SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
	    shell.setText("Do You Wanna Save the Game?");

	    GridLayout layout = new GridLayout(2, true);
	    Color shellColor = new Color(255, 221, 153);
	    layout.marginWidth = 20;
	    shell.setLayout(layout);
	    shell.setSize(350, 200);
	    shell.setBackground(shellColor);
	    
	    Label label = new Label(shell, SWT.NULL);
	    label.setText("Enter File Name Here:");
	    label.setLayoutData(new GridData(SWT.TOP, SWT.CENTER,true,true,1,1));
	    label.setBackground(new Color(255, 221, 153));

	    final Text text = new Text(shell, SWT.SINGLE | SWT.BORDER);
	    Color buttonColor = new Color(204, 136, 0);
	    final Button saveButton = new Button(shell, SWT.PUSH);
	    saveButton.setText("Yes");
	    saveButton.setLayoutData(new GridData(SWT.END, 1,false,false));
	    saveButton.setBackground(buttonColor);
	    
	    text.addListener(SWT.Modify, new Listener() {
		      public void handleEvent(Event event) {
		        try {
		          fileName = text.getText();
		          saveButton.setEnabled(true);
		        } catch (Exception e) {
		          saveButton.setEnabled(false);
		        }
		      }

			
		    });
	    Button cancelButton = new Button(shell, SWT.PUSH);
	    cancelButton.setText("No");
	    cancelButton.setBackground(buttonColor);
	    
	    saveButton.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event event) {
		    	if(fileName.equals(".txt") || fileName.equals("")) {return;}
		        shell.dispose();
		      }
		    });

		    cancelButton.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event event) {
		        shell.dispose();
		      }
		    });
		    
		    shell.addListener(SWT.Traverse, new Listener() {
		      public void handleEvent(Event event) {
		        if(event.detail == SWT.TRAVERSE_ESCAPE)
		          event.doit = false;
		      }
		    });
	    text.setText("");
	    shell.open();
    
    Display display = parent.getDisplay();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    
    shell.dispose();
    
	return fileName;
	
	
	}
	
	/*
	 * Accepts a .txt file containing board data.
	 * Writes the chess board data to a .txt file specified by the player.
	 * FILE FORMAT:
	 * 		COLOR PIECE (X,Y)
	 * EXAMPLE:
	 * 		BLACK PAWN (1,1)
	 */
	public void saveGame(Tile[][]board, boolean yourTurn, boolean whitesTurn) {
		if(fileName.length() == 0 || !fileName.contains(".txt")) {return;}
		checkForSavedGamesFolder();
		PrintWriter writer;
		
		try {
			writer = new PrintWriter("Saved Games/" + fileName);
			
			// storing the player turn info
			// so that when the game is loaded, the program knows which  player
			// will play first.
			writer.write("yourTurn " + yourTurn + "\n");
			writer.write("whitesTurn " + whitesTurn + "\n");
			for(int y=0; y<8; y++) {
				for(int x=0; x<8; x++) {
					Tile t = board[y][x];
					Piece piece = t.getPiece();
					if(piece != null) {
						writer.write(piece + "\n");
					}
				}
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/*
	 * Accepts a .txt file containing board data.
	 * Loads the pieces to the 2D board based on the file data
	 * FILE FORMAT:
	 * 		COLOR PIECE (X,Y)
	 * EXAMPLE:
	 * 		BLACK PAWN (1,1)
	 */
	public boolean[] loadGame(Tile[][] board) {
		File gameFile; Scanner reader;
		if(fileName.length() == 0 || !fileName.contains(".txt")) {return null;}
		gameFile = new File("Saved Games/" + fileName);
		boolean yourTurn,whitesTurn;
		
		try {
			yourTurn = false; whitesTurn = false;
			reader = new Scanner(gameFile);
			
			// extracting player turn info. it will be used to identify
			// whose turn it was to play right before exiting the game.
			String turnData1 =  reader.nextLine().replace("\n", "").split(" ")[1];
			String turnData2 =  reader.nextLine().replace("\n","").split(" ")[1];
			if(turnData1.equals("true")) {yourTurn = true;}
			if(turnData2.equals("true")) {whitesTurn = true;}
			
			while(reader.hasNextLine()) {
				String pieceInfo = reader.nextLine();
				pieceInfo = pieceInfo.replace("\n", "");
				addPieceToBoard(board, pieceInfo);
			}
			
			reader.close();
			return new boolean[] {yourTurn, whitesTurn};
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
			
		
	}
	
	public void setFileName(String name) {this.fileName = name;}

	/*
	 * Adds a piece to the 2D chess board
	 * Accepts a 2D board and piece information
	 */
	private void addPieceToBoard(Tile[][] board, String pieceInfo) {
		String[]  pieceData = pieceInfo.split(" ");
		String color = pieceData[0]; String piece = pieceData[1];
		String coord = pieceData[2]; 
		coord = coord.replace("(", "").replace(")", "");
		String[] pCoord = coord.split(",");
		int xCoord = Integer.parseInt(pCoord[0]);
		int yCoord = Integer.parseInt(pCoord[1]);
		Piece p = createPiece(piece, color); p.updateLocation(xCoord, yCoord);
		board[yCoord][xCoord].setPiece(p);
		
		
		
	}
	
	/**
	 * Checks the current directory to see if there is a "Saved Games" folder
	 * It will create the folder if it doesn't exit.
	 * "Saved Games" folder is used to store the saved games.
	 */
	private void checkForSavedGamesFolder() {
//		System.out.println("Checking the directory ...");
		String cwd = System.getProperty("user.dir") + "\\Saved Games";
		 File dir = new File(cwd);
		 
		 if(!dir.exists()) {
			 System.out.println("Creating Saved Games Folder...");
			 File savedGames = new File("Saved Games");
			 savedGames.mkdir();
		 }
		 
	}

	
	/*
	 * Creates a chess piece based on the given piece name
	 * Returns null if it takes an invalid piece name
	 */
	private Piece createPiece(String pName, String color) {
		// TODO Auto-generated method stub
		boolean white = true;
		if(color.equals("BLACK")) {white = false;}
		
		if(pName.equals("BISHOP")) {return new Bishop(white, parent);}
		if(pName.equals("KING")) {return new King(white, parent);}
		if(pName.equals("KNIGHT")) {return new Knight(white, parent);}
		if(pName.equals("PAWN")) {return new Pawn(white, parent);}
		if(pName.equals("QUEEN")) {return new Queen(white, parent);}
		if(pName.equals("ROOK")) {return new Rook(white, parent);}
		
		System.out.println("Invalid Piece Name!");
		return null;
		
	}
		

}
