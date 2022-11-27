package pieces;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

public class King extends Piece {
	
	String whitePiece = "wk.png";
	String blackPiece = "bk.png";
	
	boolean checked = false;

	public King(boolean white, Shell shell) {
		super(white);
		if (white) {
			setImage(new Image(shell.getDisplay(), "images/" + whitePiece));
		} else {
			setImage(new Image(shell.getDisplay(), "images/" + blackPiece));
		}
	}

	@Override
	public boolean move(int x, int y) {
		int xDistance = Math.abs(x-this.getX());
		int yDistance = Math.abs(y-this.getY());
		
		if(yDistance == 1 && xDistance == 1) {
			updateLocation(x,y);
			return true;
		}else if (yDistance == 1 && xDistance == 0) {
			updateLocation(x,y);
			return true;
		}else if(yDistance == 0 && xDistance == 1) {
			updateLocation(x,y);
			return true;
		}
		return false;
	}

}
