package game;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import displays.PawnPromotionDisplay;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

/** 
 * Chessboard implementation. Holds onto a list of Tiles that represent the
 * tiles in the 8x8 board. These tiles hold onto the pieces that occupy them. 
 * Draws itself by iterating through a this list and calling their draw methods.
 * Has a multitude of methods for different kinds of moves, standard and 
 * special cases.
 * 
 * @author Jonathan Houge & Julius Ramirez & Khojiakbar Yokubjonov
 */
public class Chessboard implements ChessboardUI {
	Canvas canvas;
	Shell shell;
	Display display;
	
	Tile[][] board;
	int SQUARE_WIDTH = 80;
	int BOARD_COORD_OFFSET = 100;
	String verticalCoords[] = new String[] { "8", "7", "6", "5", "4", "3", "2", "1" };
	String horizontalCoords[] = new String[] { "A", "B", "C", "D", "E", "F", "G", "H" };
	
	boolean promotion = false;
	public boolean endGame = false;

	List<Coordinate> selectedCoordinates;
	
	Color SELECTED = new Color(51, 204, 51);
	Color BG = new Color(255, 255, 255);
	Color SIDE = new Color(204, 136, 0);
	Color HIGHLIGHTED = new Color(110, 211, 255);
	Color OUTLINE;
	Font boardFont;
	
	/** 
	 * Chessboard constructor. Takes in the needed SWT assets for
	 * everything that needs to be done. Utilizes the display to
	 * get the color needed for outlining and the font needed
	 * for the board coordinates.
	 * 
	 * @param canvas: UI's canvas
	 * @param shell: UI's shell.
	 * @param display: UI's display
	 */
	public Chessboard(Canvas canvas, Shell shell, Display display) {
		this.canvas = canvas;
		this.shell = shell;
		this.display = display;
		this.OUTLINE = display.getSystemColor(SWT.COLOR_BLACK);
		this.boardFont = new Font(canvas.getDisplay(), "Tahoma", 15, SWT.BOLD); }
	
	/**
	 * Creation method, only ran once for initialization. Makes an empty board,
	 * creating the tiles themselves by utilizing the Tile class.
	 * 
	 * @param gc: event gc, let's us draw on the canvas
	 */
	public void createBoardData(GC gc) {
		board = new Tile[8][8]; // list of tiles
		boolean boardColor = true; // if true, tile is white and vice versa
		Color w = new Color(255, 221, 153);
		Color b = new Color(204, 136, 0); // tile colors, white and black respectively

		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {

				// create the tiles with the proper color
				if (boardColor) {
					board[y][x] = new Tile(w, OUTLINE, x * SQUARE_WIDTH + 50, y * SQUARE_WIDTH, SQUARE_WIDTH); } 
				else {
					board[y][x] = new Tile(b, OUTLINE, x * SQUARE_WIDTH + 50, y * SQUARE_WIDTH, SQUARE_WIDTH); }
				
				// alternating tile colors
				boardColor = !boardColor; }
			boardColor = !boardColor; }
	}
	
	/**
	 * Adds all the chess pieces to the board.
	 * Used for the new game only.
	 */
	public void setAllPieces() {
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				board[y][x].setPiece(makePiece(x, y)); // set the piece using a helper function
			} }
	}

	/**
	 * Helper function responsible for making the pieces and putting them in their
	 * starting positions.
	 * 
	 * @param x: row in board
	 * @param y: column in board
	 * @return Piece that was made
	 */
	private Piece makePiece(int x, int y) {

		// false == black
		boolean pieceColor = false;

		if (y == 0) { // black specialty pieces
			if (x == 0 || x == 7) { return (new Rook(pieceColor, shell)); }
			else if (x == 0 || x == 7) { return (new Rook(pieceColor, shell)); } 
			else if (x == 1 || x == 6) { return (new Knight(pieceColor, shell)); } 
			else if (x == 2 || x == 5) { return (new Bishop(pieceColor, shell)); } 
			else if (x == 4) { return (new King(pieceColor, shell)); } 
			else if (x == 3) { return (new Queen(pieceColor, shell)); } } 
		else if (y == 1) { return (new Pawn(pieceColor, shell)); } // black pawns

		// true == white
		pieceColor = !pieceColor;

		if (y == 6) { return (new Pawn(pieceColor, shell)); } // white pawns
		else if (y == 7) { // white specialty pieces
			if (x == 0 || x == 7) { return (new Rook(pieceColor, shell)); }
			else if (x == 0 || x == 7) { return (new Rook(pieceColor, shell)); } 
			else if (x == 1 || x == 6) { return (new Knight(pieceColor, shell)); }
			else if (x == 2 || x == 5) { return (new Bishop(pieceColor, shell)); }
			else if (x == 4) { return (new King(pieceColor, shell)); } 
			else if (x == 3) { return (new Queen(pieceColor, shell)); } }

		return null; // tile is given no piece
	}

	@Override
	/**
	 * The for loop to draw the board. The helper function 'Drawing' holds
	 * onto all of the conditionals needed in the process. 
	 * 
	 * @param x: row in board
	 * @param y: column in board
	 * @param gc: event gc, let's us draw on the canvas
	 */
	public void draw(GC gc) {
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				drawing(x, y, gc); } }
	}
	
	/**
	 * The helper function 'Drawing'. Need to make sure that tiles
	 * are drawn the right color: if a piece is currently selected
	 * the tile is green, a tile is cyan if it's a valid place for 
	 * the currently selected piece to move, and if neither of these
	 * are true, the original color stands. Also adds the board
	 * coordinates on the side (A-H, 1-8). 
	 * 
	 * @param x: row in board
	 * @param y: column in board
	 * @param gc: event gc, let's us draw on the canvas
	 */
	private void drawing(int x, int y, GC gc) {
		Tile t = board[x][y]; // tile in question
		Piece p = t.getPiece(); // piece in question
		
		if (p != null) { // if there's a piece, could be selected or taken by selected
			if(p.isSelected()) { t.draw(gc, SELECTED); }
			
			else if(selectedCoordinates!= null && selectedCoordinates.contains(new Coordinate(y,x))){
				t.draw(gc, HIGHLIGHTED); }
			
			else{ t.draw(gc); } 
		}
		
		else { // no piece but could be a valid place for currently selected to move to
			if(selectedCoordinates!= null && selectedCoordinates.contains(new Coordinate(y,x))){
				t.draw(gc, HIGHLIGHTED); }
			else { t.draw(gc); }
		}
		
		// adding the board coordinates
		if (x == 0) { addBoardCoords(gc, x, y); }
		if (y == 7) { addBoardCoords(gc, x, y); }
	}
	
	/**
	 * The helper function of 'Drawing'. Makes sure the board coordinates
	 * of A-H and 1-8 are drawn in the appropriate locations.
	 * 
	 * @param x: row in board
	 * @param y: column in board
	 * @param gc: event gc, let's us draw on the canvas
	 */
	private void addBoardCoords(GC gc, int x, int y) {
		gc.setFont(boardFont);
		gc.setBackground(BG);
		gc.setForeground(SIDE);
		
		// 1-8
		if (x == 0) {
			String yCoord = verticalCoords[y];
			gc.drawText(yCoord, x * SQUARE_WIDTH + 20, y * SQUARE_WIDTH + 25); }
		
		// A-H
		if (y == 7) {
			String xCoord = horizontalCoords[x];
			gc.drawText(xCoord, x * SQUARE_WIDTH + 60 + 20, (y + 1) * SQUARE_WIDTH + 15); }
	}
	
	//-- getters
	
	/**
	 * accepts the pixel coordinates and converts them to valid index on the board
	 * @param x,y  - the pixel coordinates on the canvas
	 * @return
	 */
	public int[] getBoardIndex(float x, float y) {
		x -= BOARD_COORD_OFFSET / 2;
		float indexX = x / SQUARE_WIDTH;
		float indexY = y / SQUARE_WIDTH;

		int indX = (int) indexX;
		int indY = (int) indexY;

		if (indX < 0 || indX > 7 || indY < 0 || indY > 7 || indexX < 0) {
			System.out.println("Clicking outside the board!");
			return null; }
		return new int[] { indX, indY };
	}
	
	/**
	 * board getter. returns the board.
	 * 
	 * @return board: the list of tiles that make up the board.
	 */
	public Tile[][] getBoard(){ return board; }

	//-- moving / piece methods
	
	/**
	 * This method returns a specific colored king piece so that it may be used for check and
	 * checkmate calculations. It could be improved by removing the need to iterate
	 * through the entire board every time.
	 * 
	 * @param getWhite, true if obtaining white king, false if obtaining black king.
	 * @return the desired King piece.
	 */
	public King getKing(boolean getWhite) {
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (board[row][col].getPiece() instanceof King) {
					if (getWhite) {
						if (board[row][col].getPiece().isWhite()) {
							return (King) board[row][col].getPiece();
						}
					} else {
						if (!board[row][col].getPiece().isWhite()) {
							return (King) board[row][col].getPiece();
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * This method returns a boolean which indicates if the move to the desired x/y
	 * coordinate is a legal move that the piece can made.
	 * 
	 * @param x     any integer between 0-7 inclusive
	 * @param y     any integer between 0-7 inclusive

	 * @param piece the Piece object that is to be moved
	 * @param whitesTurn true if white is making the move, false if not
	 * @return true if the move is legal, false if not.
	 * @author Julius Ramirez
	 */
	public boolean validMoveMade(int x, int y, Piece piece,boolean whitesTurn) {
		return piece.validMove(x, y, board) && validCheckMove(x, y, piece, whitesTurn);

	}

	public boolean newValidMoveMade(int x, int y, Piece piece) {
		return selectedCoordinates.contains(new Coordinate(x,y));
		
	}
	/**
	 * This method updates the piece's coordinates to the new x/y coordinate and
	 * ensures that the old tilies piece field is set to null.
	 * 
	 * @param x     any integer between 0-7 inclusive.
	 * @param y     any integer between 0-7 inclusive.
	 * @param piece the Piece object that is to be moved
	 * @author Julius Ramirez
	 */
	public void movePiece(int x, int y, Piece piece) {
		board[piece.getY()][piece.getX()].setPiece(null);
		System.out.println("MOVING" + piece + " to " + x  + ',' + y);
		piece.updateLocation(x, y); // necessary so that pawn enPassant/castling is updated
		if (board[y][x].getPiece() != null)
			board[y][x].getPiece().killPiece(); // killing previous piece for robot class
		board[y][x].setPiece(piece);
	
	}

	/**
	 * This method is responsible for returning a piece (or null) that the player
	 * wants to move.
	 * 
	 * @param x any integer between 0-7 inclusive
	 * @param y any integer between 0-7 inclusive
	 * @param whitesTurn true if white is making the move, false if not
	 * @param getWhite, true if obtaining white king, false if obtaining black king.
	 * @return true if the movement does not put/have the king in check, false if it does.
	 * @return
	 */
	public boolean validCheckMove(int x, int y, Piece piece, boolean getWhite) {
		Pawn p = null;
		boolean booleanToSave = false;
		boolean extra2 = false;
		boolean extra3 = false;
		boolean isDead = false;
		Rook r = null;
		King k = null;
		// Saving variables that would be overwritten due to move being made
		if(piece instanceof Pawn) {
			p = (Pawn) piece;
			booleanToSave = p.firstMove;
			extra2 = p.enPassantable;
			extra3 = p.didEnPassant;
		}else if(piece instanceof Rook) {
			r = (Rook) piece;
			booleanToSave = r.moved;
		}else if(piece instanceof King) { 
			k = (King) piece;
			booleanToSave = k.moved;
			extra2 = k.castlingMoveMade;
		}
		int oldX = piece.getX();
		int oldY = piece.getY();
		Piece oldPiece = board[y][x].getPiece(); 
		if(oldPiece != null) {
			isDead = oldPiece.getAliveStatus();
		}
		// Simulate a move
		this.movePiece(x, y, piece);
		determineKingCheckStatus(getWhite);
		King newKing = getKing(getWhite);
		boolean checked = newKing.checked;
		// Restoring board to previous state
		this.movePiece(oldX, oldY, piece);
		if(piece instanceof Pawn) {
			p.firstMove = booleanToSave;
			p.enPassantable=extra2;
			p.didEnPassant = extra3; // May not be necessary but saving in case
			board[oldY][oldX].setPiece(p); 
		}else if(piece instanceof Rook) {
			r.moved = booleanToSave;
			board[oldY][oldX].setPiece(r);
		}else if(piece instanceof King) {
			k.moved = booleanToSave;
			k.castlingMoveMade = extra2;
			board[oldY][oldX].setPiece(k); // prevent's king's moved from being overwritten
		}
		if(oldPiece != null) {
			oldPiece.setAliveStatus(isDead);
		}
		board[y][x].setPiece(oldPiece); 
		determineKingCheckStatus(getWhite);
		return !checked;
	}

	/**
	 * The purpose of this method is to determine if a king is now in check or not.
	 * The player's color is passed so that only the desired player's king is analyzed.
	 * 
	 * @param getWhite, true if player is white, false if not
	 */
	public boolean determineKingCheckStatus(boolean getWhite) { // made public for robot
		King king = getKing(getWhite);
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (board[row][col].getPiece() != null
						&& !(board[row][col].getPiece() instanceof King) && board[row][col].getPiece().validMove(king.getX(), king.getY(), board)) {
					king.inCheck();
					//System.out.println("Oh no! The " + king + " is in check because of the " +board[row][col].getPiece());
					board[king.getY()][king.getX()].setPiece(king);
					return true;
				}
			}
		}
		king.checkEvaded();
		board[king.getY()][king.getX()].setPiece(king);
		return false;
	}

	/**
	 * This method is responsible for returning a piece (or null) that the player
	 * wants to move.
	 * 
	 * @param x        any integer between 0-7 inclusive.
	 * @param y        any integer between 0-7 inclusive.
	 * @param isWhite, true if player is white, false if not
	 * @return a piece object if the piece can be selected by the player, null if
	 *         empty/opponents piece.
	 * @author Julius Ramirez
	 */
	public Piece selectPiece(int x, int y, boolean playerIsWhite) {;
		// this if also ensures the player selects one of their own pieces to move
		if (this.board[y][x].getPiece() != null
				&& this.board[y][x].getPiece().isWhite() == playerIsWhite) {
			return this.board[y][x].getPiece();
		}
		return null;
	}

	
	/**
	 * This is a debug version of selectPiece(). It acts the same however the user
	 * can also move the opponents pieces.
	 * 
	 * @param x           any integer between 0-7 inclusive.
	 * @param y           any integer between 0-7 inclusive.
	 * @return a piece object if the piece can be selected by the player, null if
	 *         empty/opponents piece.
	 * @author Julius Ramirez
	 */
	public Piece selectPiece(int x, int y) {
		if (this.board[y][x].getPiece() != null) {
			return this.board[y][x].getPiece();

		}
		return null;

	}

	/**
	 * This simply updates the Tile[][] array so that the piece field is null.
	 * @param xCoord any integer between 0-7 inclusive.
	 * @param yCoord any integer between 0-7 inclusive.
	 */
	public void removePiece(int xCoord, int yCoord) {
		this.board[yCoord][xCoord].setPiece(null); }
	
	/**
	 * This method is responsible for updating the board accordingly after a valid
	 * move is made. 
	 * 
	 * @param xCoord any integer between 0-7 inclusive.
	 * @param yCoord any integer between 0-7 inclusive.
	 * @param selectedPiece the piece that is to be moved to xCoord, yCoord
	 */
	public int updateBoard(int xCoord, int yCoord, Piece selectedPiece) {
		int gameOver = 0;
		//System.out.println("Chessboard.java - Moving..." + selectedPiece);
		this.movePiece(xCoord,yCoord,selectedPiece);
		//System.out.println("Chessboard.java - Piece updated! " + selectedPiece);
		this.checkCastleMoveMade(selectedPiece);
		this.checkEnPassantMoveMade(selectedPiece);
		this.checkPromotion(selectedPiece);
		if(this.determineKingCheckStatus(!selectedPiece.isWhite())) {
			System.out.println("King in check!");
			gameOver = this.determineCheckMate(selectedPiece);
		};
		return gameOver;
	}
	/**
	 * This method checks if an en passant move has been made so that the board may
	 * be updated accordingly.
	 * 
	 * @param selectedPiece the piece that was moved to a new spot.
	 */
	private void checkEnPassantMoveMade(Piece selectedPiece) {
		if(selectedPiece instanceof Pawn) {
			Pawn pawn = (Pawn)selectedPiece;
			if (pawn.didEnPassant) {
				//System.out.println("Chessboard - Entered block!");
				int x = pawn.getX();
				int y = pawn.getY();
				if(pawn.isWhite()) {
					y++;
				}else {
					y--;
				}
				this.removePiece(selectedPiece.getX(),y);
				pawn.removeEnPassantMove();
			}
		}
	}
	private void checkCastleMoveMade(Piece piece) {
		if(piece instanceof King) {
			King k = (King) piece;
			if(k.castlingMoveMade) {
				this.removePiece(k.getX(),k.getY());
				King newKing = new King(piece.isWhite(), shell);
				newKing.moved = true;
				Rook newRook = new Rook(piece.isWhite(), shell);
				newRook.moved = true;
				if(k.getX() == 7 && k.getY() == 7) {
					board[7][5].setPiece(newRook);
					board[7][6].setPiece(newKing);
				}else if(k.getX() == 0 && k.getY() ==7) {
					board[7][3].setPiece(newRook);
					board[7][2].setPiece(newKing);
				}else if(k.getX() == 0 && k.getY() == 0) {
					board[0][3].setPiece(newRook);
					board[0][2].setPiece(newKing);
				}else if (k.getX() == 7 && k.getY() == 0) {
					board[0][5].setPiece(newRook);
					board[0][6].setPiece(newKing);
				}else {
					System.out.println("CHESSBOARD.JAVA - CHECKCASTLEMOVEMADE - this block should never be reached: " + k);
				}
				k.castlingMoveMade = false;
			}
		}
	}
	
	

	/**
	 * This method determines if the game is over.
	 * [made public for robot]
	 * 
	 * @param movedPiece, the piece that was moved this turn. Used to determine next player's color,
	 * could be changed
	 */
	public int determineCheckMate(Piece movedPiece) {
		for(int row = 0; row <= 7; row++) {
			for(int col = 0;col <= 7; col++) {
				if(board[row][col].getPiece()!= null) { 
					// if piece is at this coord AND the piece belongs to the opponent,
					// check if the opponent can make any valid moves.
					if (board[row][col].getPiece().isWhite() != movedPiece.isWhite()) {
						Piece friendlyPiece = board[row][col].getPiece();
						List<Coordinate> friendlyMoves = friendlyPiece.generateMoves(board);
						for(Coordinate coord: friendlyMoves) {
							System.out.println(friendlyPiece + ": " + coord);
							if(validMoveMade(coord.getX(),coord.getY(),friendlyPiece, friendlyPiece.isWhite())) {
								System.out.println("Chessboard.java - validMove possible, checkmate not met");
								return 0;
							}
						}
					}
				}
			}
		}
		//If this part is reached no validMoveMade returned true, thus game over!
		System.out.println("Chessboard.java - The opponent can not make any valid moves! Game over!");
		
		this.endGame = true;
		if (movedPiece.getColor() == "White") { return 1; }
		else { return 2; }
	}

	/**
	 * This method promotes the pawn TODO to a queen if it is on the opposite side of the board.
	 * @param selectedPiece the piece that has been moved.
	 */
	private void checkPromotion(Piece selectedPiece) {
		// TODO add promotion selection
		if(selectedPiece instanceof Pawn) {
			Pawn pawn = (Pawn) selectedPiece;
			if (pawn.promotion()) {
				String decision = new PawnPromotionDisplay().start(display);
				Object piece = new Queen(selectedPiece.isWhite(), shell);
				if (decision.equals("Rook")) {
					piece = new Rook(selectedPiece.isWhite(), shell); }
				else if (decision.equals("Knight")) {
					piece = new Knight(selectedPiece.isWhite(), shell); }
				else if (decision.equals("Bishop")) {
					piece = new Bishop(selectedPiece.isWhite(), shell); }
				
				((Piece) piece).updateLocation(pawn.getX(), pawn.getX());
				board[selectedPiece.getY()][selectedPiece.getX()].setPiece((Piece) piece);
				
				this.promotion = true;
			}
		}
	}
	
	public void highlightCoordinates(Piece selectedPiece) {
		selectedCoordinates = selectedPiece.generateMoves(board);
		filterCheckMoves(selectedPiece,selectedCoordinates);
	}
	private void filterCheckMoves(Piece piece,List<Coordinate> coordinates) {
		for(int i = 0;i<selectedCoordinates.size();i++) {
			Coordinate c = selectedCoordinates.get(i);
			if(!validCheckMove(c.x,c.y,piece,piece.isWhite())) {
				selectedCoordinates.remove(i);
				i--;
			}
		}
		//TODO filter out castling if king ends up in check
	}
	public void unhighlightCoordinates(Piece selectedPiece) { selectedCoordinates = null; }
	
	public Tile getTile(int x, int y) { return board[y][x]; }
	
	public boolean getPromotion() {
		if (this.promotion) {
			this.promotion = false;
			return true; }
		return false;
	}

	public void printBoard() {
		for (int row = 0; row < 8; row++) {
			
			for (int col = 0; col < 8; col ++) {
				System.out.print(this.board[row][col]+ " ");
			}
			System.out.println();
		}
	}
}