package pieces;

import org.eclipse.swt.graphics.GC;

/**
 * This abstract class is responsible for outlining what subclasses of Piece can do.
 *  Subclasses of Piece are:  King, Queen, Bishop, Knight, Pawn, and Rook.
 *  
 *  @author Julius Ramirez
 */
public abstract class Piece {
	private boolean white;
	private int x;
	private int y;
	
	public Piece(boolean white) {
		this.white = white;
	}
	public boolean isWhite() {
		return this.white;
	}
	public abstract boolean move(int x,int y);
	public int getX(){
		return this.x;
	}
	public int getY(){
		return this.y;
	}
	public void updateLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
//	public void draw(GC gc) {
//		gc.drawImage(img, x*SQUARE_WIDTH+10, y*SQUARE_WIDTH+10);
//	}
	//private abstract boolean isMoveValid(int x, int y);
}
