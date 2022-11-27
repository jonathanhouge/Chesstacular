package pieces;

import java.util.ArrayList;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

//import Tile;

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
	private Image visual;
	private static int SQUARE_WIDTH = 80; // should it be all caps?
	
	public static void setWidth(int width) {
		SQUARE_WIDTH = width;
	}
	public Piece(boolean white) {
		this.white = white;
	}
	public boolean isWhite() {
		return this.white;
	}
	public abstract boolean move(int x,int y);
//	public boolean move(int x,int y, ArrayList<Tile> tiles) {
//		return this.standardMove(x,y) && this.hasNoCollisons(x,y,tiles);
//	}
	//public abstract boolean standardMove(int x2, int y2);
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
	public void setImage(Image image) {
		this.visual = image;
	}
	public void draw(GC gc) {
		gc.drawImage(visual, x*SQUARE_WIDTH+10, y*SQUARE_WIDTH+10);
	}
	//private abstract boolean isMoveValid(int x, int y);
}
