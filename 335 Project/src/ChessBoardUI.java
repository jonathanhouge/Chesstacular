/* Chess Board UI.
 * 
 * Author: Khojiakbar Yokubjonov & Jonathan Houge
 */

import java.util.ArrayList;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Shell;

interface ChessBoardUI{
	public void draw(GC gc);
	public void draw(PaintEvent e);
	
}

class ChessBoardUIOne implements ChessBoardUI{
	Canvas canvas;
	Shell shell;
	int SQUARE_WIDTH = 80;
	ArrayList<String>imgData = new ArrayList<>();
	String [][]boardData; // old
	Tile [][]board; // new
	
	
	public ChessBoardUIOne(Canvas canvas, Shell shell) {
		this.canvas = canvas;
		this.shell = shell;
		//initializeBoardData("white");
		//createBoardData();

	}

	@Override
	public void draw(GC gc) {
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				board[x][y].draw(gc);
			}
		}
	}
	
	// not used
	@Override
	public void draw(PaintEvent e) {
		// TODO Auto-generated method stub
		boolean white = true;
		for(int y=0; y<8; y++) {
			for(int x=0; x<8; x++) {
				if(white) {
					e.gc.setBackground(new Color(255, 221, 153));
					
				}else {
					e.gc.setBackground(new Color(204, 136, 0));
				}
				
				
				e.gc.fillRectangle(x*SQUARE_WIDTH, y*SQUARE_WIDTH, SQUARE_WIDTH, SQUARE_WIDTH);
				if(boardData[y][x] != null) {
					Image img = new Image(shell.getDisplay(), "images/" + boardData[y][x]);
					e.gc.drawImage(img, x*SQUARE_WIDTH+10, y*SQUARE_WIDTH+10);
				}
				
				white = !white;
			}
			white = !white;
			
		}
		
	}
	
	
//	private void setUpImgData() {
//		imgData.add("bb.png");
//		imgData.add("wb.png");
//		
//		imgData.add("bk.png");
//		imgData.add("wk.png");
//		
//		imgData.add("bkn.png");
//		imgData.add("wkn.png");
//		
//		imgData.add("bp.png");
//		imgData.add("wp.png");
//		
//		imgData.add("br.png");
//		imgData.add("wr.png");
//		
//		imgData.add("bq.png");
//		imgData.add("wq.png");
//	}
	
	// make this function simpleton
	protected void createBoardData(GC gc) {
		board = new Tile[8][8];
		
		boolean white = true;
		Color w = new Color(255, 221, 153); Color b = new Color(204, 136, 0);
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				
				if(white) { board[x][y] = new Tile(w, x*SQUARE_WIDTH, y*SQUARE_WIDTH, SQUARE_WIDTH); }
				else { board[x][y] = new Tile(b, x*SQUARE_WIDTH, y*SQUARE_WIDTH, SQUARE_WIDTH); }

				white = !white; }
			
			white = !white; }
	}
	
	// not used
	private void initializeBoardData(String color) {
		//black pieces
		boardData = new String[8][8];
		boardData[0][0] = "br.png";
		boardData[0][1] = "bkn.png";
		boardData[0][2] = "bb.png";
		boardData[0][3] = "bq.png";
		boardData[0][4] = "bk.png";
		boardData[0][5] = "bb.png";
		boardData[0][6] = "bkn.png";
		boardData[0][7] = "br.png";
		
		for(int i=0; i<8; i++) {
			boardData[1][i] = "bp.png";
		}
		
		
		for(int i=0; i<8; i++) {
			boardData[6][i] = "wp.png";
		}
		
		boardData[7][0] = "wr.png";
		boardData[7][1] = "wkn.png";
		boardData[7][2] = "wb.png";
		boardData[7][3] = "wq.png";
		boardData[7][4] = "wk.png";
		boardData[7][5] = "wb.png";
		boardData[7][6] = "wkn.png";
		boardData[7][7] = "wr.png";
		
		
	}
	
	public void mouseClickUpdate(int x, int y) {
		getBoardIndex(x,y);
		
	}
	
	private int[] getBoardIndex(float x, float y) {
		float indexX = x/SQUARE_WIDTH;
		float indexY = y/SQUARE_WIDTH;
		
		int indX = Math.round(indexX);
		int indY = Math.round(indexY);
//		System.out.println(indexY+":"+indexY);
		System.out.println(indY + ":"+indX );
		
		
		
		return new int[] {indX,indY};
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}