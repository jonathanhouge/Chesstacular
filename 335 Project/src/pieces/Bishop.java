package pieces;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import game.Coordinate;
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
		} else { // non-standard movement, need this block for queen movement
			return false;
		}
		return true;
	}

	@Override
	public List<Coordinate> generateMoves(Tile[][] tiles) {
		List<Coordinate> coordinates = new ArrayList<>();
		// UP-RIGHT
		int y = getY() - 1;
		int x = getX() + 1;
		while (true) {
			if(x < 0 || y < 0 || x > 7 || y >7) {
				break;
			}
			if (!tiles[y][x].hasPiece()) {
				coordinates.add(new Coordinate(x, y));
			} else {
				if(tiles[y][x].getPiece().isWhite() != this.isWhite()) {
					coordinates.add(new Coordinate(x,y));
				}
				break;
			}
			y--;
			x++;
		}
		// DOWN-RIGHT
		y = getY() + 1;
		x = getX() + 1;
		while (true) {
			if(x < 0 || y < 0 || x > 7 || y >7) {
				break;
			}
			if (!tiles[y][x].hasPiece()) {
				coordinates.add(new Coordinate(x, y));
			} else {
				if(tiles[y][x].getPiece().isWhite() != this.isWhite()) {
					coordinates.add(new Coordinate(x,y));
				}
				break;
			}
			y++;
			x++;
		}
		// UP-LEFT
		y = getY() - 1;
		x = getX() - 1;
		while (true) {
			if(x < 0 || y < 0 || x > 7 || y >7) {
				break;
			}
			if (!tiles[y][x].hasPiece()) {
				coordinates.add(new Coordinate(x, y));
			} else {
				if(tiles[y][x].getPiece().isWhite() != this.isWhite()) {
					coordinates.add(new Coordinate(x,y));
				}
				break;
			}
			x--;
			y--;
		}
		// DOWN-LEFT
		y = getY() + 1;
		x = getX() - 1;
//		for(int x = getX()-1,y = getY()+1;x>=0 && x <= 7 && y >= 0 && y <= 7;x--,y++) {
//			
//		}
		while (true) {
			if(x < 0 || y < 0 || x > 7 || y >7) {
				break;
			}
			if (!tiles[y][x].hasPiece()) {
				coordinates.add(new Coordinate(x, y));
			} else {
				if(tiles[y][x].getPiece().isWhite() != this.isWhite()) {
					coordinates.add(new Coordinate(x,y));
				}
				break;
			}
			x--;
			y++;
		}
		return coordinates;
	}
	//private void generateMovesHelper() {}
}
