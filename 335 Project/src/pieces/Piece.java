package pieces;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

import game.Coordinate;
import game.Tile;

/**
 * This abstract class is responsible for outlining what subclasses of Piece can
 * do, along with some methods that are non-specific to piece subclasses.
 * Subclasses of Piece are: King, Queen, Bishop, Knight, Pawn, and Rook.
 * 
 * @author Julius Ramirez
 */

//TODO ideally, update setLocation to be 0-7 instead of pixel loc
public abstract class Piece {
	private boolean white;
	private int x;
	private int y;
	private Image visual;
	private static int SQUARE_WIDTH = 80;
	private boolean dead = false;

	public String name;
	int BOARD_COORD_OFFSET = 100;
	public boolean selected;

	/**
	 * This method is used to set the SQUARE_WIDTH variable, a variable representing
	 * how wide each tile on the board is which is necessary for when a piece must
	 * draw itself onto the UI.
	 * 
	 * @param width, an integer representing how wide a square is on the board.
	 */
	public static void setWidth(int width) {
		SQUARE_WIDTH = width;
	}

	public Piece(boolean white) {
		this.white = white;
		this.selected = false;
	}

	/**
	 * This method returns a string in the form of COLOR PIECE X Y.
	 * 
	 * @return a string describing the state of the piece.
	 */
	@Override
	public String toString() {
		String color;
		if (white) {
			color = "WHITE ";
		} else {
			color = "BLACK ";
		}
		String builder = color + name + " (" + this.x + "," + this.y +")";
		return builder;
	}
	
	
	/**
	 * these functions are used to highlight the user selected piece
	 *  on the chess board;
	 */
	
	public void setSelected() {
		this.selected = true;
	}
	public boolean isSelected() {
		return this.selected;
	}
	
	public void SetNotSelected() {
		this.selected = false;
	}

	/**
	 * This method returns a boolean indicating if the move is valid. A move is
	 * valid if it is a standard move of that piece, and if it the movement does not
	 * collide with pieces already on the board. It assumes that the new coordinate
	 * is different than the current pieces coordinate.
	 * 
	 * @param x     any integer between 0-7 inclusive.
	 * @param y     any integer between 0-7 inclusive.
	 * @param tiles a 2D array containing Tile objects that contain a Piece field.
	 * @return a boolean, true if move can be made, false if not.
	 */
	public boolean validMove(int x, int y, Tile[][] tiles) {
		return this.standardMove(x, y) && this.hasNoCollisions(x, y, tiles);
	}

	/**
	 * This method is responsible for drawing a piece onto the UI.
	 * 
	 * @param gc the graphical context where the image is to be drawn onto.
	 */
	public void draw(GC gc) {

		gc.drawImage(this.visual, this.x * SQUARE_WIDTH + 10+BOARD_COORD_OFFSET/2, this.y * SQUARE_WIDTH + 10);
	}

	/**
	 * This method is used to determine the color of the piece.
	 * 
	 * @return true if piece is white, false if black..
	 */
	public boolean isWhite() {
		return this.white;
	}

	/**
	 * This getter method returns the x value of the piece.
	 * 
	 * @return an integer representing the x value of the piece.
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * This getter method returns the y value of the piece.
	 * 
	 * @return an integer representing the y value of the piece.
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * This method updates the location of a given piece.
	 * 
	 * @param x any integer between 0-7 inclusive.
	 * @param y any integer between 0-7 inclusive.
	 */
	public void updateLocation(int x, int y) {
		this.x =  x;
		this.y = y;
	}

	/**
	 * This method sets the image of the piece so that it is properly represented on
	 * the chess board.
	 * 
	 * @param image an image representing the Piece object.
	 */
	public void setImage(Image image) {
		this.visual = image;
	}

	/**
	 * This method returns a boolean depending on if the piece is moved in a way
	 * that it is intended to move.
	 * 
	 * @param x any integer between 0-7 inclusive.
	 * @param y any integer between 0-7 inclusive.
	 * @return true if the piece is moved in a typical way, false if not.
	 */
	public abstract boolean standardMove(int x, int y);

	/**
	 * This method returns a boolean indicating if the piece collides with a
	 * friendly piece at the x/y coordinate.
	 * 
	 * @param x     any integer between 0-7 inclusive
	 * @param y     any integer between 0-7 inclusive
	 * @param tiles a 2D list containing Tile objects which contain an accessible
	 *              Piece field.
	 * @return true if no pieces are in the way, false if a piece would be blocking
	 *         the move.
	 */
	public abstract boolean hasNoCollisions(int x, int y, Tile[][] tiles);

	/**
	 * This method sets the x/y coordinate of the piece. It MUST be given 
	 * the graphical x/y values where the user has clicked. It is the only
	 * method that interprets x/y in this way.
	 * 
	 * @param x the x coordinate where the user clicked
	 * @param y the x coordinate where the user clicked
	 */
	public void setLocation(int x, int y) {
		this.x = (x-BOARD_COORD_OFFSET/2) / getSQUARE_WIDTH();
		this.y = y / getSQUARE_WIDTH();
	}

	/**
	 * Returns the SQUARE_WIDTH variable.
	 * 
	 * @return an int representing how wide squares on the board are.
	 */
	public static int getSQUARE_WIDTH() {
		return SQUARE_WIDTH;
	}
	
	/**
	 * Returns the color of the piece
	 * 
	 * @return String representing the color
	 */
	public String getColor() {
		if (white)
			return "White";
		else
			return "Black";
	}
	
	/**
	 * Returns if piece is killed off
	 */
	public boolean isDead() {
		return this.dead;
	}
	
	/**
	 * Kill this piece so that it may not be moved by the AI
	 */
	public void killPiece() {
		this.dead = true;
	}
	
	/**
	 * Returns name of the piece
	 */
	public String getName() {
		return this.name;
	}
	public abstract List<Coordinate> generateMoves(Tile[][] tiles);
	
	public boolean hasEnemyPiece(int x, int y, Tile[][]tiles) {
		return tiles[y][x].hasPiece() && tiles[y][x].getPiece().isWhite() != this.white;
	}
	public boolean hasFriendlyPiece(int x, int y, Tile[][]tiles) {
		return tiles[y][x].hasPiece() && tiles[y][x].getPiece().isWhite() == this.white;
	}
}
