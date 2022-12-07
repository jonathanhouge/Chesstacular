package pieces;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import game.Coordinate;
import game.Tile;

/**
 * A subclass of Piece, this class contains Queen specific implementations of
 * the methods validMove(), standardMove(), setLocation() updateLocation(),
 * hasNoCollisions(), and generateMoves(). The class relies on the Rook and
 * Bishop class' standardMove() and hasNoCollisions() methods to work properly,
 * due to queen movement being a combination of a rook and bishop piece.
 * 
 * @author Julius Ramirez
 *
 */
public class Queen extends Piece {
	/**
	 * This rook will always be at the same location as the queen. Used for valid
	 * move detection and move generation.
	 */
	private Rook rook;
	/**
	 * This bishop will always be at the same location as the queen. Used for valid
	 * move detection and move generation.
	 */
	private Bishop bishop;

	// Image names
	String whitePiece = "wq.png";
	String blackPiece = "bq.png";
	int points = 9;

	/**
	 * Subclass constructor of {@link Piece#Piece(boolean)}. Creates and stores a
	 * Rook and Bishop object so that they may be used for valid move detection and
	 * move generation.
	 * 
	 * @param white true if piece is white, false if black
	 * @param shell the graphical shell. Used to set the image of the piece.
	 */
	public Queen(boolean white, Shell shell) {
		super(white);
		if (white) {
			setImage(new Image(shell.getDisplay(), "images/" + whitePiece));
		} else {
			setImage(new Image(shell.getDisplay(), "images/" + blackPiece));
		}
		this.name = "QUEEN";
		this.rook = new Rook(white, shell);
		this.rook.moved = true; // set moved to true so castling bugs will not occur.
		this.bishop = new Bishop(white, shell);
	}

	/**
	 * {@inheritDoc} Valid move determination is done using the Rook and Bishop
	 * classes standardMove() and hasNoCollisions() methods.
	 */
	@Override
	public boolean validMove(int x, int y, Tile[][] tiles) {
		// The code is written this way so that valid move determination is based on
		// only a
		// Rook or Bishop's movement, not both.
		if (rook.standardMove(x, y)) {
			return rook.hasNoCollisions(x, y, tiles);
		} else if (bishop.standardMove(x, y)) {
			return bishop.hasNoCollisions(x, y, tiles);
		}
		return false;
	}

	/***
	 * Because the queen utilizes the Rook and Bishop implementation of
	 * standardMove(), this method should never be called. Calling it will cause the
	 * program to terminate with a status code of 403.
	 */
	@Override
	public boolean standardMove(int x, int y) {
		System.out.println("Queen.java - queen standardMove() called, however it should never be called.");
		System.exit(403);
		return false;
	}

	/**
	 * Because the queen utilizes the Rook and Bishop implementation of
	 * hasNoCollisions(), this method should never be called. Calling it will cause
	 * the program to terminate with a status code of 403
	 */
	@Override
	public boolean hasNoCollisions(int x, int y, Tile[][] tiles) {
		System.out.println("Queen.java - queen hasNoCollisions() called, however it should never be called.");
		System.exit(403);
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

	@Override
	public List<Coordinate> generateMoves(Tile[][] tiles) {
		List<Coordinate> coordinates = new ArrayList<>();
		coordinates = rook.generateMoves(tiles);
		coordinates.addAll(bishop.generateMoves(tiles));
		return coordinates;
	}

}