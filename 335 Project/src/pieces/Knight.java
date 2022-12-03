package pieces;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import game.Coordinate;
import game.Tile;

public class Knight extends Piece {

	String whitePiece = "wkn.png";
	String blackPiece = "bkn.png";
	int points = 3;

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
		if (tiles[y][x].getPiece() != null && tiles[y][x].getPiece().isWhite() == this.isWhite()) {
			return false;
		}
		return true;
	}

	//TODO check if coordinate has piece
	@Override
	public List<Coordinate> generateMoves(Tile[][] tiles) {
		List<Coordinate> coordinates = new ArrayList<>();
		// Top Standing L- type moves
		int y = getY() - 2;
		if (y >= 0) {
			if (getX() - 1 >= 0) {
				coordinates.add(new Coordinate(getX() - 1, y));
			}
			if (getX() + 1 <= 7) {
				coordinates.add(new Coordinate(getX() + 1, y));
			}
		}
		// Botom L - type moves
		y = getY() + 2;
		if (y <= 7) {
			if (getX() - 1 >= 0) {
				coordinates.add(new Coordinate(getX() - 1, y));
			}
			if (getX() + 1 <= 7) {
				coordinates.add(new Coordinate(getX() + 1, y));
			}
		}
		
		int x = getX() - 2;
		if (x >= 0) {
			if (getY() - 1 >= 0) {
				coordinates.add(new Coordinate(x, getY() - 1));
			}
			if (getY() + 1 <= 7) {
				coordinates.add(new Coordinate(x,getY() + 1));
			}
		}
		x = getX() + 2;
		if (x <= 7) {
			if (getY() - 1 >= 0) {
				coordinates.add(new Coordinate(x, getY() - 1));
			}
			if (getY() + 1 <= 7) {
				coordinates.add(new Coordinate(x,getY() + 1));
			}
		}
		return coordinates;
	}

}
