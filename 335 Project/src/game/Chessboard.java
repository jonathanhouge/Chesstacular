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

				// set the piece using a helper function
				board[y][x].setPiece(makePiece(x, y));

				// alternating tile colors
				boardColor = !boardColor; }
			boardColor = !boardColor; }
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

	private int[] getBoardIndex(float x, float y) {

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
	 * This method returns a boolean which indicates if the move to the desired x/y
	 * coordinate is a legal move that the piece can make.
	 * 
	 * @param x     the x coordinate where the user clicked
	 * @param y     the x coordinate where the user clicked
	 * @param piece the Piece object that is to be moved
	 * @return true if the move is legal, false if not.
	 * @author Julius Ramirez
	 */
	public boolean validMoveMade(int x, int y, Piece piece) {
		int[] coordinates = getBoardIndex(x, y);
		if (coordinates == null) {
			return false;
		}
		int xCoord = coordinates[0];
		int yCoord = coordinates[1];
		return piece.validMove(xCoord, yCoord, board);
	}

	/**
	 * This method updates the piece's coordinates to the new x/y coordinate and
	 * ensures that the old tilies piece field is set to null.
	 * 
	 * @param x     the x coordinate where the user clicked
	 * @param y     the x coordinate where the user clicked
	 * @param piece the Piece object that is to be moved
	 * @author Julius Ramirez
	 */
	public void movePiece(int x, int y, Piece piece) {
		int[] coordinates = getBoardIndex(x, y);
		if (coordinates == null) {
			return;
		}
		int xCoord = coordinates[0];
		int yCoord = coordinates[1];
		board[piece.getY()][piece.getX()].setPiece(null);
		piece.updateLocation(x, y);
		board[yCoord][xCoord].setPiece(piece);

	}

	/**
	 * This method is responsible for returning a piece (or null) that the player
	 * wants to move.
	 * 
	 * @param x           the x coordinate where the user clicked.
	 * @param y           the x coordinate where the user clicked.
	 * @param playerColor the color (White or Black) of the player.
	 * @return a piece object if the piece can be selected by the player, null if
	 *         empty/opponents piece.
	 * @author Julius Ramirez
	 */
	public Piece selectPiece(int x, int y, String playerColor) {
		boolean playerIsWhite = playerColor.equals("White");
		int[] coordinates = getBoardIndex(x, y);
		int xCoord = coordinates[0];
		int yCoord = coordinates[1];
		// this if also ensures the player selects one of their own pieces to move
		if (this.board[yCoord][xCoord].getPiece() != null
				&& this.board[yCoord][xCoord].getPiece().isWhite() == playerIsWhite) {
			return this.board[yCoord][xCoord].getPiece();
		}
		return null;
	}

	/**
	 * This is a debug version of selectPiece(). It acts the same however the user
	 * can also move the opponents pieces.
	 * 
	 * @param x           the x coordinate where the user clicked.
	 * @param y           the x coordinate where the user clicked.
	 * @param playerColor the color (White or Black) of the player.
	 * @return a piece object if the piece can be selected by the player, null if
	 *         empty/opponents piece.
	 * @author Julius Ramirez
	 */
	public Piece selectPiece(int x, int y) {
		int[] coordinates = getBoardIndex(x, y);
		if (coordinates == null) {
			return null;
		}
		int xCoord = coordinates[0];
		int yCoord = coordinates[1];
		if (this.board[yCoord][xCoord].getPiece() != null) {
			return this.board[yCoord][xCoord].getPiece();
		}
		return null;

	}
	public void removePiece(int xCoord, int yCoord) {
		this.board[yCoord][xCoord].setPiece(null);

	}
}