package pieces;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import game.Tile;

public class Bishop extends Piece {

	String whitePiece = "wb.png";
	String blackPiece = "bb.png";
	int points = 3;

	public Bishop(boolean white, Shell shell) {
		super(white);

		if (white) {
			setImage(new Image(shell.getDisplay(), "images/" + whitePiece));
		} else {
			setImage(new Image(shell.getDisplay(), "images/" + blackPiece));
		}
		this.name = "BISHOP";

	}

	@Override
	public boolean standardMove(int x, int y) {
		int xDistance = Math.abs(x - this.getX());
		int yDistance = Math.abs(y - this.getY());
		return xDistance == yDistance;
	}

	/**
	 * {@inheritDoc} Additionally, it also checks to see if the piece collides with
	 * another piece while attempting to move to the new x/y coordinate.
	 */
	@Override
	public boolean hasNoCollisions(int x, int y, Tile[][] tiles) {
		if (tiles[y][x].getPiece() != null && tiles[y][x].getPiece().isWhite() == this.isWhite()) {
			return false;
		}
		int distance = Math.abs(x - this.getX());

		if (this.getX() < x && this.getY() < y) { // Moving south-east
			for (int i = 1; i < distance; i++) {
				if (tiles[this.getY() + i][this.getX() + i].hasPiece()) {
					return false;
				}
			}
		} else if (this.getX() > x && this.getY() > y) { // Moving north-west
			for (int i = 1; i < distance; i++) {
				if (tiles[this.getY() - i][this.getX() - i].hasPiece()) {
					return false;
				}
			}
		} else if (this.getX() < x && this.getY() > y) {// Moving north-east
			for (int i = 1; i < distance; i++) {
				if (tiles[this.getY() - i][this.getX() + i].hasPiece()) {
					return false;
				}
			}
		} else if (this.getX() > x && this.getY() < y) { // Moving south-west
			for (int i = 1; i < distance; i++) {
				if (tiles[this.getY() + i][this.getX() - i].hasPiece()) {
					return false;
				}
			}
		} else { //non-standard movement, need this block for queen movement
			return false;
		}
		return true;
	}

}
