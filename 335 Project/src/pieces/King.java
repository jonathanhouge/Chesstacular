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
	}

	@Override
	public boolean standardMove(int x, int y) {
		int xDistance = Math.abs(x-this.getX());
		int yDistance = Math.abs(y-this.getY());
		
		if(yDistance <= 1 && xDistance <= 1) {
			return true;
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

	public void inCheck() {
		this.checked = true;
	}
	public void checkEvaded() {
		this.checked = false;
	}

	@Override
	public List<Coordinate> generateMoves(Tile[][] tiles) {
		List<Coordinate> coordinates = new ArrayList<>();
		for(int row = -1;row<= 1;row++) {
			for(int col = -1; col <= 1;col++) {
				if(getX()+col < 0 || getY()+row < 0 || getX()+col > 7 || getY()+row >7) {
					break;
				}
				if(!tiles[getY()+row][getX()+col].hasPiece()) {
					coordinates.add(new Coordinate(getX()+col,getY()+row));
				}else {
					if(tiles[getY()+row][getX()+col].getPiece().isWhite()!= this.isWhite()) {
						coordinates.add(new Coordinate(getX()+col,getY()+row));
					}
				}
			}
		}
		return coordinates;
	}

}
