package pieces;

public class Queen extends Piece {
	private Rook rook;
	private Bishop bishop;
	
	String whitePiece = "wq.png";
	String blackPiece = "bq.png";

	public Queen(boolean white) {
		super(white);
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
