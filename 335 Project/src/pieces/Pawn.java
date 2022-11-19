package pieces;

public class Pawn extends Piece {

	boolean firstMove;

	public Pawn(boolean white) {
		super(white);
		this.firstMove = true;
		// TODO Auto-generated constructor stub
	}


	@Override
	public boolean move(int x, int y) {
		if (this.getX() == x) { // ensures only vertical movement
			if (y < this.getY()) { // ensures movement upwards
				if (!firstMove) {
					return this.getY() - y == 1;
				} else {
					firstMove = false;
					return this.getY() - y <= 2;
				}
			}

		}
		return false; // invalid move made

	}

}
