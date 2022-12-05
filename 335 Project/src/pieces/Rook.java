package pieces;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import game.Coordinate;
import game.Tile;

public class Rook extends Piece {

    String whitePiece = "wr.png";
    String blackPiece = "br.png";
    int points = 5;

    public boolean moved;

    public Rook(boolean white, Shell shell) {
        super(white);
        if (white) {
            setImage(new Image(shell.getDisplay(), "images/" + whitePiece));
        } else {
            setImage(new Image(shell.getDisplay(), "images/" + blackPiece));
        }
        this.name = "ROOK";
        this.moved = false;
    }

    @Override
    public boolean standardMove(int x, int y) {
        if (this.getX() == x) { // possible horizontal movement
            if (0 <= y && y < 8) {
                return true;
            }
        } else if (this.getY() == y) { // possible vertical movement
            if (0 <= x && x < 8) {
                return true;
            }
        }
        return false;
    }
    @Override
    public void updateLocation(int x, int y) {
    	System.out.println("Rook update location called! Moving to " + x +","+ y);
    	super.updateLocation(x,y);
    	this.moved = true;
    }

    /**
     * {@inheritDoc} Additionally, it also checks to see if the piece collides with
     * another piece while attempting to move to the new x/y coordinate.
     */
    @Override
    public boolean hasNoCollisions(int x, int y, Tile[][] tiles) {
        // first, check to see if the desired spot will collide with players own piece
        if (tiles[y][x].getPiece() != null && tiles[y][x].getPiece().isWhite() == this.isWhite()) {
            return false;
        }

        // finally, check to see nothing is in the path of the movement
        if (x < this.getX()) { // left
            for (int i = x + 1; i < this.getX(); i++) {
                if (tiles[y][i].hasPiece()) {
                    return false;
                }
            }
        } else if (x > this.getX()) {// right
            for (int i = x - 1; i > this.getX(); i--) {
                if (tiles[y][i].hasPiece()) {
                    return false;
                }
            }
        } else if (y < this.getY()) { // down
            for (int i = y + 1; i < this.getY(); i++) {
                if (tiles[i][x].hasPiece()) {
                    return false;
                }
            }
        } else if(y > this.getY()) { // up
            for (int i = y - 1; i > this.getY(); i--) {
                if (tiles[i][x].hasPiece()) {
                    return false;
                }
            }
        }else {
            return false;
        }
        return true;
    }

	@Override
	public List<Coordinate> generateMoves(Tile[][] tiles) {
		 List<Coordinate> coordinates = new ArrayList<>();
		//UP
		int y = getY() - 1;
		while(y >= 0) {
			if(!tiles[y][getX()].hasPiece()) {
				coordinates.add(new Coordinate(getX(),y));
			}else {
				if(tiles[y][getX()].getPiece().isWhite() != this.isWhite()) {
					coordinates.add(new Coordinate(getX(),y));
				}
				break;
			}
			y--;
		}
		//DOWN
		y = getY() + 1;
		while(y <=7) {
			if(!tiles[y][getX()].hasPiece()) {
				coordinates.add(new Coordinate(getX(),y));
			}else {
				if(tiles[y][getX()].getPiece().isWhite() != this.isWhite()) {
					coordinates.add(new Coordinate(getX(),y));
				}
				break;
			}
			y++;
		}
		//RIGHT
		int x = getX() + 1;
		while(x <= 7) {
			if(!tiles[getY()][x].hasPiece()) {
				coordinates.add(new Coordinate(x,getY()));
			}else {
				if(tiles[getY()][x].getPiece().isWhite() != this.isWhite()) {
					coordinates.add(new Coordinate(x,getY()));
				}
				break;
			}
			x++;
		}
		//LEFT
		x = getX() - 1;
		while(x >= 0) {
			if(!tiles[getY()][x].hasPiece()) {
				coordinates.add(new Coordinate(x,getY()));
			}else {
				if(tiles[getY()][x].getPiece().isWhite() != this.isWhite()) {
					coordinates.add(new Coordinate(x,getY()));
				}
				break;
			}
			x--;
		}
		return coordinates;
	}

}
