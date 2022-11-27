package pieces;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

public class Knight extends Piece {
	
	String whitePiece = "wkn.png";
	String blackPiece = "bkn.png";
	int points = 3;

	public Knight(boolean white, Shell shell) {
		super(white);
		if (white) {
			setImage(new Image(shell.getDisplay(), "images/" + whitePiece));
		} else {
			setImage(new Image(shell.getDisplay(), "images/" + blackPiece));
		}
	}

	@Override
	public boolean standardMove(int x, int y) {
		int xDistance = Math.abs(this.getX()-x);
		int yDistance = Math.abs(this.getY()-y);
		if(xDistance == 2) { // Horizontal L move
			return yDistance == 1;
		}else if (xDistance == 1) { // Vertical L move
			return yDistance == 2;
		}
		return false;
	}

	@Override
	public boolean hasNoCollisions(int x, int y, Tile[][] tiles) {
		// TODO Auto-generated method stub
		return false;
	}

}
