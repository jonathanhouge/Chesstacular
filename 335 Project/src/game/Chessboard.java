package game;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Shell;

import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

public class Chessboard implements ChessBoardUI{
	Canvas canvas;
	Shell shell;
	int SQUARE_WIDTH = 80;
	Tile [][]board;
	
	
	public Chessboard(Canvas canvas, Shell shell) {
		this.canvas = canvas;
		this.shell = shell;
	}

	@Override
	public void draw(GC gc) {
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				board[x][y].draw(gc); } }
	}
	
	/* 'simpleton' function, only ran once for initialization.
	 * color boolean parameter determines what pieces should be in front of the player. */
	public void createBoardData(GC gc, boolean white) {
		board = new Tile[8][8]; // list of tiles
		boolean boardColor = true; // if true, tile is white and vice versa
		Color w = new Color(255, 221, 153); Color b = new Color(204, 136, 0); // tile colors, white and black respectively
		
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				
				// create the tiles with the proper color
				if(boardColor) { board[x][y] = new Tile(w, x*SQUARE_WIDTH, y*SQUARE_WIDTH, SQUARE_WIDTH); }
				else { board[x][y] = new Tile(b, x*SQUARE_WIDTH, y*SQUARE_WIDTH, SQUARE_WIDTH); }
				
				// set the piece using a helper function
				board[x][y].setPiece(makePiece(x, y, white));

				// alternating tile colors
				boardColor = !boardColor; }
			boardColor = !boardColor; 
		}
	}
	
	/* helper function responsible for making the pieces and putting
	 * them in their starting positions. */
	private Piece makePiece(int x, int y, boolean playerColor) {
		
		// want the pieces farthest from the player to be the opposite color (opponent's color)
		playerColor = !playerColor;

		if (y == 0) { // enemy specialty pieces
			if (x == 0 || x == 7) { return (new Rook(playerColor, shell)); }
			else if (x == 0 || x == 7) { return (new Rook(playerColor, shell)); }
			else if (x == 1 || x == 6) { return (new Knight(playerColor, shell)); }
			else if (x == 2 || x == 5) { return (new Bishop(playerColor, shell)); }
			else if (x == 3) { return (new King(playerColor, shell)); }
			else if (x == 4) { return (new Queen(playerColor, shell)); } }
		else if (y == 1) { return (new Pawn(playerColor, shell)); } // enemy pawns
		
		// want the pieces closest to the player to be their color (player's color)
		playerColor = !playerColor;
		
		if (y == 6) { return (new Pawn(playerColor, shell)); } // your pawns
		else if (y == 7) { // your specialty pieces
			if (x == 0 || x == 7) { return (new Rook(playerColor, shell)); }
			else if (x == 0 || x == 7) { return (new Rook(playerColor, shell)); }
			else if (x == 1 || x == 6) { return (new Knight(playerColor, shell)); }
			else if (x == 2 || x == 5) { return (new Bishop(playerColor, shell)); }
			else if (x == 3) { return (new King(playerColor, shell)); }
			else if (x == 4) { return (new Queen(playerColor, shell)); } }
		
		return null; // tile is given no piece
	}
	
	public void mouseClickUpdate(int x, int y) {
		getBoardIndex(x,y);
	}
	//test
	
	private int[] getBoardIndex(float x, float y) {
		float indexX = x/SQUARE_WIDTH;
		float indexY = y/SQUARE_WIDTH;
		
		int indX = (int) indexX;
		int indY = (int) indexY;
//		System.out.println(indexY+":"+indexY);
		System.out.println(indY + ":" + indX );
		
		return new int[] {indX,indY};
	}
}