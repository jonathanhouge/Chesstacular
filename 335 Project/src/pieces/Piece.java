package pieces;
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
	//private abstract boolean isMoveValid(int x, int y);
}
