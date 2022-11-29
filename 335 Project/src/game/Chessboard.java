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

	public Chessboard(Canvas canvas, Shell shell) {
		this.canvas = canvas;
		this.shell = shell;
	}

	@Override
	public void draw(GC gc) {
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				board[x][y].draw(gc);
				addBoardCoords(gc, x, y);
			}
		}
	}

	/*
	 * 'simpleton' function, only ran once for initialization. color boolean
	 * parameter determines what pieces should be in front of the player.
	 */
	public void createBoardData(GC gc, boolean white) {
		board = new Tile[8][8]; // list of tiles
		boolean boardColor = true; // if true, tile is white and vice versa
		Color w = new Color(255, 221, 153); Color b = new Color(204, 136, 0); // tile colors, white and black respectively
		
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				
				// create the tiles with the proper color

				if(boardColor) { board[y][x] = new Tile(w, x*SQUARE_WIDTH+50, y*SQUARE_WIDTH, SQUARE_WIDTH); }
				else { board[y][x] = new Tile(b, x*SQUARE_WIDTH+50, y*SQUARE_WIDTH, SQUARE_WIDTH); }
				
				// set the piece using a helper function
				board[y][x].setPiece(makePiece(x, y, white));

				// alternating tile colors
				boardColor = !boardColor; }
			boardColor = !boardColor; 
		}
	}
	
	/* helper function responsible for making the pieces and putting
	 * them in their starting positions. */
	private Piece makePiece(int x, int y, boolean playerColor) {
		
		// want the pieces farthest from the player to be the opposite color (opponent's color)
		playerColor = !playerColor;

		if (y == 0) { // enemy specialty pieces
			if (x == 0 || x == 7) { return (new Rook(playerColor, shell)); }
			else if (x == 0 || x == 7) { return (new Rook(playerColor, shell)); }
			else if (x == 1 || x == 6) { return (new Knight(playerColor, shell)); }
			else if (x == 2 || x == 5) { return (new Bishop(playerColor, shell)); }
			else if (x == 3) { return (new King(playerColor, shell)); }
			else if (x == 4) { return (new Queen(playerColor, shell)); } }
		else if (y == 1) { return (new Pawn(playerColor, shell)); } // enemy pawns
		
		// want the pieces closest to the player to be their color (player's color)
		playerColor = !playerColor;
		
		if (y == 6) { return (new Pawn(playerColor, shell)); } // your pawns
		else if (y == 7) { // your specialty pieces
			if (x == 0 || x == 7) { return (new Rook(playerColor, shell)); }
			else if (x == 0 || x == 7) { return (new Rook(playerColor, shell)); }
			else if (x == 1 || x == 6) { return (new Knight(playerColor, shell)); }
			else if (x == 2 || x == 5) { return (new Bishop(playerColor, shell)); }
			else if (x == 3) { return (new King(playerColor, shell)); }
			else if (x == 4) { return (new Queen(playerColor, shell)); } }
		
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
			System.out.println("Clicking outside the board! Returning null");
			return null;
		}
		System.out.println(horizontalCoords[indX] + verticalCoords[indY]);
		return new int[] { indX, indY };
	}

	private void addBoardCoords(GC gc, int x, int y) {

		Font font = new Font(canvas.getDisplay(), "Tahoma", 15, SWT.BOLD);
		gc.setFont(font);
		gc.setBackground(new Color(255, 255, 255));
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
	 * This method returns the king piece so that it may be used for check and
	 * checkmate calculations. It could be improved by removing the need to iterate
	 * through the entire board.
	 * 
	 * @param color a string, either "White" or "Black"
	 * @return
	 */
	public King getKing(boolean playerIsWhite) {
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (board[row][col].getPiece() instanceof King) {
					if (playerIsWhite) {
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
	 * coordinate is a legal move that the piece can make.
	 * 
	 * @param x     the x coordinate where the user clicked
	 * @param y     the x coordinate where the user clicked
	 * @param piece the Piece object that is to be moved
	 * @return true if the move is legal, false if not.
	 * @author Julius Ramirez
	 */
	public boolean validMoveMade(int x, int y, Piece piece,boolean whiteTurn) {
		return piece.validMove(x, y, board) && validCheckMove(x, y, piece, whiteTurn);
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
		piece.updateLocation(x, y);
		board[y][x].setPiece(piece);

	}
	/**
	 * 
	 * @param x
	 * @param y
	 * @param piece
	 * @param playerIsWhite
	 * @return
	 */
	public boolean validCheckMove(int x, int y, Piece piece, boolean playerIsWhite) {
		int oldX = piece.getX();
		int oldY = piece.getY();
		this.movePiece(x, y, piece);
		determineKingCheckStatus(playerIsWhite);
		King newKing = getKing(playerIsWhite);
		if (!newKing.checked) {
			System.out.println("Valid check move because king no longer in check");
			this.movePiece(oldX, oldY, piece);
			return true;
		}
		System.out.println("Invalid check move because king still in check");
		this.movePiece(oldX, oldY, piece);
		return false;
		//return true;
	}

	/**
	 * The purpose of this method is to determine if a king is now in check or not.
	 * 
	 * @param playerIsWhite, true if player is white, false if not
	 */
	public void determineKingCheckStatus(boolean playerIsWhite) {
		King king = getKing(playerIsWhite);
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (board[row][col].getPiece() != null
						&& board[row][col].getPiece().validMove(king.getX(), king.getY(), board)) {
					//System.out.println("CHESSBOARD: THE PIECE " +board[row][col].getPiece() +" CAN KILL THE KING" );
					king.inCheck();
					board[king.getY()][king.getX()].setPiece(king);
					return;
				}
			}
		}
		king.checkEvaded();
		board[king.getY()][king.getX()].setPiece(king);
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
	 * @param x           the x coordinate 0-7.
	 * @param y           the y coordinate 0-7.
	 * @param playerColor the color (White or Black) of the player.
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

	public void removePiece(int xCoord, int yCoord) {
		this.board[yCoord][xCoord].setPiece(null);
	}

	

	public void checkEnPassantMoveMade(Piece selectedPiece) {
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

	public void updateBoard(int xCoord, int yCoord, Piece selectedPiece) {
		this.movePiece(xCoord,yCoord,selectedPiece);
		this.checkEnPassantMoveMade(selectedPiece);
		this.determineKingCheckStatus(!selectedPiece.isWhite());
	}
}