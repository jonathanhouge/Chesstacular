package pieces;

public class Bishop extends Piece {

	public Bishop(boolean white) {
		super(white);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean move(int x, int y) {
		int xDistance = Math.abs(x-this.getX());
		int yDistance = Math.abs(y-this.getY());
		return xDistance == yDistance;
	}

}
