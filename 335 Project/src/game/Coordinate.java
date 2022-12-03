package game;

public class Coordinate {
	int x;
	int y;
	char[] bottom = {'A','B','C','D','E','F','G','H','?'};
	char[] side = {'8','7','6','5','4','3','2','1','?'};
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public String toString() {
		return "(" + bottom[x] + side[y] + ")";
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Coordinate) {
			Coordinate c = (Coordinate) o;
			return c.x == this.x && c.y == this.y;
		}
		return false;
		
	}
}
