/* Gives the player the option to go up against a computer, whether one utilizing
 * random possible moves (the "easy" version) or one utilizing an algorithm 
 * designed to pick the best possible move (the "hard" version).
 * 
 * Authors: Jonathan Houge &
 */

package game;

import java.util.ArrayList;
import java.util.List;

import pieces.Piece;

public class Robot {
	private String color;
	private Chessboard boardUI;
	List<Piece> pieces;
	public Robot(String color) {
		this.color = color;
		pieces = new ArrayList<>();
		System.out.println("Robot is working and he is "+ this.getColor());
	}
	
	public void setBoard(Chessboard boardUI) {
		this.boardUI = boardUI;
	}
	public void populatePiecesList(Tile[][] board) {
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col ++) {
				Tile t = board[row][col];
				if (t.getPiece() != null && t.getPiece().getColor().equals(this.color)) {
					pieces.add(t.getPiece());
//					System.out.print(t);
				}
			}
		}
	}
	
	public void printPieces(Tile[][] board) {
		for (Piece p: pieces) {
			if (p.getX() == 1000)
				System.out.println("out");
			else
				System.out.println(p);
		}
		
	}
	public String getColor() {
		return this.color;
	}
	
	public void move() {
		
	}
}