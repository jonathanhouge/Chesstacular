package game;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Shell;

import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

public class Chessboard implements ChessBoardUI {
	Canvas canvas;
	Shell shell;
	int SQUARE_WIDTH = 80;
	Tile[][] board;
	String verticalCoords[] = new String[] { "8", "7", "6", "5", "4", "3", "2", "1" };
	String horizontalCoords[] = new String[] { "A", "B", "C", "D", "E", "F", "G", "H" };
	int BOARD_COORD_OFFSET = 100;

	
	Color SELECTED = new Color(51, 204, 51);
	Color BG = new Color(255, 255, 255);
	Color SIDE = new Color(204, 136, 0);

	public Chessboard(Canvas canvas, Shell shell) {
		this.canvas = canvas;
		this.shell = shell;
	}
	
	public Tile[][] getBoard(){return board;}
	public void setBoard(Tile[][]board) {this.board = board;}

	@Override
	public void draw(GC gc) {
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				drawing(x, y, gc); } }
	}
	
	private void drawing(int x, int y, GC gc) {
		Tile t = board[x][y];
		Piece p = t.getPiece();
		
		if(p != null) {
			if(p.isSelected()) {
				System.out.println("I am selected\n");
				t.draw(gc, SELECTED); }
			else {
				t.draw(gc);
			} }
		
		else {
			t.draw(gc); }
		
		if (x == 0) { addBoardCoords(gc, x, y); }
		if (y == 7) { addBoardCoords(gc, x, y); }
	}

	/*
	 * 'simpleton' function, only ran once for initialization. color boolean
	 * parameter determines what pieces should be in front of the player.
	 * 
	 * it creates an empty board
	 */
	public void createBoardData(GC gc, boolean white) {
		board = new Tile[8][8]; // list of tiles
		boolean boardColor = true; // if true, tile is white and vice versa
		Color w = new Color(255, 221, 153);
		Color b = new Color(204, 136, 0); // tile colors, white and black respectively

		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {

				// create the tiles with the proper color

				if (boardColor) {
					board[y][x] = new Tile(w, x * SQUARE_WIDTH + 50, y * SQUARE_WIDTH, SQUARE_WIDTH); } 
				else {
					board[y][x] = new Tile(b, x * SQUARE_WIDTH + 50, y * SQUARE_WIDTH, SQUARE_WIDTH); }


				// alternating tile colors
				boardColor = !boardColor; }
			boardColor = !boardColor; }
	}
	
	/**
	 * Adds all the chess pieces to the board.
	 * Used for the new game only.
	 */
	public void setAllPieces() {
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				
				// set the piece using a helper function
				board[y][x].setPiece(makePiece(x, y));
			}
			
		}
		
	}

	/*
	 * helper function responsible for making the pieces and putting them in their
	 * starting positions.
	 */
	private Piece makePiece(int x, int y) {

		// want the pieces farthest from the player to be the opposite color (opponent's
		// color)
		boolean pieceColor = false;

		if (y == 0) { // black specialty pieces
			if (x == 0 || x == 7) { return (new Rook(pieceColor, shell)); }
			else if (x == 0 || x == 7) { return (new Rook(pieceColor, shell)); } 
			else if (x == 1 || x == 6) { return (new Knight(pieceColor, shell)); } 
			else if (x == 2 || x == 5) { return (new Bishop(pieceColor, shell)); } 
			else if (x == 3) { return (new King(pieceColor, shell)); } 
			else if (x == 4) { return (new Queen(pieceColor, shell)); } } 
		else if (y == 1) { return (new Pawn(pieceColor, shell)); } // black pawns

		pieceColor = !pieceColor;

		if (y == 6) { return (new Pawn(pieceColor, shell)); } // your pawns
		else if (y == 7) { // your specialty pieces
			if (x == 0 || x == 7) { return (new Rook(pieceColor, shell)); }
			else if (x == 0 || x == 7) { return (new Rook(pieceColor, shell)); } 
			else if (x == 1 || x == 6) { return (new Knight(pieceColor, shell)); }
			else if (x == 2 || x == 5) { return (new Bishop(pieceColor, shell)); }
			else if (x == 3) { return (new King(pieceColor, shell)); } 
			else if (x == 4) { return (new Queen(pieceColor, shell)); } }

		return null; // tile is given no piece
	}

	public void mouseClickUpdate(float x, float y) {
		getBoardIndex(x, y);
	}
	
	public int[] getBoardIndex(float x, float y) {
		x -= BOARD_COORD_OFFSET / 2;
//		System.out.println(x + ":" + y);
		float indexX = x / SQUARE_WIDTH;
		float indexY = y / SQUARE_WIDTH;
//		System.out.println(indexX + ":" + indexY);

		int indX = (int) indexX;
		int indY = (int) indexY;
//		System.out.println(indexY+":"+indexY);

		if (indX < 0 || indX > 7 || indY < 0 || indY > 7 || indexX < 0) {
			System.out.println("Clicking outside the board!");
			return null;
		}
		System.out.println(horizontalCoords[indX] + verticalCoords[indY]);
		return new int[] { indX, indY };
	}

	private void addBoardCoords(GC gc, int x, int y) {

		Font font = new Font(canvas.getDisplay(), "Tahoma", 15, SWT.BOLD);
		gc.setFont(font);
		gc.setBackground(BG);
		gc.setForeground(SIDE);
		if (x == 0) {
			String vCoord = verticalCoords[y];
			gc.drawText(vCoord, x * SQUARE_WIDTH + 20, y * SQUARE_WIDTH + 25);

		}
		if (y == 7) {
			String xCoord = horizontalCoords[x];
			gc.drawText(xCoord, x * SQUARE_WIDTH + 60 + 20, (y + 1) * SQUARE_WIDTH + 15);
		}

	}

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
		piece.updateLocation(x, y); // necessary so that pawn enPassant is updated
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
		int oldX = piece.getX();
		int oldY = piece.getY();
		Piece oldPiece = board[y][x].getPiece(); // This is to ensure that if a piece is taken due to movePiece(), it gets restored.
		this.movePiece(x, y, piece);
		determineKingCheckStatus(getWhite);
		King newKing = getKing(getWhite);
		if (!newKing.checked) {
			System.out.println("Valid check move because king not in check");
			this.movePiece(oldX, oldY, piece);
			board[y][x].setPiece(oldPiece);
			return true;
		}
		System.out.println("Invalid check move because king in check");
		this.movePiece(oldX, oldY, piece);
		board[y][x].setPiece(oldPiece);

		return false;
		//return true;
	}

	/**
	 * The purpose of this method is to determine if a king is now in check or not.
	 * The player's color is passed so that only the desired player's king is analyzed.
	 * 
	 * @param getWhite, true if player is white, false if not
	 */
	private boolean determineKingCheckStatus(boolean getWhite) {
		King king = getKing(getWhite);
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (board[row][col].getPiece() != null
						&& board[row][col].getPiece().validMove(king.getX(), king.getY(), board)) {
					king.inCheck();
					System.out.println("Oh no! The " + king + " is in check because of the " +board[row][col].getPiece());
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
		this.board[yCoord][xCoord].setPiece(null);

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
				int x = pawn.getX();
				int y = pawn.getY();
				if(pawn.isWhite()) {
					y++;
				}else {
					y--;
				}
				this.removePiece(x,y);
				pawn.removeEnPassantMove();
			}
		}
	}

	/**
	 * This method is responsible for updating the board accordingly after a valid
	 * move is made. 
	 * 
	 * @param xCoord any integer between 0-7 inclusive.
	 * @param yCoord any integer between 0-7 inclusive.
	 * @param selectedPiece the piece that is to be moved to xCoord, yCoord
	 */
	public void updateBoard(int xCoord, int yCoord, Piece selectedPiece) {
		System.out.println("Chessboard.java - Moving..." + selectedPiece);
		this.movePiece(xCoord,yCoord,selectedPiece);
		System.out.println("Chessboard.java - Piece updated! " + selectedPiece);
		this.checkEnPassantMoveMade(selectedPiece);
		this.checkPromotion(selectedPiece);
		if(this.determineKingCheckStatus(!selectedPiece.isWhite())) {
			this.determineCheckMate(selectedPiece);
		};
	}

	/**
	 * This method determines if the game is over.
	 * 
	 * @param movedPiece, the piece that was moved this turn. Used to determine next player's color,
	 * could be changed
	 */
	private void determineCheckMate(Piece movedPiece) {
		// TODO implemen this fully, also maybe only call if piece is in check?
		for(int row = 0; row < 7; row++) {
			for(int col = 0;col < 7; col++) {
				if(board[row][col].getPiece()!= null) { 
					// if piece is at this coord AND the piece belongs to the opponent,
					// check if the opponent can make any valid moves.
					// TODO could probably do validCheckMove since generateMoves SHOULD
					// already pass the standardMove() check.
					if (board[row][col].getPiece().isWhite() != movedPiece.isWhite()) {
						Piece friendlyPiece = board[row][col].getPiece();
						Coordinate[] friendlyMoves = friendlyPiece.generateMoves();
						for(Coordinate coord: friendlyMoves) {
							if(validMoveMade(coord.getX(),coord.getY(),friendlyPiece, friendlyPiece.isWhite())) {
								//TODO if this part is reached check is, false could return something or set some field.
								System.out.println("Chessboard.java - validMove possible, checkmate not met");
								return;
							}
						}
					}
				}
			}
		}
		//If this part is reached no validMoveMade returned true, thus game over!
		//System.out.println("Chessboard.java - The opponent can not make any valid moves! Game over!");
		return;
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
				System.out.println("The pawn may now be promoted! Implement later, for now setting to queen.");
				Queen queen = new Queen(selectedPiece.isWhite(),shell);
				queen.updateLocation(pawn.getX(), pawn.getX());
				board[selectedPiece.getY()][selectedPiece.getX()].setPiece(queen);
			}
		}
	}
}