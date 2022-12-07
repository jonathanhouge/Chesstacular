package pieces;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import game.Coordinate;
import game.Tile;

/**
 * A subclass of Piece, this class contains King specific implementations of the
 * methods standardMove(),updateLocation(), hasNoCollisions(), and
 * generateMoves().
 * 
 * @author Julius Ramirez
 *
 */
public class King extends Piece {
	// Image name
	String whitePiece = "wk.png";
	String blackPiece = "bk.png";

	/**
	 * True if the king is in check, false if not.
	 */
	public boolean checked = false;
	/**
	 * True if the king has ever been moved, false if not.
	 */
	public boolean moved = false;
	/**
	 * True if the king has castled, false if not. It would only ever be true once.
	 */
	public boolean castlingMoveMade = false;

	/**
	 * Subclass constructor of {@link Piece#Piece(boolean)}.
	 * 
	 * @param white true if piece is white, false if black
	 * @param shell the graphical shell. Used to set the image of the piece.
	 */
	public King(boolean white, Shell shell) {
		super(white);
		if (white) {
			setImage(new Image(shell.getDisplay(), "images/" + whitePiece));
		} else {
			setImage(new Image(shell.getDisplay(), "images/" + blackPiece));
		}
		this.name = "KING";
	}

	@Override
	public boolean standardMove(int x, int y) {
		int xDistance = Math.abs(x - this.getX());
		int yDistance = Math.abs(y - this.getY());

		if (yDistance <= 1 && xDistance <= 1) {
			return true;
		} else if (!this.moved && yDistance == 0 && (xDistance == 3 || xDistance == 4)) {
			// Castling section
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc} The King's moved field is also set to true so castling may not
	 * be performed in future moves.
	 */
	@Override
	public void updateLocation(int x, int y) {
		super.updateLocation(x, y);
		this.moved = true;
	}

	@Override
	public boolean hasNoCollisions(int x, int y, Tile[][] tiles) {
		if (hasFriendlyPiece(x, y, tiles)) {
			if ((x == 7 && y == 7) || (x == 0 && y == 7) || (x == 7 && y == 0) || (x == 0 && y == 0)) {
				return true;
			}
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 * <P>
	 * The castling move coordinate is generated in the King class, however due to
	 * the nature of the generateMoves() method two edge cases occur:
	 * <P>
	 * 1. If a piece is protecting their king from being in check and thus cannot
	 * move, the generateMoves() for that piece might still produce a coordinate
	 * that will prevent the other king from doing the castle move.
	 * <P>
	 * 2. A king will be able to castle even if the resulting location of the king
	 * allows it to be attacked by a pawn.
	 * <P>
	 * Edge case two can be fixed by implementing helper methods that check for
	 * pawns in the corresponding locations, however case one involves more
	 * structural changes. A solution could be to pass the entire Chessboard object
	 * instead of the Tile array due to the Chessboard class implementing a
	 * validCheckMove() method and also a filterMoves() method, so when an opponents
	 * generateMoves() is called, those generated moves can be filtered, thus only
	 * valid moves will prevent castling from occuring.
	 * 
	 * @see game.Chessboard#validCheckMove(int, int, Piece, boolean)
	 */
	@Override
	public List<Coordinate> generateMoves(Tile[][] tiles) {
		List<Coordinate> coordinates = new ArrayList<>();
		for (int row = -1; row <= 1; row++) {
			for (int col = -1; col <= 1; col++) {
				if (getX() + col < 0 || getY() + row < 0 || getX() + col > 7 || getY() + row > 7) {
					break;
				}
				if (!tiles[getY() + row][getX() + col].hasPiece()) {
					coordinates.add(new Coordinate(getX() + col, getY() + row));
				} else {
					if (tiles[getY() + row][getX() + col].getPiece().isWhite() != this.isWhite()) {
						coordinates.add(new Coordinate(getX() + col, getY() + row));
					}
				}
			}
		}
		if (!moved && !checked) { // This if-block is for adding castling moves
			if (this.isWhite()) {
				whiteCastlingHelper(tiles, coordinates);
			} else {
				blackCastlingHelper(tiles, coordinates);
			}
		}

		return coordinates;
	}

	// -- Helpers
	/**
	 * This method is called within generateMoves() to determine if the king is able
	 * to castle. If the castling criteria has been met, the Rook's coordinate is
	 * added to the List containing possible moves.
	 * 
	 * @param tiles       a 2D array containing Tile objects which have an
	 *                    obtainable Piece field.
	 * @param coordinates a List containing Coordinate objects that store's integers
	 *                    x and y
	 */
	private void blackCastlingHelper(Tile[][] tiles, List<Coordinate> coordinates) {
		if (hasFriendlyRook(7, 0, tiles)) {// Castling for right rook
			Rook r = (Rook) tiles[0][7].getPiece();
			if (!r.moved) {
				List<Coordinate> positions = new ArrayList<>();
				positions.add(new Coordinate(5, 0));
				positions.add(new Coordinate(6, 0));
				positions.add(new Coordinate(7, 0));
				boolean underAttack = underAttack(positions, tiles);
				if (!underAttack && !tiles[0][5].hasPiece() && !tiles[0][6].hasPiece()) {
					// Pawn edge case mentioned in generateMoves() header occurs here
					coordinates.add(new Coordinate(7, 0));
				}
			}
		}
		if (hasFriendlyRook(0, 0, tiles)) {// Castling for left rook
			Rook r = (Rook) tiles[0][0].getPiece();
			if (!r.moved) {
				List<Coordinate> positions = new ArrayList<>();
				positions.add(new Coordinate(0, 0));
				positions.add(new Coordinate(1, 0));
				positions.add(new Coordinate(2, 0));
				positions.add(new Coordinate(3, 0));
				boolean underAttack = underAttack(positions, tiles);
				if (!underAttack && !tiles[0][1].hasPiece() && !tiles[0][2].hasPiece() && !tiles[0][3].hasPiece()) {
					// Pawn edge case mentioned in generateMoves() header occurs here
					coordinates.add(new Coordinate(0, 0));
				}
			}
		}
	}

	/**
	 * This method is called within generateMoves() to determine if the king is able
	 * to castle. If the castling criteria has been met, the Rook's coordinate is
	 * added to the List containing possible moves.
	 * 
	 * @param tiles       a 2D array containing Tile objects which have an
	 *                    obtainable Piece field.
	 * @param coordinates a List containing Coordinate objects that store's integers
	 *                    x and y
	 */
	private void whiteCastlingHelper(Tile[][] tiles, List<Coordinate> coordinates) {
		if (hasFriendlyRook(7, 7, tiles)) {// castling for right rook
			Rook r = (Rook) tiles[7][7].getPiece();
			if (!r.moved) {
				List<Coordinate> positions = new ArrayList<>();
				positions.add(new Coordinate(5, 7));
				positions.add(new Coordinate(6, 7));
				positions.add(new Coordinate(7, 7));
				boolean underAttack = underAttack(positions, tiles);
				if (!underAttack && !tiles[7][5].hasPiece() && !tiles[7][6].hasPiece()) {
					// Pawn edge case mentioned in generateMoves() header occurs here
					coordinates.add(new Coordinate(7, 7));
				}
			}
		}
		if (hasFriendlyRook(0, 7, tiles)) {// Castling for left rook
			Rook r = (Rook) tiles[7][0].getPiece();
			if (!r.moved) {
				List<Coordinate> positions = new ArrayList<>();
				positions.add(new Coordinate(0, 7));
				positions.add(new Coordinate(1, 7));
				positions.add(new Coordinate(2, 7));
				positions.add(new Coordinate(3, 7));
				boolean underAttack = underAttack(positions, tiles);
				if (!underAttack && !tiles[7][1].hasPiece() && !tiles[7][2].hasPiece() && !tiles[7][3].hasPiece()) {
					// Pawn edge case mentioned in generateMoves() header occurs here
					coordinates.add(new Coordinate(0, 7));
				}
			}
		}
	}

	/**
	 * This helper method is used within the castling helper methods to determine if
	 * a corner tile contains a friendly Rook.
	 * 
	 * @param x     an integer equaling either 0 or 7
	 * @param y     an integer equaling either 0 or 7
	 * @param tiles a 2D array containing Tile objects which have an obtainable
	 *              Piece field.
	 * @return true if the location has a friendly Rook, false if not.
	 */
	private boolean hasFriendlyRook(int x, int y, Tile[][] tiles) {
		return hasFriendlyPiece(x, y, tiles) && tiles[y][x].getPiece() instanceof Rook;
	}

	/**
	 * This helper method is used within the castling helper methods to determine if
	 * any of the pieces between the Rook or King may be be attacked. The Rook's
	 * coordinate is included as well.
	 * 
	 * @param positions a List of Coordinate objects which are the space between the
	 *                  king and the rook, along with the Rook's location
	 * @param tiles     a 2D array containing Tile objects which have an obtainable
	 *                  Piece field.
	 * @return true if the space between the King and/or the Rook may be attacked by
	 *         some piece, false if not.
	 */
	private boolean underAttack(List<Coordinate> positions, Tile[][] tiles) {
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (tiles[7][7].hasPiece() && (tiles[7][7].getPiece().isWhite() != this.isWhite())) {
					List<Coordinate> enemyMoves = tiles[7][7].getPiece().generateMoves(tiles);
					for (Coordinate c : enemyMoves) {
						// Edge case, generateMoves() does not filter out non-valid check moves
						// thus piece that may be unable to move could still show tile as under attack.
						if (positions.contains(c)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	// -- Setters
	/**
	 * This simply sets the king in check, meaning that an enemy piece is currently
	 * able to kill it. This method is called whenever the board is updated.
	 */
	public void setCheck() {
		this.checked = true;
	}

	/**
	 * This simply sets the king's check field to false. This method is called
	 * whenever the board is updated, and may be called even when the king is not in
	 * check.
	 */
	public void checkEvaded() {
		this.checked = false;
	}
}
