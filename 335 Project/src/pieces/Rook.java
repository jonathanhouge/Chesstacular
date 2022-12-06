package pieces;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import game.Coordinate;
import game.Tile;

public class Rook extends Piece {
	// Image names
	String whitePiece = "wr.png";
	String blackPiece = "br.png";
	int points = 5;
	/**
	 * True if the rook has ever been moved, false if not.
	 */
	public boolean moved;

	/**
	 * Subclass constructor of {@link Piece#Piece(boolean)}. The {@link Rook#moved} field is initialized to
	 * false.
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
		this.moved = false;
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
	 * {@inheritDoc} Additionally, it also checks to see if the piece collides with
	 * another piece while attempting to move to the new x/y coordinate.
	 */
	@Override
	public boolean hasNoCollisions(int x, int y, Tile[][] tiles) {
		// First, check to see if the desired spot will collide with players own piece
		if (tiles[y][x].getPiece() != null && tiles[y][x].getPiece().isWhite() == this.isWhite()) {
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
		int y = getY() - 1;
		while (y >= 0) {
			if (!tiles[y][getX()].hasPiece()) {
				coordinates.add(new Coordinate(getX(), y));
			} else {
				if (hasEnemyPiece(getX(), y, tiles)) {
					coordinates.add(new Coordinate(getX(), y));
				}
				break;
			}
			y--;
		}
		// Down movement
		y = getY() + 1;
		while (y <= 7) {
			if (!tiles[y][getX()].hasPiece()) {
				coordinates.add(new Coordinate(getX(), y));
			} else {
				if (hasEnemyPiece(getX(), y, tiles)) {
					coordinates.add(new Coordinate(getX(), y));
				}
				break;
			}
			y++;
		}
		// Right movement
		int x = getX() + 1;
		while (x <= 7) {
			if (!tiles[getY()][x].hasPiece()) {
				coordinates.add(new Coordinate(x, getY()));
			} else {
				if (hasEnemyPiece(x, getY(), tiles)) {
					coordinates.add(new Coordinate(x, getY()));
				}
				break;
			}
			x++;
		}
		// Left movement
		x = getX() - 1;
		while (x >= 0) {
			if (!tiles[getY()][x].hasPiece()) {
				coordinates.add(new Coordinate(x, getY()));
			} else {
				if (hasEnemyPiece(x, getY(), tiles)) {
					coordinates.add(new Coordinate(x, getY()));
				}
				break;
			}
			x--;
		}
		return coordinates;
	}

}
