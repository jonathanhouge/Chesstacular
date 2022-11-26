package pieces;

public class Knight extends Piece {
	
	String whitePiece = "wkn.png";
	String blackPiece = "bkn.png";
	int points = 3;

	public Knight(boolean white) {
		super(white);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean move(int x, int y) {
		int xDistance = Math.abs(this.getX()-x);
		int yDistance = Math.abs(this.getY()-y);
		if(xDistance == 2) { // Horizontal L move
			return yDistance == 1;
		}else if (xDistance == 1) { // Vertical L move
			return yDistance == 2;
		}
		return false;
	}

}
