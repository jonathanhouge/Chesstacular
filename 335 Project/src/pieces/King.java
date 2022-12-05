package pieces;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import game.Coordinate;
import game.Tile;

public class King extends Piece {

	String whitePiece = "wk.png";
	String blackPiece = "bk.png";

	public boolean checked;
	public boolean moved;
	public boolean castlingMoveMade;
	public King(boolean white, Shell shell) {
		super(white);
		if (white) {
			setImage(new Image(shell.getDisplay(), "images/" + whitePiece));
		} else {
			setImage(new Image(shell.getDisplay(), "images/" + blackPiece));
		}
		this.name = "KING";
		this.checked = false;
		this.moved = false;
		castlingMoveMade = false;
	}

	@Override
	public boolean standardMove(int x, int y) {
		int xDistance = Math.abs(x - this.getX());
		int yDistance = Math.abs(y - this.getY());

		if (yDistance <= 1 && xDistance <= 1) {
			return true;
		}else if (!moved && yDistance == 0 && (xDistance == 3 || xDistance == 4)) {
			castlingMoveMade = true;
			return true;
		}
		return false;
	}

	@Override
	public boolean hasNoCollisions(int x, int y, Tile[][] tiles) {
		if (tiles[y][x].getPiece() != null && tiles[y][x].getPiece().isWhite() == this.isWhite()) {
			if((x==7 && y ==7)) {
				return castlingMoveMade;
			}
			return false;
		}
		return true;
	}

	public void inCheck() {
		this.checked = true;
	}

	public void checkEvaded() {
		this.checked = false;
	}

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
		if (!moved && !checked) {
			System.out.println("KING.JAVA - king not moved and not in check");
			if (this.isWhite()) {
				if (tiles[7][7].hasPiece() && tiles[7][7].getPiece() instanceof Rook
						&& tiles[7][7].getPiece().isWhite() == this.isWhite()) {//right rook
					Rook r = (Rook) tiles[7][7].getPiece();
					if(!r.moved) {
						System.out.println("KING.JAVA - king and rook not moved");
						List<Coordinate> positions = new ArrayList<>();
						positions.add(new Coordinate(5,7));
						positions.add(new Coordinate(6,7));
						positions.add(new Coordinate(7,7));
						boolean underAttack = false;
						for(int row = 0;row<8;row++) {
							for(int col = 0;col<8;col++) {
								if(tiles[7][7].hasPiece() && (tiles[7][7].getPiece().isWhite() != this.isWhite())){
									List<Coordinate> enemyMoves = tiles[7][7].getPiece().generateMoves(tiles);
									for(Coordinate c: enemyMoves) {
										if(positions.contains(c)) {
											System.out.println("KING.JAVA - Castle move has space under attack! Castling not viable");
											underAttack = true;
											break;
										}
									}
								}
							}
						}
						if(!underAttack && !tiles[7][5].hasPiece() && !tiles[7][6].hasPiece()) {
							System.out.println("KING.JAVA - castle added!");
							coordinates.add(new Coordinate(7,7));
						}
					}
				}
			} else {

			}
		}

		return coordinates;
	}

}
