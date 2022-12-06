package pieces;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import game.Coordinate;
import game.Tile;

public class Pawn extends Piece {

	String whitePiece = "wp.png";
	String blackPiece = "bp.png";
	int points = 1;

	public boolean firstMove;
	public boolean enPassantable = false; // if this is true, en passant may be done on this pawn
	public boolean didEnPassant = false;

	public Pawn(boolean white, Shell shell) {
		super(white);
		this.firstMove = true;
		if (white) {
			setImage(new Image(shell.getDisplay(), "images/" + whitePiece));
		} else {
			setImage(new Image(shell.getDisplay(), "images/" + blackPiece));
		}
		this.name = "PAWN";

	}

	public void removeEnPassantMove() {
		//System.out.println("Pawn.java - removing didEnPassant");
		this.didEnPassant = false;

	}

	@Override
	public void updateLocation(int x, int y) {
		int yDistance = Math.abs(this.getY() - y);
		int xDistance = Math.abs(this.getX() - x);
		super.updateLocation(x, y);
		if (!firstMove && yDistance == 1) {
			//System.out.println(this + "is not en passantable anymore!");
			enPassantable = false;
		} else {
			firstMove = false;
			if (yDistance == 2) {
				enPassantable = true;
			}
		}
	}

	/**
	 * {@inheritDoc} Due to the special en passant move, it also checks to see if a
	 * legal en passant move has been made.
	 */
	@Override
	public boolean validMove(int x, int y, Tile[][] tiles) {
		int xDistance = Math.abs(this.getX() - x);
		int yDistance = Math.abs(this.getY() - y);

		if (!standardMove(x, y)) {// ensures white pieces move up, black pieces move down
			return false;
		}

		if (this.getX() == x) { // vertical movement block
			return hasNoCollisions(x, y, tiles);
		} else { // check to see if player takes opponents piece
			if (xDistance == 1 && yDistance == 1) {
				// check if moving onto enemy piece OR moving onto en passant spot
				if (hasEnemyPiece(x, y, tiles)) {
					return true;
				} else if (!tiles[y][x].hasPiece()) { // en passant block
					if (validEnPassantMove(x, y, tiles)) {
						didEnPassant = true;
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * This method checks to see if an en passant move has been made. If the x/y
	 * coordinate is empty, in the validMove() method call, this method will be
	 * called which will check the space above or below and see if there is a pawn
	 * there. If an opponents pawn is there and the en passant boolean is true, it
	 * returns true.
	 * 
	 * @param x     any integer between 0-7 inclusive.
	 * @param y     any integer between 0-7 inclusive.
	 * @param tiles a 2D array containing Tile objects that contain a Piece field
	 * @return a boolean, true if a legal en passant move has been made, false if
	 *         not
	 */
	private boolean validEnPassantMove(int x, int y, Tile[][] tiles) {
		if (this.isWhite()) {
			if (hasEnemyPiece(x, y + 1, tiles) && tiles[y + 1][x].getPiece() instanceof Pawn) {
				Pawn opponent = (Pawn) tiles[y + 1][x].getPiece();
				return opponent.enPassantable;
			}
		} else {
			if (hasEnemyPiece(x, y - 1, tiles) && tiles[y - 1][x].getPiece() instanceof Pawn) {
				Pawn opponent = (Pawn) tiles[y - 1][x].getPiece();
				return opponent.enPassantable;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc} Additionally, it checks to see if a piece is in the way of the
	 * movement to the x/y coordinate and if the pawn has moved 2 spaces only on its
	 * first move.
	 * 
	 * @return true if no piece would block the move, false if a piece is blocking
	 *         the move or if
	 */
	@Override
	public boolean hasNoCollisions(int x, int y, Tile[][] tiles) {
		int yDistance = Math.abs(this.getY() - y);
		if (yDistance == 1) {
			return !tiles[y][x].hasPiece();
		} else if (yDistance == 2 && firstMove) {
			if (this.isWhite()) {
				return !tiles[y][x].hasPiece() && !tiles[y + 1][x].hasPiece();
			} else {
				return !tiles[y][x].hasPiece() && !tiles[y - 1][x].hasPiece();
			}
		}
		return false;
	}

	/**
	 * This method checks if the player is moving their pawn in the right direction
	 * based on the pieces color and also ensures that if they move two spaces, it
	 * is only on the first move.
	 * 
	 * @param x any integer between 0-7 inclusive.
	 * @param y any integer between 0-7 inclusive.
	 * @return a boolean, true if a pawn has moved in the right direction/amount of
	 *         spaces, false if non standard movement or moved beyond legal range.
	 */
	public boolean standardMove(int x, int y) {
		int yDistance = Math.abs(this.getY() - y);
		if (yDistance >= 3) {
			return false;
		} else if (yDistance == 2 && !firstMove) {
			return false;
		}
		if (this.isWhite()) {
			if (y > this.getY()) {
				return false; // false due to downward movement
			}
		} else {
			if (y < this.getY()) {
				return false; // false due to upwward movement
			}
		}
		return true;
	}

	/**
	 * This method simply returns if the pawn is now on the opposite side of the
	 * board so that it may then be promoted to some other piece
	 *
	 * @return true if promotion is possible, false if not.
	 */
	public boolean promotion() {
		if (this.isWhite()) {
			return this.getY() == 0;
		}
		return this.getY() == 7;
	}

	@Override
	public List<Coordinate> generateMoves(Tile[][] tiles) {
		List<Coordinate> coordinates = new ArrayList<>();
		if (this.isWhite()) {
			if (getY() - 1 >= 0 && !tiles[getY() - 1][getX()].hasPiece()) { // Vertical moves
				coordinates.add(new Coordinate(getX(), getY() - 1));
				if (firstMove && !tiles[getY() - 2][getX()].hasPiece()) {
					coordinates.add(new Coordinate(getX(), getY() - 2));
				}
			}
			if (getY() - 1 >= 0) {		// Diagonal moves
				if (getX() - 1 >= 0) {	// UP-LEFT\
					diagonalHelper(getX() - 1, getY() - 1, tiles,coordinates);
				}
				if (getX() + 1 <= 7) { // UP-RIGHT
					diagonalHelper(getX() + 1,getY() - 1,tiles,coordinates);
				}
			}
		} else {
			if (getY() + 1 <= 7 && !tiles[getY() + 1][getX()].hasPiece()) { // Vertical moves
				coordinates.add(new Coordinate(getX(), getY() + 1));
				if (firstMove && !tiles[getY() + 2][getX()].hasPiece()) {
					coordinates.add(new Coordinate(getX(), getY() + 2));
				}
			}
			if (getY() + 1 <= 7) { // Diagonal moves
				if (getX() - 1 >= 0) {	//DOWN-LEFT
					diagonalHelper(getX() - 1,getY() + 1,tiles,coordinates);
				}
				if (getX() + 1 <= 7) {	//DOWN-RIGHT\
					diagonalHelper(getX() + 1,getY() + 1,tiles,coordinates);
				}
			}
		}
		return coordinates;
	}
	
	/**
	 * 
	 * @param x The x to be moved to
	 * @param y	The y to be moved to
	 * @param tiles
	 * @param coordinates
	 */
	private void diagonalHelper(int x, int y, Tile[][]tiles,List<Coordinate> coordinates) {
		if (hasEnemyPiece(x, y, tiles)) {
			coordinates.add(new Coordinate(x, y));
		} else if (!tiles[y][x].hasPiece()) {
			if (hasEnemyPiece(x, getY(), tiles) && tiles[getY()][x].getPiece() instanceof Pawn) {
				Pawn p = (Pawn) tiles[getY()][x].getPiece();
				if (p.enPassantable) {
					System.out.println("Pawn IS en passantable");
					coordinates.add(new Coordinate(x, y));
				}else {
					System.out.println("Pawn NOT en passantable");
				}
			}
		}
	}
}
