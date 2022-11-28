package game;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
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
	String verticalCoords[] = new String[]{"8","7","6","5","4","3","2","1"};
	String horizontalCoords[] = new String[] {"A","B","C","D","E","F","G","H"};
	int BOARD_COORD_OFFSET = 100;
	
	
	public Chessboard(Canvas canvas, Shell shell) {
		this.canvas = canvas;
		this.shell = shell;
	}

	@Override
	public void draw(GC gc) {
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				board[x][y].draw(gc); 
				addBoardCoords(gc, x,y);
				} 
			}
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
				if(boardColor) { board[x][y] = new Tile(w, x*SQUARE_WIDTH+50, y*SQUARE_WIDTH, SQUARE_WIDTH); }
				else { board[x][y] = new Tile(b, x*SQUARE_WIDTH+50, y*SQUARE_WIDTH, SQUARE_WIDTH); }
				
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
	
	public void mouseClickUpdate(float x, float y) {
		getBoardIndex(x,y);
	}
	
	private int[] getBoardIndex(float x, float y) {
		
		x-=BOARD_COORD_OFFSET/2;
//		System.out.println(x + ":" + y);
		float indexX =  x/SQUARE_WIDTH;
		float indexY =  y/SQUARE_WIDTH;
//		System.out.println(indexX + ":" + indexY);
		
		int indX = (int) indexX;
		int indY = (int) indexY;
//		System.out.println(indexY+":"+indexY);
		if(indX < 0 || indX > 7 || indY < 0 || indY > 7 || indexX < 0) {
			System.out.println("Clicking outside the board!");
			return null;}
		System.out.println(horizontalCoords[indX] + verticalCoords[indY] );
		return new int[] {indX,indY};
	}
	
	private void addBoardCoords(GC gc, int x, int y) {
		
		
		Font font = new Font(canvas.getDisplay(), "Tahoma", 15, SWT.BOLD);
		gc.setFont(font);
		gc.setBackground(new Color(255,255,255));
		if(x == 0) {
			String vCoord = verticalCoords[y];
			gc.drawText(vCoord, x*SQUARE_WIDTH+20, y*SQUARE_WIDTH+25);
			
		}if (y == 7) {
			String xCoord = horizontalCoords[x];
			gc.drawText(xCoord, x*SQUARE_WIDTH+60+20, (y+1)*SQUARE_WIDTH+15);
		}
		
		
	}
}