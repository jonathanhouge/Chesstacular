package pieces;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import game.Tile;

public class Pawn extends Piece {

	String whitePiece = "wp.png";
	String blackPiece = "bp.png";
	int points = 1;

	boolean firstMove;
	boolean enPassant = false; // if this is true, en passant may be done on this pawn
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
		this.didEnPassant = false;
	}
	@Override
	public void updateLocation(int x, int y) {
		int yConverted = y/getSQUARE_WIDTH();
		int yDistance = Math.abs(this.getY() - yConverted);
		super.updateLocation(x, y);
		System.out.println(yConverted);
		System.out.println("PAWN updateLocation(): Y distance: " + yDistance + " Y: " + y);
		if (!firstMove && yDistance == 1) {
			enPassant = false;
		} else {
			firstMove = false;
			if (yDistance == 2) {
				enPassant = true;
			}
		}
	}

	/**
	 * {@inheritDoc} Due to the special en passant move, it also checks to see if 
	 * a legal en passant move has been made.
	 */
	@Override
	public boolean validMove(int x, int y, Tile[][] tiles) {
		int xDistance = Math.abs(this.getX() - x);
		int yDistance = Math.abs(this.getY() - y);

		if (!standardMove(x, y)) {// ensures white pieces move up, black pieces move down
			System.out.println("validMove(): Non standard pawn move made!");
			return false;
		}
		
		if (this.getX() == x) { // vertical movement block
			return hasNoCollisions(x,y,tiles);
		} else { // check to see if player takes opponents piece
			if (xDistance == 1 && yDistance == 1) {
				// check if moving onto own piece OR moving onto en passant spot
				if (tiles[y][x].getPiece() != null && tiles[y][x].getPiece().isWhite() != this.isWhite()) {
					return true;
				} else if (tiles[y][x].getPiece() == null) { // en passant block
					System.out.println("PAWN validMove(): Entered en passant block");
					if(enPassantMove(x,y,tiles)) {
						didEnPassant = true;
						return true;
					}
				}
			}
		}

		return false;
	}
	
	/**
	 * This method checks to see if an en passant move has been made. If the x/y coordinate is empty, 
	 * in the validMove() method call, this method will be called which will check the space above or 
	 * below and see if there is a pawn there. If an opponents pawn is there and the en passant boolean 
	 * is true, it returns true.
	 * 
	 * @param x any integer between 0-7 inclusive.
	 * @param y	any integer between 0-7 inclusive.
	 * @param tiles a 2D array containing Tile objects that contain a Piece field
	 * @return a boolean, true if a legal en passant move has been made, false if not
	 */
	private boolean enPassantMove(int x, int y, Tile[][]tiles) {
		if (this.isWhite()) {
			if (tiles[y + 1][x].getPiece() != null && tiles[y + 1][x].getPiece() instanceof Pawn
					&& tiles[y + 1][x].getPiece().isWhite() != this.isWhite()) {
				Pawn opponent = (Pawn) tiles[y +1][x].getPiece();
				System.out.println("PAWN enPassantMove(): returning pawns en passant field");
				return opponent.enPassant;
			}
		} else {
			if (tiles[y - 1][x].getPiece() != null && tiles[y - 1][x].getPiece() instanceof Pawn
					&& tiles[y - 1][x].getPiece().isWhite() != this.isWhite()) {
				Pawn opponent = (Pawn) tiles[y - 1][x].getPiece();
				System.out.println("PAWN enPassantMove(): returning pawns en passant field");
				return opponent.enPassant;
			}
		}
		System.out.println("If/else failed to return field");
		return false;
	}
	
	/**
	 * {@inheritDoc} Additionally, it checks to see if a piece is in the 
	 * way of the movement to the x/y coordinate and if the pawn has moved
	 * 2 spaces only on its first move.
	 * 
	 * @return true if no piece would block the move, false if a piece is blocking the move
	 * or if
	 */
	@Override
	public boolean hasNoCollisions(int x, int y, Tile[][] tiles) {
		int yDistance = Math.abs(this.getY() - y);
		if (yDistance == 1) {
			return !tiles[y][x].hasPiece();
		} else if(yDistance == 2 && firstMove){
			if(this.isWhite()) {
				return !tiles[y][x].hasPiece() &&  !tiles[y+1][x].hasPiece();
			}else {
				return !tiles[y][x].hasPiece() &&  !tiles[y-1][x].hasPiece();
			}
		}
		return false;
	}
	/**
	 * This method checks if the player is moving their pawn in the right direction
	 * based on the pieces color and also ensures that if they move two spaces, 
	 * it is only on the first move.
	 * 
	 * @param x any integer between 0-7 inclusive.
	 * @param y	any integer between 0-7 inclusive.
	 * @return a boolean, true if a pawn has moved in the right direction/amount of spaces,
	 * false if non standard movement or moved beyond legal range.
	 */

	public boolean standardMove(int x, int y) {
		int yDistance = Math.abs(this.getY() - y);
		if(yDistance >= 3) {
			System.out.println("Moved 3+ spaces!");
			return false;
		}else if (yDistance == 2 && !firstMove) {
			System.out.println("moved 2 spaces but after first move");
			return false;
		}
		if (this.isWhite()) { //TODO determine if white will always be moving up
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
	public boolean promotion() {
		if (this.isWhite()) {
			return this.getY() == 0;
		}
		return this.getY() == 7;
	}

}
