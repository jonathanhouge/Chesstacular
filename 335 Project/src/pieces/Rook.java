package pieces;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import game.Coordinate;
import game.Tile;

/**
 * A subclass of Piece, this class contains Rook specific implementations of the
 * methods standardMove(),updateLocation(), hasNoCollisions(), and
 * generateMoves().
 * 
 * @author Julius Ramirez
 *
 */
public class Rook extends Piece {
	// Image names
	String whitePiece = "wr.png";
	String blackPiece = "br.png";
	int points = 5;
	/**
	 * True if the rook has ever been moved, false if not.
	 */
	public boolean moved = false;

	/**
	 * Subclass constructor of {@link Piece#Piece(boolean)}. The {@link Rook#moved}
	 * field is initialized to false.
	 * 
	 * @param white true if the piece is white, false if not
	 * @param shell the graphical shell. Used to set the image of the piece.
	 */
	public Rook(boolean white, Shell shell) {
		super(white);
		if (white) {
			setImage(new Image(shell.getDisplay(), "images/" + whitePiece));
		} else {
			setImage(new Image(shell.getDisplay(), "images/" + blackPiece));
		}
		this.name = "ROOK";
	}

	@Override
	public boolean standardMove(int x, int y) {
		if (this.getX() == x) { // possible horizontal movement
			if (0 <= y && y < 8) {
				return true;
			}
		} else if (this.getY() == y) { // possible vertical movement
			if (0 <= x && x < 8) {
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc} Additionally, it sets the moved field to true.
	 */
	@Override
	public void updateLocation(int x, int y) {
		super.updateLocation(x, y);
		this.moved = true;
	}

	/**
	 * {@inheritDoc} Also checks to see if the movement to the new x/y coordinate
	 * collides with any piece.
	 */
	@Override
	public boolean hasNoCollisions(int x, int y, Tile[][] tiles) {
		// First, check to see if the desired spot will collide with players own piece
		if (hasFriendlyPiece(x, y, tiles)) {
			return false;
		}
		// Finally, check to see nothing is in the path of the movement
		if (x < this.getX()) { // left movement
			for (int i = x + 1; i < this.getX(); i++) {
				if (tiles[y][i].hasPiece()) {
					return false;
				}
			}
		} else if (x > this.getX()) {// right movement
			for (int i = x - 1; i > this.getX(); i--) {
				if (tiles[y][i].hasPiece()) {
					return false;
				}
			}
		} else if (y < this.getY()) { // movement down
			for (int i = y + 1; i < this.getY(); i++) {
				if (tiles[i][x].hasPiece()) {
					return false;
				}
			}
		} else if (y > this.getY()) { // movement up
			for (int i = y - 1; i > this.getY(); i--) {
				if (tiles[i][x].hasPiece()) {
					return false;
				}
			}
		} else {
			// This block would normally never be reached but due to the Queen object's
			// movement utilizing a Rook object, this block must be included.
			return false;
		}
		return true;
	}

	@Override
	public List<Coordinate> generateMoves(Tile[][] tiles) {
		List<Coordinate> coordinates = new ArrayList<>();
		// Up movement
		for (int y = getY() - 1; y >= 0; y--) {
			if (generateMovesHelper(getX(), y, tiles, coordinates)) {
				break;
			}
		}
		// Down movement
		for (int y = getY() + 1; y <= 7; y++) {
			if (generateMovesHelper(getX(), y, tiles, coordinates)) {
				break;
			}
		}
		// Right movement
		for (int x = getX() + 1; x <= 7; x++) {
			if (generateMovesHelper(x, getY(), tiles, coordinates)) {
				break;
			}
		}
		// Left movement
		for (int x = getX() - 1; x >= 0; x--) {
			if (generateMovesHelper(x, getY(), tiles, coordinates)) {
				break;
			}
		}
		return coordinates;
	}

	/**
	 * This helper method is used in the generateMoves() method. It adds a
	 * Coordinate object to the list of coordinates if it is a valid space and also
	 * returns a boolean: false if the loop within generateMoves() can continue, or
	 * true if the loop must be broken.
	 * 
	 * @param x           The integer x, between 0-7 inclusive, to be moved to.
	 * @param y           The integer y, between 0-7 inclusive, to be moved to.
	 * @param tiles       a 2D array containing Tile objects which have a Piece
	 *                    field.
	 * @param coordinates a List of coordinate objects that the piece can move onto.
	 * @return true if the loop must be broken due to piece collision, false if not.
	 */
	private boolean generateMovesHelper(int x, int y, Tile[][] tiles, List<Coordinate> coordinates) {
		if (!tiles[y][x].hasPiece()) {
			coordinates.add(new Coordinate(x, y));
		} else {
			if (hasEnemyPiece(x, y, tiles)) {
				coordinates.add(new Coordinate(x, y));
			}
			return true;
		}
		return false;
	}
}
