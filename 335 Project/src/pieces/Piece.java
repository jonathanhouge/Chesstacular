package pieces;

import java.util.List;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

import game.Coordinate;
import game.Tile;

/**
 * This abstract class is responsible for outlining general Piece functionality.
 * Subclasses of Piece are: King, Queen, Bishop, Knight, Pawn, and Rook.
 * 
 * @author Julius Ramirez
 */

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
	 * Creates a new piece, only saving its color. The location of the piece must 
	 * thus be set after its creation. Selected is initialized to false.
	 * @param white true if the piece is white, false if it is not.
	 */
	public Piece(boolean white) {
		this.white = white;
		this.selected = false;
	}

	/**
	 * This method returns a string in the form of: "COLOR PIECE (X,Y)"
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
		String builder = color + name + " (" + this.x + "," + this.y + ")";
		return builder;
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
	 * This method is responsible for drawing a piece onto the UI canvas.
	 * 
	 * @param gc the graphical context where the image is to be drawn onto.
	 */
	public void draw(GC gc) {
		gc.drawImage(this.visual, this.x * SQUARE_WIDTH + 10 + BOARD_COORD_OFFSET / 2, this.y * SQUARE_WIDTH + 10);
	}

	/**
	 * This helper method is used in the generateMove() methods, it returns true if
	 * the tile has an enemy piece.
	 * 
	 * @param x     the x, between 0 and 7 inclusive, to be moved to.
	 * @param y     the y, between 0 and 7 inclusive, to be moved to.
	 * @param tiles a 2D list containing Tile objects which contain an accessible
	 *              Piece field.
	 * @return true if enemy piece is at x/y, false if it's empty or a friendly
	 *         piece.
	 */
	public boolean hasEnemyPiece(int x, int y, Tile[][] tiles) {
		return tiles[y][x].hasPiece() && tiles[y][x].getPiece().isWhite() != this.white;
	}

	/**
	 * This helper method is used in the generateMove() methods, it returns true if
	 * the tile has a friendly piece.
	 * 
	 * @param x     the x, between 0 and 7 inclusive, to be moved to.
	 * @param y     the y, between 0 and 7 inclusive, to be moved to.
	 * @param tiles a 2D list containing Tile objects which contain an accessible
	 *              Piece field.
	 * @return true if friendly piece is at x/y, false if it's empty or an enemy
	 *         piece.
	 */
	public boolean hasFriendlyPiece(int x, int y, Tile[][] tiles) {
		return tiles[y][x].hasPiece() && tiles[y][x].getPiece().isWhite() == this.white;
	}

	// -- Getters
// -- Getters
	/**
	 * Returns the string color of the piece.
	 * 
	 * @return "White" if white, else "Black"
	 */
	public String getColor() {
		if (white)
			return "White";
		else
			return "Black";
	}

	/**
	 * Returns the dead fields current status. The dead field is used by the AI.
	 * 
	 * @return true if piece is dead, false if not.
	 */
	public boolean isDead() {
		return this.dead;
	}

	/**
	 * Returns the name of the piece, used in opponent promotion determination.
	 * 
	 * @return the string name of the piece in the form: PIECE
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * This method is used to determine the color of the piece, returning the
	 * piece's white field which is true if the piece is white, false if it is
	 * black.
	 * 
	 * @return true if piece is white, false if black.
	 */
	public boolean isWhite() {
		return this.white;
	}

	/**
	 * This getter method returns the x value of the piece.
	 * 
	 * @return an integer between 0 and 7 inclusive representing the x value of the
	 *         piece.
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * This getter method returns the y value of the piece.
	 * 
	 * @return an integer between 0 and 7 inclusive representing the y value of the
	 *         piece.
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * This method return a boolean indicating if this piece is selected.
	 * 
	 * @return true if this piece is selected, false if not.
	 */
	public boolean isSelected() {
		return this.selected;
	}

	// -- Setters
	/**
	 * This method updates the location of a the piece.
	 * 
	 * @param x any integer between 0-7 inclusive.
	 * @param y any integer between 0-7 inclusive.
	 */
	public void updateLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * This method sets the x/y coordinate of the piece. It MUST be given the
	 * graphical x/y values where the user has clicked. It is the only method that
	 * interprets x/y in this way and could be improved by following the x/y =
	 * coordinate convention, which would allow for easier move simulation due to no
	 * longer needing to save a piece's booleans.
	 * 
	 * @param x the x coordinate where the user clicked
	 * @param y the x coordinate where the user clicked
	 */
	public void setLocation(int x, int y) {
		this.x = (x - BOARD_COORD_OFFSET / 2) / SQUARE_WIDTH;
		this.y = y / SQUARE_WIDTH;
	}

	/**
	 * This method sets the piece as not selected so that when drawing the canvas,
	 * it does not highlight it as selected.
	 */
	public void SetNotSelected() {
		this.selected = false;
	}

	/**
	 * Sets the dead field of the piece. Used by the AI.
	 * 
	 * @param status, true if piece is dead, false if not.
	 */
	public void setDeadStatus(boolean status) {
		this.dead = status;
	}

	/**
	 * Sets the dead field to true so that it may not be moved by the AI.
	 */
	public void killPiece() {
		this.dead = true;
	}

	/**
	 * This method sets the pieces selected boolean to true so it may be highlighted
	 * on the canvas.
	 */
	public void setSelected() {
		this.selected = true;
	}

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

	/**
	 * This method sets the image of the piece so that it is properly represented on
	 * the chess board.
	 * 
	 * @param image an image representing the Piece object.
	 */
	public void setImage(Image image) {
		this.visual = image;
	}

	// -- Abstract methods
	/**
	 * This method returns a boolean depending on if the piece is moved in a way
	 * that it is intended to move, both state specific move or as it generally is
	 * moved.
	 * 
	 * @param x any integer between 0-7 inclusive.
	 * @param y any integer between 0-7 inclusive.
	 * @return true if the piece is moved in a legal way, false if not.
	 */
	public abstract boolean standardMove(int x, int y);

	/**
	 * Returns a boolean indicating if the piece collides with a friendly piece at
	 * the x/y coordinate. Enemy collisions are allowed.
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
	 * This method generates most of the valid moves a piece can do in terms of
	 * standard piece movement and no collisions made. It does NOT filter out
	 * non-valid check related moves (i.e. moves that keep the king in check/put the
	 * king in check).
	 * 
	 * @param tiles tiles a 2D list containing Tile objects which contain an
	 *              accessible Piece field.
	 * @return a List of Coordinate objects which are spaces where the piece can
	 *         possibly move.
	 */
	public abstract List<Coordinate> generateMoves(Tile[][] tiles);
}
