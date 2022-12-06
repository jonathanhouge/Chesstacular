package pieces;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import game.Coordinate;
import game.Tile;

public class Knight extends Piece {
	// Image names
	String whitePiece = "wkn.png";
	String blackPiece = "bkn.png";
	int points = 3;

	/**
	 * Subclass constructor of {@link Piece#Piece(boolean)}.
	 * 
	 * @param white true if piece is white, false if black
	 * @param shell the graphical shell. Used to set the image of the piece.
	 */
	public Knight(boolean white, Shell shell) {
		super(white);
		if (white) {
			setImage(new Image(shell.getDisplay(), "images/" + whitePiece));
		} else {
			setImage(new Image(shell.getDisplay(), "images/" + blackPiece));
		}
		this.name = "KNIGHT";
	}

	@Override
	public boolean standardMove(int x, int y) {
		int xDistance = Math.abs(this.getX() - x);
		int yDistance = Math.abs(this.getY() - y);
		if (xDistance == 2) { // Horizontal L move
			return yDistance == 1;
		} else if (xDistance == 1) { // Vertical L move
			return yDistance == 2;
		}
		return false;
	}
	@Override
	public boolean hasNoCollisions(int x, int y, Tile[][] tiles) {
		if (hasFriendlyPiece(x,y,tiles)) {
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc} This method may be improved by creating a
	 * helper method that does the bound checking.
	 */
	@Override
	public List<Coordinate> generateMoves(Tile[][] tiles) {
		List<Coordinate> coordinates = new ArrayList<>();
		// Down two, go left/right one
		int y = getY() - 2;
		if (y >= 0) {
			if (getX() - 1 >= 0) {
				generateMovesHelper(getX()-1,y,tiles,coordinates);
			}
			if (getX() + 1 <= 7) {
				generateMovesHelper(getX()+1,y,tiles,coordinates);
			}
		}
		// Up two, go left/right one
		y = getY() + 2;
		if (y <= 7) {
			if (getX() - 1 >= 0) {
				generateMovesHelper(getX()-1,y,tiles,coordinates);
			}
			if (getX() + 1 <= 7) {
				generateMovesHelper(getX()+1,y,tiles,coordinates);
			}
		}
		// Left two, go up/down one
		int x = getX() - 2;
		if (x >= 0) {
			if (getY() - 1 >= 0) {
				generateMovesHelper(x,getY()-1,tiles,coordinates);
			}
			if (getY() + 1 <= 7) {
				generateMovesHelper(x,getY()+1,tiles,coordinates);
			}
		}
		// Right two, go up/down one
		x = getX() + 2;
		if (x <= 7) {
			if (getY() - 1 >= 0) {
				generateMovesHelper(x,getY()-1,tiles,coordinates);
			}
			if (getY() + 1 <= 7) {
				generateMovesHelper(x,getY()+1,tiles,coordinates);
			}
		}
		return coordinates;
	}
	
	/**
	 * This helper is used to determine if the given diagonal x/y coordinate from a
	 * specific pawn is a valid spot to be moved onto. It is valid if the space is 
	 * empty or has an enemy piece.
	 * 
	 * @param x           The integer x, between 0-7 inclusive, to be moved to.
	 * @param y           The integer y, between 0-7 inclusive, to be moved to.
	 * @param tiles       a 2D array containing Tile objects which have an
	 *                    obtainable Piece field.
	 * @param coordinates a List of coordinate objects that the piece can move onto.
	 */
	private void generateMovesHelper(int x, int y, Tile[][] tiles, List<Coordinate> coordinates) {
		if(!tiles[y][x].hasPiece()) {
			coordinates.add(new Coordinate(x,y));
		}else if (hasEnemyPiece(x,y,tiles)) {
			coordinates.add(new Coordinate(x,y));
		}
	}

}
