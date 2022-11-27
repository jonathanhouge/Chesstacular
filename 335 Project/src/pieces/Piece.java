package pieces;

import java.util.ArrayList;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

//import Tile;

/**
 * This abstract class is responsible for outlining what subclasses of Piece can
 * do. Subclasses of Piece are: King, Queen, Bishop, Knight, Pawn, and Rook.
 * 
 * @author Julius Ramirez
 */
public abstract class Piece {
	private boolean white;
	private int x;
	private int y;
	private Image visual;
	private static int SQUARE_WIDTH = 80; // should it be all caps?

	public static void setWidth(int width) {
		SQUARE_WIDTH = width;
	}

	public Piece(boolean white) {
		this.white = white;
	}

	/**
	 * This method returns a boolean indicating if the move is valid.
	 * A move is valid if it is a standard move of that piece, and if 
	 * it does not collide with pieces already on the board.
	 * 
	 * @param x,y: any int between 0-7 inclusive.
	 * @param tiles: a 2D array containing 
	 * @return a boolean, true if move can be made, false if not
	 */
	public boolean validMove(int x, int y, Tile[][] tiles) {
		return true; // for testing purposes, this is set to true
		//return this.standardMove(x, y) && this.hasNoCollisions(x, y, tiles);
	}

	public void draw(GC gc) {
		gc.drawImage(this.visual, this.x * SQUARE_WIDTH + 10, this.y * SQUARE_WIDTH + 10);
	}
	
	/**
	 * This method acts as a getter, it returns true if the piece is white,
	 * false if black.
	 * @return true if piece is white, false if black.
	 */
	public boolean isWhite() {
		return this.white;
	}
	/**
	 * This getter method returns the x value of the piece
	 * 
	 * 	@return an int that represents the y value of the piece
	 */
	public int getX() {
		return this.x;
	}
	/**
	 * This getter method returns the y value of the piece.
	 * 
	 * @return an int that represents the y value of the piece
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * This method updates the location of a given piece.
	 */
	public void updateLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * This method sets the image of the piece so that it is correctly
	 * represented on the chess board.
	 */
	public void setImage(Image image) {
		this.visual = image;
	}

	/**
	 * This abstract method returns a boolean depending on if the piece is moved
	 * in a way that the specific subclass of piece is intended to move. 
	 * 
	 * @return true if the piece is moved in a typical way, false if not
	 */
	public abstract boolean standardMove(int x, int y);

	/**
	 * This abstract method returns a boolean depending on if the piece collides
	 * with its own piece at the x/y coordinate and if it collides with another 
	 * piece while attempting to move to the x/y coordinate.
	 * 
	 * @return true if no pieces are in the way, false if a piece would be blocking
	 * the move
	 */
	public abstract boolean hasNoCollisions(int x, int y, Tile[][] tiles);
}
