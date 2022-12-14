package game;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import displays.PawnPromotionDisplay;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

/**
 * Chessboard implementation. Holds onto a list of Tiles that represent the
 * tiles in the 8x8 board. These tiles hold onto the pieces that occupy them.
 * Draws itself by iterating through this list and calling their draw methods.
 * Has a multitude of methods for different kinds of moves, standard and special
 * cases, and determining end game conditions.
 * 
 * @author Jonathan Houge & Julius Ramirez & Khojiakbar Yokubjonov
 */
public class Chessboard implements ChessboardUI {
	/**
	 * The UI Shell object
	 */
	private Shell shell;
	/**
	 * The UI Display object
	 */
	private Display display;
	/**
	 * A 2D array containing {@link game.Tile} objects which represent a 7X7
	 * chessboard.
	 */
	private Tile[][] board;
	/**
	 * The size of a graphical square tile.
	 */
	private int SQUARE_WIDTH = 80;
	/**
	 * The left x offset, or in other words the space that the vertical coordinate
	 * identifiers occupy
	 */
	private int BOARD_COORD_OFFSET = 100;
	/**
	 * An array containing Strings which represent the order of the vertical
	 * coordinates
	 */
	private String verticalCoords[] = new String[] { "8", "7", "6", "5", "4", "3", "2", "1" };
	/**
	 * An array containing Strings which represent the order of the horizontal
	 * coordinates
	 */
	private String horizontalCoords[] = new String[] { "A", "B", "C", "D", "E", "F", "G", "H" };
	/**
	 * A boolean which represents if this Chessboard object has made a promotion
	 * move. If set to true, it gets handled by the server so the opponent doesn't
	 * pick the promotion piece, and is then promptly set to false.
	 */
	private boolean promotion = false;
	/**
	 * A List of Coordinate objects which represent the tiles the selected piece can
	 * move to. It's worth noting that the selected piece Piece object is not
	 * actually saved however.
	 */
	private List<Coordinate> selectedCoordinates;

	// UI Fields
	Color SELECTED = new Color(51, 204, 51);
	Color BG = new Color(255, 255, 255);
	Color SIDE = new Color(204, 136, 0);
	Color HIGHLIGHTED = new Color(110, 211, 255);
	Color OUTLINE;
	Font boardFont;

	/**
	 * Chessboard constructor. Takes in the needed SWT assets for everything that
	 * needs to be done. Utilizes the display to get the color needed for outlining
	 * and the font needed for the board coordinates.
	 * 
	 * @param shell:   UI's shell.
	 * @param display: UI's display
	 */
	public Chessboard(Canvas canvas, Shell shell, Display display) {
		this.shell = shell;
		this.display = display;
		this.OUTLINE = display.getSystemColor(SWT.COLOR_BLACK);
		this.boardFont = new Font(canvas.getDisplay(), "Tahoma", 15, SWT.BOLD);
	}

	/**
	 * Creation method, only ran once for initialization. Makes an empty board,
	 * creating the tiles themselves by utilizing the Tile class.
	 * 
	 * @param gc: event gc, let's us draw on the canvas
	 */
	public void createBoardData(GC gc) {
		board = new Tile[8][8]; // list of tiles
		boolean boardColor = true; // if true, tile is white and vice versa
		Color w = new Color(255, 221, 153);
		Color b = new Color(204, 136, 0); // tile colors, white and black respectively

		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {

				// create the tiles with the proper color
				if (boardColor) {
					board[y][x] = new Tile(w, OUTLINE, x * SQUARE_WIDTH + 50, y * SQUARE_WIDTH, SQUARE_WIDTH);
				} else {
					board[y][x] = new Tile(b, OUTLINE, x * SQUARE_WIDTH + 50, y * SQUARE_WIDTH, SQUARE_WIDTH);
				}

				// alternating tile colors
				boardColor = !boardColor;
			}
			boardColor = !boardColor;
		}
	}

	/**
	 * Adds all the chess pieces to the board. Used for the new game only.
	 */
	public void setAllPieces() {
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				board[y][x].setPiece(makePiece(x, y)); // set the piece using a helper function
			}
		}
	}

	/**
	 * Helper function responsible for making the pieces and putting them in their
	 * starting positions.
	 * 
	 * @param x: row in board
	 * @param y: column in board
	 * @return Piece that was made
	 */
	private Piece makePiece(int x, int y) {

		// false == black
		boolean pieceColor = false;

		if (y == 0) { // black specialty pieces
			if (x == 0 || x == 7) {
				return (new Rook(pieceColor, shell));
			} else if (x == 0 || x == 7) {
				return (new Rook(pieceColor, shell));
			} else if (x == 1 || x == 6) {
				return (new Knight(pieceColor, shell));
			} else if (x == 2 || x == 5) {
				return (new Bishop(pieceColor, shell));
			} else if (x == 4) {
				return (new King(pieceColor, shell));
			} else if (x == 3) {
				return (new Queen(pieceColor, shell));
			}
		} else if (y == 1) {
			return (new Pawn(pieceColor, shell));
		} // black pawns

		// true == white
		pieceColor = !pieceColor;

		if (y == 6) {
			return (new Pawn(pieceColor, shell));
		} // white pawns
		else if (y == 7) { // white specialty pieces
			if (x == 0 || x == 7) {
				return (new Rook(pieceColor, shell));
			} else if (x == 0 || x == 7) {
				return (new Rook(pieceColor, shell));
			} else if (x == 1 || x == 6) {
				return (new Knight(pieceColor, shell));
			} else if (x == 2 || x == 5) {
				return (new Bishop(pieceColor, shell));
			} else if (x == 4) {
				return (new King(pieceColor, shell));
			} else if (x == 3) {
				return (new Queen(pieceColor, shell));
			}
		}

		return null; // tile is given no piece
	}

	/**
	 * The for loop to draw the board. The helper function 'Drawing' holds onto all
	 * of the conditionals needed in the process.
	 * 
	 * @param x:  row in board
	 * @param y:  column in board
	 * @param gc: event gc, let's us draw on the canvas
	 */
	@Override
	public void draw(GC gc) {
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				drawing(x, y, gc);
			}
		}
	}

	/**
	 * The helper function 'Drawing'. Need to make sure that tiles are drawn the
	 * right color: if a piece is currently selected the tile is green, a tile is
	 * cyan if it's a valid place for the currently selected piece to move, and if
	 * neither of these are true, the original color stands. Also adds the board
	 * coordinates on the side (A-H, 1-8).
	 * 
	 * @param x:  row in board
	 * @param y:  column in board
	 * @param gc: event gc, let's us draw on the canvas
	 */
	private void drawing(int x, int y, GC gc) {
		Tile t = board[x][y]; // tile in question
		Piece p = t.getPiece(); // piece in question

		if (p != null) { // if there's a piece, could be selected or taken by selected
			if (p.isSelected()) {
				t.draw(gc, SELECTED);
			}

			else if (selectedCoordinates != null && selectedCoordinates.contains(new Coordinate(y, x))) {
				t.draw(gc, HIGHLIGHTED);
			}

			else {
				t.draw(gc);
			}
		}

		else { // no piece but could be a valid place for currently selected to move to
			if (selectedCoordinates != null && selectedCoordinates.contains(new Coordinate(y, x))) {
				t.draw(gc, HIGHLIGHTED);
			} else {
				t.draw(gc);
			}
		}

		// adding the board coordinates
		if (x == 0) {
			addBoardCoords(gc, x, y);
		}
		if (y == 7) {
			addBoardCoords(gc, x, y);
		}
	}

	/**
	 * The helper function of 'Drawing'. Makes sure the board coordinates of A-H and
	 * 1-8 are drawn in the appropriate locations.
	 * 
	 * @param x:  row in board
	 * @param y:  column in board
	 * @param gc: event gc, let's us draw on the canvas
	 */
	private void addBoardCoords(GC gc, int x, int y) {
		gc.setFont(boardFont);
		gc.setBackground(BG);
		gc.setForeground(SIDE);

		// 1-8
		if (x == 0) {
			String yCoord = verticalCoords[y];
			gc.drawText(yCoord, x * SQUARE_WIDTH + 20, y * SQUARE_WIDTH + 25);
		}

		// A-H
		if (y == 7) {
			String xCoord = horizontalCoords[x];
			gc.drawText(xCoord, x * SQUARE_WIDTH + 60 + 20, (y + 1) * SQUARE_WIDTH + 15);
		}
	}

	// -- getters

	/**
	 * Accepts the pixel coordinates, converts them to a valid index on the board,
	 * and returns an array containing those coordinates.
	 * 
	 * @param x a float that represents the y coordinate on the canvas where the
	 *          user clicked.
	 * @param y a float that represents the y coordinate on the canvas where the
	 *          user clicked.
	 * @return an Integer array of length two where index 0 is x and index 1 is the
	 *         y board coordinate.
	 */
	public int[] getBoardIndex(float x, float y) {
		x -= BOARD_COORD_OFFSET / 2;
		float indexX = x / SQUARE_WIDTH;
		float indexY = y / SQUARE_WIDTH;

		int indX = (int) indexX;
		int indY = (int) indexY;

		if (indX < 0 || indX > 7 || indY < 0 || indY > 7 || indexX < 0) {
			System.out.println("Clicking outside the board!");
			return null;
		}
		return new int[] { indX, indY };
	}

	/**
	 * Board getter, returns the board.
	 * 
	 * @return board: the list of tiles that make up the board.
	 */
	public Tile[][] getBoard() {
		return board;
	}

	// -- moving / piece methods

	/**
	 * This method returns a specific colored king piece so that it may be used for
	 * check and checkmate calculations.
	 * <P>
	 * It could be improved by removing the need to iterate through the entire board
	 * every time by saving the king's location or saving it as a field that has a
	 * reference to the King object.
	 *
	 * @param getWhite, true if obtaining white king, false if obtaining black king.
	 * @return the desired King piece.
	 */
	public King getKing(boolean getWhite) {
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (board[row][col].getPiece() instanceof King) {
					if (getWhite) {
						if (board[row][col].getPiece().isWhite()) {
							return (King) board[row][col].getPiece();
						}
					} else {
						if (!board[row][col].getPiece().isWhite()) {
							return (King) board[row][col].getPiece();
						}
					}
				}
			}
		}
		// This should never be reached.
		System.out.println("Chessboard.java - no king found, exiting...");
		System.exit(403);
		return null;
	}

	/**
	 * This method returns a boolean which indicates if the move to the desired x/y
	 * coordinate is a legal move that the piece can made.
	 * <P>
	 * It can be improved by simply checking if the x/y coordinate is within the
	 * {@link #selectedCoordinates} field, although this would require changes in
	 * the Pawn classes' validMove() method as that method call sets state/move
	 * specific booleans that would thus no longer be updated.
	 * 
	 * @see pieces.Pawn#validMove(int, int, Tile[][])
	 * @param x          any integer between 0-7 inclusive
	 * @param y          any integer between 0-7 inclusive
	 * 
	 * @param piece      the Piece object that is to be moved
	 * @param whitesTurn true if white is making the move, false if not
	 * @return true if the move is legal, false if not.
	 */
	public boolean validMoveMade(int x, int y, Piece piece, boolean whitesTurn) {
		return piece.validMove(x, y, board) && validCheckMove(x, y, piece, whitesTurn);

	}

	/**
	 * This method updates the piece's coordinates to the new x/y coordinate and
	 * ensures that the old tiles piece field is set to null. Additionally,
	 * updateLocation() is called so that state specific fields are updated
	 * accordingly. If it moves onto a tile that already has a piece, it set's that
	 * piece's dead field to true so that the robot won't select that piece to be
	 * moved.
	 * 
	 * @see pieces.Pawn#updateLocation(int, int)
	 * @see pieces.King#updateLocation(int, int)
	 * @see pieces.Rook#updateLocation(int, int)
	 * @param x     any integer between 0-7 inclusive.
	 * @param y     any integer between 0-7 inclusive.
	 * @param piece the Piece object that is to be moved to x/y.
	 */
	public void movePiece(int x, int y, Piece piece) {
		board[piece.getY()][piece.getX()].setPiece(null);
		System.out.println("MOVING" + piece + " to " + x + ',' + y);
		piece.updateLocation(x, y);
		if (board[y][x].getPiece() != null)
			board[y][x].getPiece().killPiece(); // killing previous piece for robot class
		board[y][x].setPiece(piece);

	}

	/**
	 * This method is responsible for returning a piece (or null) that the player
	 * wants to move.
	 * <P>
	 * This method implementation can be greatly improved by making changes in the
	 * Piece subclasses via a revised setLocation() method which simply changes the
	 * x/y field of the piece due to updateLocation() changes state specific
	 * variables that thus must be saved and restored whenever this method gets
	 * called.
	 * 
	 * @see pieces.Pawn#updateLocation(int, int)
	 * @see pieces.King#updateLocation(int, int)
	 * @see pieces.Rook#updateLocation(int, int)
	 * @param x         any integer between 0-7 inclusive
	 * @param y         any integer between 0-7 inclusive
	 * @param piece     the Piece object that is to be moved to x/y
	 * @param getWhite, true if obtaining white king, false if obtaining black king.
	 * @return true if the movement does not put/have the king in check, false if it
	 *         does.
	 * @return true if the move does not break any non-valid check moves, false if
	 *         it does.
	 */
	public boolean validCheckMove(int x, int y, Piece piece, boolean getWhite) {
		Pawn p = null;
		boolean extra1 = false;
		boolean extra2 = false;
		boolean extra3 = false;
		boolean isDead = false;
		Rook r = null;
		King k = null;
		// Saving variables that would be overwritten due to move being made
		if (piece instanceof Pawn) {
			p = (Pawn) piece;
			extra1 = p.firstMove;
			extra2 = p.enPassantable;
			extra3 = p.didEnPassant; // Likely not necessary but saving in case
		} else if (piece instanceof Rook) {
			r = (Rook) piece;
			extra1 = r.moved;
		} else if (piece instanceof King) {
			k = (King) piece;
			extra1 = k.moved;
			extra2 = k.castlingMoveMade;
		}
		int oldX = piece.getX();
		int oldY = piece.getY();
		Piece oldPiece = board[y][x].getPiece();
		if (oldPiece != null) {
			isDead = oldPiece.isDead();
		}
		// Simulate a move
		this.movePiece(x, y, piece);
		determineKingCheckStatus(getWhite);
		King newKing = getKing(getWhite);
		boolean checked = newKing.checked;
		// Restoring board to previous state
		this.movePiece(oldX, oldY, piece);
		if (piece instanceof Pawn) {
			p.firstMove = extra1;
			p.enPassantable = extra2;
			p.didEnPassant = extra3; // Likely not necessary but saving in case
			board[oldY][oldX].setPiece(p);
		} else if (piece instanceof Rook) {
			r.moved = extra1;
			board[oldY][oldX].setPiece(r);
		} else if (piece instanceof King) {
			k.moved = extra1;
			k.castlingMoveMade = extra2;
			board[oldY][oldX].setPiece(k);
		}
		if (oldPiece != null) {
			oldPiece.setDeadStatus(isDead);
		}
		board[y][x].setPiece(oldPiece);
		determineKingCheckStatus(getWhite);
		// Negate checked because if it's true, king is in check and thus not valid.
		return !checked;
	}

	/**
	 * The purpose of this method is to determine if a king is now in check or not.
	 * It is called within updateBoard(). The player's color is passed so that only
	 * the desired player's king is analyzed. [made public for robot]
	 * 
	 * @param getWhite, true if player is white, false if not
	 * @return true if the king is in check, false if not.
	 */
	public boolean determineKingCheckStatus(boolean getWhite) {
		King king = getKing(getWhite);
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (board[row][col].getPiece() != null && !(board[row][col].getPiece() instanceof King)) {
					if (board[row][col].getPiece().validMove(king.getX(), king.getY(), board)) {
						// update the check field and update array with newly updated king.
						king.setCheck();
						board[king.getY()][king.getX()].setPiece(king);
						return true;
					}
				}
			}
		}
		// update the check field and update array with newly updated king.
		king.checkEvaded();
		board[king.getY()][king.getX()].setPiece(king);
		return false;
	}

	/**
	 * This method is responsible for returning a piece (or null) that the player
	 * wants to move.
	 * 
	 * @param x        any integer between 0-7 inclusive.
	 * @param y        any integer between 0-7 inclusive.
	 * @param isWhite, true if player is white, false if not
	 * @return a piece object if the piece can be selected by the player, null if
	 *         empty/opponents piece.
	 */
	public Piece selectPiece(int x, int y, boolean playerIsWhite) {
		if (this.board[y][x].getPiece() != null && this.board[y][x].getPiece().isWhite() == playerIsWhite) {
			return this.board[y][x].getPiece();
		}
		return null;
	}

	/**
	 * This simply updates the Tile[][] array so that the piece field is null.
	 * 
	 * @param xCoord any integer between 0-7 inclusive.
	 * @param yCoord any integer between 0-7 inclusive.
	 */
	public void removePiece(int xCoord, int yCoord) {
		this.board[yCoord][xCoord].setPiece(null);
	}

	/**
	 * This method is responsible for updating the board accordingly after a valid
	 * move has been made. It returns a boolean which indicates if the game is over
	 * or not, and if so, who won.
	 * 
	 * @param xCoord        any integer between 0-7 inclusive.
	 * @param yCoord        any integer between 0-7 inclusive.
	 * @param selectedPiece the piece that is to be moved to xCoord, yCoord.
	 * @return an integer, 0 if checkmate not met, 1 if white won, and 2 if black
	 *         won.
	 */
	public int updateBoard(int xCoord, int yCoord, Piece selectedPiece) {
		int gameOver = 0;
		// System.out.println("Chessboard.java - Moving..." + selectedPiece);
		this.movePiece(xCoord, yCoord, selectedPiece);
		// System.out.println("Chessboard.java - Piece updated! " + selectedPiece);
		this.checkCastleMoveMade(selectedPiece);
		this.checkEnPassantMoveMade(selectedPiece);
		this.checkPromotion(selectedPiece);
		if (this.determineKingCheckStatus(!selectedPiece.isWhite())) {
			System.out.println("King in check!");
			gameOver = this.determineCheckMate(selectedPiece);
		}
		;
		return gameOver;
	}

	/**
	 * This method checks if the en passant move has been made so that the board may
	 * be updated accordingly.
	 * 
	 * @param selectedPiece the piece that has been moved.
	 */
	private void checkEnPassantMoveMade(Piece selectedPiece) {
		if (selectedPiece instanceof Pawn) {
			Pawn pawn = (Pawn) selectedPiece;
			if (pawn.didEnPassant) {
				int y = pawn.getY();
				if (pawn.isWhite()) {
					y++;
				} else {
					y--;
				}
				this.removePiece(selectedPiece.getX(), y);
				// Sets didEnPassant to false so that board doesn't get updated incorrectly.
				pawn.removeEnPassantMove();
			}
		}
	}

	/**
	 * This method determines if the Piece object moved has castled and updates the
	 * board accordingly if it did.
	 * 
	 * @param piece the piece that has been moved.
	 */
	private void checkCastleMoveMade(Piece piece) {
		if (piece instanceof King) {
			King k = (King) piece;
			if (k.castlingMoveMade) {
				this.removePiece(k.getX(), k.getY());
				King newKing = new King(piece.isWhite(), shell);
				newKing.moved = true; // Set to true so castling can't occur again
				Rook newRook = new Rook(piece.isWhite(), shell);
				newRook.moved = true; // Set to true so castling can't occur again
				if (k.getX() == 7 && k.getY() == 7) {
					board[7][5].setPiece(newRook);
					board[7][6].setPiece(newKing);
				} else if (k.getX() == 0 && k.getY() == 7) {
					board[7][3].setPiece(newRook);
					board[7][2].setPiece(newKing);
				} else if (k.getX() == 0 && k.getY() == 0) {
					board[0][3].setPiece(newRook);
					board[0][2].setPiece(newKing);
				} else if (k.getX() == 7 && k.getY() == 0) {
					board[0][5].setPiece(newRook);
					board[0][6].setPiece(newKing);
				} else {
					System.out.println(
							"Chessboard.java - checkCastleMoveMade - this block should never be reached printing king status and exiting: "
									+ k);
					System.exit(403);
				}
				// Set castlingMoveMade to false so no bugs occur when piece is moved later.
				k.castlingMoveMade = false;
			}
		}
	}

	/**
	 * This method determines if the game is over. Additionally, it returns an int
	 * which indicates which player (if any) has won. [made public for robot]
	 * 
	 * @param movedPiece, the piece that was moved this turn. Used to determine next
	 *                    player's color.
	 * @return an integer, 0 if checkmate not met, 1 if white won, and 2 if black
	 *         won.
	 */
	public int determineCheckMate(Piece movedPiece) {
		for (int row = 0; row <= 7; row++) {
			for (int col = 0; col <= 7; col++) {
				if (movedPiece.hasEnemyPiece(col, row, board)) {
					Piece otherPlayerPiece = board[row][col].getPiece();
					List<Coordinate> coordinates = otherPlayerPiece.generateMoves(board);
					for (Coordinate coord : coordinates) {
						if (validMoveMade(coord.getX(), coord.getY(), otherPlayerPiece, otherPlayerPiece.isWhite())) {
							System.out.println("Chessboard.java - validMove possible, checkmate not met");
							return 0;
						}
					}
				}
			}
		}
		// If this part is reached no validMoveMade returned true, thus game over!
		System.out.println("Chessboard.java - The opponent can not make any valid moves! Game over!");
		// This section is used to determine which player has won.
		if (movedPiece.getColor() == "White") {
			return 1;
		} else {
			return 2;
		}
	}

	/**
	 * This method promotes the pawn if it is on the opposite side of the board. It
	 * opens a display that allows the user to choose between a Queen, Knight,
	 * Bishop, or Rook, and updates the board accordingly with their selection.
	 * 
	 * @param selectedPiece the Piece object that has been moved.
	 */
	private void checkPromotion(Piece selectedPiece) {
		if (selectedPiece instanceof Pawn) {
			Pawn pawn = (Pawn) selectedPiece;
			if (pawn.promotion()) {
				String decision = new PawnPromotionDisplay().start(display);
				Object piece = new Queen(selectedPiece.isWhite(), shell);
				if (decision.equals("Rook")) {
					piece = new Rook(selectedPiece.isWhite(), shell);
				} else if (decision.equals("Knight")) {
					piece = new Knight(selectedPiece.isWhite(), shell);
				} else if (decision.equals("Bishop")) {
					piece = new Bishop(selectedPiece.isWhite(), shell);
				}
				((Piece) piece).updateLocation(pawn.getX(), pawn.getX()); // TODO fix?
				board[selectedPiece.getY()][selectedPiece.getX()].setPiece((Piece) piece);
				this.promotion = true;
			}
		}
	}

	/**
	 * This method highlights the possible moves that a piece may be moved onto and
	 * saves them to the {@link #selectedCoordinates} field.
	 * 
	 * @param selectedPiece the Piece object that may be moved by the player.
	 */
	public void highlightCoordinates(Piece selectedPiece) {
		selectedCoordinates = selectedPiece.generateMoves(board);
		filterCheckMoves(selectedPiece, selectedCoordinates);
	}

	/**
	 * This method filters non-valid check related moves from a list of coordinates
	 * that a piece could potentially move to. It does so by calling
	 * {@link Chessboard#validCheckMove(int, int, Piece, boolean)} and is called in
	 * the {@link Chessboard#highlightCoordinates(Piece)} method.
	 * <P>
	 * Currently it only filters the {@link Chessboard#selectedCoordinates} List,
	 * however it could easily be repurposed to filter all generateMove() methods.
	 * 
	 * @param piece       the Piece object that is to be moved.
	 * @param coordinates a List of Coordinate objects that represent possible moves
	 *                    piece can move to.
	 */
	private void filterCheckMoves(Piece piece, List<Coordinate> coordinates) {
		for (int i = 0; i < selectedCoordinates.size(); i++) {
			Coordinate c = selectedCoordinates.get(i);
			if (!validCheckMove(c.x, c.y, piece, piece.isWhite())) {
				selectedCoordinates.remove(i);
				i--;
			}
		}
	}

	/**
	 * Un-highlights possible moves by setting selectedCoordinates to null.
	 */
	public void unhighlightCoordinates() {
		selectedCoordinates = null;
	}

	/**
	 * Returns tile at row y, column x.
	 * 
	 * @param x any integer between 0 and 7 inclusive.
	 * @param y any integer between 0 and 7 inclusive.
	 * @return a Tile object at the desired location.
	 */
	public Tile getTile(int x, int y) {
		return board[y][x];
	}

	/**
	 * This method is used to determine if promotion has occurred on the board. It
	 * is used so that an opponent is not able to pick the enemies piece. If a
	 * player is playing with a robot however, they are able to choose what piece
	 * the robot gets.
	 * 
	 * @return true if promotion has occurred, false if not.
	 */
	public boolean getPromotion() {
		if (this.promotion) {
			this.promotion = false; // set to false so that promotion doesn't constantly occur.
			return true;
		}
		return false;
	}

	/**
	 * This is a debug method used to print the current state of the board in text
	 * form, although it can be improved via better formatting.
	 */
	public void printBoard() {
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				System.out.print(this.board[row][col] + " ");
			}
			System.out.println();
		}
	}
}