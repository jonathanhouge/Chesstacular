package pieces;

public class Pawn extends Piece {
	
	String whitePiece = "wp.png";
	String blackPiece = "bp.png";
	int points = 1;
	

	boolean firstMove;
	boolean enPassant = false; // if this is true, en passant may be done on this pawn
	public Pawn(boolean white) {
		super(white);
		this.firstMove = true;
	}

	@Override
	public boolean move(int x, int y) {
		if (this.getX() == x) { // ensures only vertical movement
			if (this.isWhite()) {
				if (y > this.getY()) {
					return false; // false due to downward movement
				}
			} else {
				if (y < this.getY()) {
					return false; // false due to upwward movement
				}
			}

			int yDistance = Math.abs(this.getY() - y);
			if (!firstMove&& yDistance == 1) {
				updateLocation(x,y);
				enPassant = false;
				return true;
			} else {
				
				if(yDistance == 2) {
					firstMove = false;
					updateLocation(x,y);
					enPassant = true;
					return true;
				}else if (yDistance == 1) {
					firstMove = false;
					updateLocation(x,y);
					return true;
				}
				
			}
		}
		return false; // invalid move made

	}

	public boolean promotion() {
		if (this.isWhite()) {
			return this.getY() == 0;
		}
		return this.getY() == 7;
	}
}
