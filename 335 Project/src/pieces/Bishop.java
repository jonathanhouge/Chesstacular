package pieces;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

public class Bishop extends Piece {

	String whitePiece = "wb.png";
	String blackPiece = "bb.png";
	int points = 3;

	public Bishop(boolean white, Shell shell) {
		super(white);

		if (white) {
			setImage(new Image(shell.getDisplay(), "images/" + whitePiece));
		} else {
			setImage(new Image(shell.getDisplay(), "images/" + blackPiece));
		}
	}

	@Override
	public boolean standardMove(int x, int y) {
		int xDistance = Math.abs(x - this.getX());
		int yDistance = Math.abs(y - this.getY());
		if (xDistance == yDistance) {
			this.updateLocation(x, y);
			return true;
		}
		return false;
	}

	@Override
	public boolean hasNoCollisions(int x, int y, Tile[][] tiles) {
		return false;
	}


}
