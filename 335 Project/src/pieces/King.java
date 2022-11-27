package pieces;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

public class King extends Piece {
	
	String whitePiece = "wk.png";
	String blackPiece = "bk.png";

	public King(boolean white, Shell shell) {
		super(white);
		if (white) {
			setImage(new Image(shell.getDisplay(), "images/" + whitePiece));
		} else {
			setImage(new Image(shell.getDisplay(), "images/" + blackPiece));
		}
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
		// TODO Auto-generated method stub
		return false;
	}

}
