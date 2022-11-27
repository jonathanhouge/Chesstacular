package pieces;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

public class Rook extends Piece {
	
	String whitePiece = "wr.png";
	String blackPiece = "br.png";
	int points = 5;

	public Rook(boolean white, Shell shell) {
		super(white);
		if (white) {
			setImage(new Image(shell.getDisplay(), "images/" + whitePiece));
		} else {
			setImage(new Image(shell.getDisplay(), "images/" + blackPiece));
		}
	}

	@Override
	public boolean move(int x, int y) {
		if(this.getX() == x) { // possible horizontal movement
			if(0 < y && y < 8) {
				this.updateLocation(x, y);
				return true;
			}
		}else if(this.getY() == y){ // possible vertical movement
			if(0 < x && x < 8) {
				this.updateLocation(x, y);
				return true;
			}
		}
		return false;
	}

}
