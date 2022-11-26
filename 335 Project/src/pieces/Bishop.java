package pieces;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

public class Bishop extends Piece {
	
	String whitePiece = "wb.png";
	String blackPiece = "bb.png";
	Image visual;

	public Bishop(boolean white, Shell shell) {
		super(white);
		
		if (white) { visual = new Image(shell.getDisplay(), "images/" + whitePiece); }
		else { visual = new Image(shell.getDisplay(), "images/" + blackPiece); }
	}

	@Override
	public boolean move(int x, int y) {
		int xDistance = Math.abs(x-this.getX());
		int yDistance = Math.abs(y-this.getY());
		if(xDistance == yDistance) {
			this.updateLocation(x, y);
			return true;
		}
		return false;
	}

}
