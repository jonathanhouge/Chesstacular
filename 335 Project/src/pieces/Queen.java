package pieces;

import java.util.ArrayList;

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
	public boolean standardMove(int x, int y) {
		if (rook.standardMove(x, y) || bishop.standardMove(x, y)) {
			return true;
		}
		return false; // must move the rook and bishop with the queen however
	}

	@Override
	public boolean hasNoCollisions(int x, int y, Tile[][] tiles) {
		return false;
	}

}
