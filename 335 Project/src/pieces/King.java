package pieces;

public class King extends Piece {

	public King(boolean white) {
		super(white);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean move(int x, int y) {
		int xDistance = Math.abs(x-this.getX());
		int yDistance = Math.abs(y-this.getY());
		
		if(yDistance == 1 && xDistance == 1) {
			updateLocation(x,y);
			return true;
		}else if (yDistance == 1 && xDistance == 0) {
			updateLocation(x,y);
			return true;
		}else if(yDistance == 0 && xDistance == 1) {
			updateLocation(x,y);
			return true;
		}
		return false;
	}

}
