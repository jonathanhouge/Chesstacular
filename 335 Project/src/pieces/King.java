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
			System.out.println("KING - setting castlingMoveMade to true!!!");
			return true;
		}
		return false;
	}
	@Override
    public void updateLocation(int x, int y) {
    	System.out.println("King update location called! CastlingMoveMade is equal to: " + this.castlingMoveMade);
    	super.updateLocation(x,y);
    	this.moved = true;
    }
	@Override
	public boolean hasNoCollisions(int x, int y, Tile[][] tiles) {
		if (tiles[y][x].getPiece() != null && tiles[y][x].getPiece().isWhite() == this.isWhite()) {
			if((x==7 && y ==7 ) || (x==0 && y==7)||(x==7 && y==0) || (x==0 && y==0)) {//TODO add remaining coords
				return true;
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
			//System.out.println("KING.JAVA - king not moved and not in check");
			if (this.isWhite()) {
				if (hasFriendlyRook(7,7,tiles)) {//right rook LOOKS GOOD
					Rook r = (Rook) tiles[7][7].getPiece();
					if(!r.moved) {
						//System.out.println("KING.JAVA - king and right rook not moved");
						List<Coordinate> positions = new ArrayList<>();
						positions.add(new Coordinate(5,7));
						positions.add(new Coordinate(6,7));
						positions.add(new Coordinate(7,7));
						boolean underAttack = underAttack(positions,tiles);
						if(!underAttack && !tiles[7][5].hasPiece() && !tiles[7][6].hasPiece()) {
							//System.out.println("KING.JAVA - castle added!");
							coordinates.add(new Coordinate(7,7));
						}
					}
				}
				if (hasFriendlyRook(0,7,tiles)) {//left rook
					Rook r = (Rook) tiles[7][0].getPiece();
					if(!r.moved) {
						//System.out.println("KING.JAVA - king and left rook not moved");
						List<Coordinate> positions = new ArrayList<>();
						positions.add(new Coordinate(0,7));
						positions.add(new Coordinate(1,7));
						positions.add(new Coordinate(2,7));
						positions.add(new Coordinate(3,7));
						boolean underAttack = underAttack(positions,tiles);
						if(!underAttack && !tiles[7][1].hasPiece() && !tiles[7][2].hasPiece()&& !tiles[7][3].hasPiece()) {
							//System.out.println("KING.JAVA - castle added!");
							coordinates.add(new Coordinate(0,7));
						}else {
							//System.out.println("Either under attack or has piece");
						}
					}
				}
			} else {
				if (hasFriendlyRook(7,0,tiles)) {//right rook
					Rook r = (Rook) tiles[0][7].getPiece();
					if(!r.moved) {
						//System.out.println("KING.JAVA - king and rook not moved");
						List<Coordinate> positions = new ArrayList<>();
						positions.add(new Coordinate(5,0));
						positions.add(new Coordinate(6,0));
						positions.add(new Coordinate(7,0));
						boolean underAttack = underAttack(positions,tiles);
						if(!underAttack && !tiles[0][5].hasPiece() && !tiles[0][6].hasPiece()) {
							//System.out.println("KING.JAVA - castle added!");
							coordinates.add(new Coordinate(7,0));
						}
					}
				}
				if (hasFriendlyRook(0,0,tiles)) {//left rook
					Rook r = (Rook) tiles[0][0].getPiece();
					if(!r.moved) {
						//System.out.println("KING.JAVA - king and rook not moved");
						List<Coordinate> positions = new ArrayList<>();
						positions.add(new Coordinate(0,0));
						positions.add(new Coordinate(1,0));
						positions.add(new Coordinate(2,0));
						positions.add(new Coordinate(3,0));
						boolean underAttack = underAttack(positions,tiles);
						if(!underAttack && !tiles[0][1].hasPiece() && !tiles[0][2].hasPiece()&& !tiles[0][3].hasPiece()) {
							//System.out.println("KING.JAVA - castle added!");
							coordinates.add(new Coordinate(0,0));
						}
					}
				}
			}
		}

		return coordinates;
	}
	private boolean hasFriendlyRook(int x, int y, Tile[][] tiles) {
		return tiles[y][x].hasPiece() && tiles[y][x].getPiece() instanceof Rook && tiles[y][x].getPiece().isWhite() == this.isWhite(); 
	}
	private boolean underAttack(List<Coordinate> positions,Tile[][] tiles) {
		for(int row = 0;row<8;row++) {
			for(int col = 0;col<8;col++) {
				if(tiles[7][7].hasPiece() && (tiles[7][7].getPiece().isWhite() != this.isWhite())){
					List<Coordinate> enemyMoves = tiles[7][7].getPiece().generateMoves(tiles);
					for(Coordinate c: enemyMoves) { 
						//TODO edge case, generateMoves() does not filter out non-valid check moves
						if(positions.contains(c)) {
							System.out.println("KING.JAVA - Castle move has space under attack! Castling not viable");
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
