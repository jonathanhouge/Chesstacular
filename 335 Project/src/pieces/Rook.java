package pieces;

public class Rook extends Piece {

	public Rook(boolean white) {
		super(white);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean move(int x, int y) {
		if(this.getX() == x) { // possible horizontal movement
			if(0 < y && y < 8) {
				//collision detection here

			}
		}else if(this.getY() == y){ // possible vertical movement
			if(0 < x && x < 8) {
				//collision detection here

			}
		}
		return false;
	}

	public void highlightMoves() {
		
	}
}
