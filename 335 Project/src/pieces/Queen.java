package pieces;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import game.Tile;

public class Queen extends Piece {
	private Rook rook;
	private Bishop bishop;

	String whitePiece = "wq.png";
	String blackPiece = "bq.png";
	int points = 9;

	public Queen(boolean white, Shell shell) {
		super(white);
		if (white) {
			setImage(new Image(shell.getDisplay(), "images/" + whitePiece));
		} else {
			setImage(new Image(shell.getDisplay(), "images/" + blackPiece));
		}
		this.name = "QUEEN";
		this.rook = new Rook(white, shell);
		this.bishop = new Bishop(white, shell);
	}

	
	@Override
	public boolean validMove(int x, int y, Tile[][] tiles) {
		if(rook.standardMove(x, y)) {
			return rook.hasNoCollisions(x, y, tiles);
		}else if (bishop.standardMove(x, y)) {
			return bishop.hasNoCollisions(x, y, tiles);
		}
		return false;
	}
	/**
	 * {@inheritDoc} Standard movement determination is done using the 
	 * Rook and Bishop classes standardMove() methods.
	 */
	@Override
	public boolean standardMove(int x, int y) {
		System.out.println("Queen.java - queen standardMove() called, however it should never be called.");
		System.exit(1);
		return false;
	}

	/**
	 * {@inheritDoc} Additionally, it also checks to see if the piece collides with
	 * another piece while attempting to move to the new x/y coordinate. Collision 
	 * detection is done using the Rook and Bishop classes hasNoCollisions() methods.
	 */
	@Override
	public boolean hasNoCollisions(int x, int y, Tile[][] tiles) {
		System.out.println("Queen.java - queen hasNoCollisions() called, however it should never be called.");
		System.exit(1);
		return false;
	}

	@Override 
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		this.rook.setLocation(x, y);
		this.bishop.setLocation(x, y);
	}
	@Override
	public void updateLocation(int x, int y) {
		super.updateLocation(x, y);
		this.rook.updateLocation(x, y);
		this.bishop.updateLocation(x, y);
	}

}