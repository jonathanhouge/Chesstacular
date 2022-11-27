package pieces;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

public class Queen extends Piece {
	private Rook rook;
	private Bishop bishop;
	
	String whitePiece = "wq.png";
	String blackPiece = "bq.png";
	int points = 9;

	public Queen(boolean white, Shell shell) {
		super(white);
		if (white) {
			setImage(new Image(shell.getDisplay(), "images/" + whitePiece));
		} else {
			setImage(new Image(shell.getDisplay(), "images/" + blackPiece));
		}
	}

	@Override
	public boolean move(int x, int y) {
		if (rook.move(x, y) || bishop.move(x, y)) {
			rook.updateLocation(x, y);
			bishop.updateLocation(x, y);
			this.updateLocation(x,y);

			return true;
		}
		return false; // must move the rook and bishop with the queen however
	}

}
