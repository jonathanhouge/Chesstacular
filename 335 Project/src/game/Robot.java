/* Gives the player the option to go up against a computer, whether one utilizing
 * random possible moves (the "easy" version) or one utilizing an algorithm 
 * designed to pick the best possible move (the "hard" version).
 * 
 * Authors: Jonathan Houge &
 */

package game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import pieces.Piece;

public class Robot {
	private String color;
	private Chessboard boardUI;
	List<Piece> pieces;
	
	boolean whitesTurn;
	public Robot(String color) {
		this.color = color;
		if (color.equals("White"))
			whitesTurn = true;
		else
			whitesTurn = false;
		pieces = new ArrayList<>();
		System.out.println("Robot is working and he is "+ this.getColor());
	}
	
	public void setBoard(Chessboard boardUI) {
		this.boardUI = boardUI;
	}
	
	public void movePiece() {
		updatePieces();
		Random random = new Random();
		int i = random.nextInt(pieces.size());
		Piece selectedPiece = pieces.get(i);
		if (!makeMove(selectedPiece))
			movePiece();
		
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
	
	public void updatePieces() {
		List<Piece> list = new ArrayList<>();
		for (Piece p: pieces) 
			if (p.getX() == 1000)
				list.add(p);
		for (Piece p: list)
			pieces.remove(p);
	}
	public void printPieces(Tile[][] board) {
		updatePieces();
		for (Piece p: pieces) {
			if (p.getX() == 1000)
				System.out.println("out");
			else
				System.out.println(p);
		}
		
	}
	
	public boolean makeMove(Piece piece) {
		Random rand = new Random();
		int iteration = rand.nextInt(6);
		if (iteration == 1)
			return iteration1(piece);
		else if(iteration ==2)
			return iteration2(piece);
		else if(iteration ==3)
			return iteration3(piece);
		else if(iteration ==4)
			return iteration4(piece);
		return false;
			
	}
	
	public boolean iteration1(Piece piece) {
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (boardUI.validMoveMade(col,row,piece,whitesTurn)) {
					boardUI.updateBoard(col,row,piece);
					return true;
				}
			}
		}
		return false;
	}
	public boolean iteration2(Piece piece) {
		for (int row = 0; row < 8; row+=2) {
			for (int col = 0; col < 8; col++) {
				if (boardUI.validMoveMade(col,row,piece,whitesTurn)) {
					boardUI.updateBoard(col,row,piece);
					return true;
				}
			}
		}
		for (int row = 1; row < 8; row+=2) {
			for (int col = 0; col < 8; col++) {
				if (boardUI.validMoveMade(col,row,piece,whitesTurn)) {
					boardUI.updateBoard(col,row,piece);
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean iteration3(Piece piece) {
		for (int row = 7; row >= 0; row--) {
			for (int col = 7; col >= 0; col --) {
				if (boardUI.validMoveMade(col,row,piece,whitesTurn)) {
					boardUI.updateBoard(col,row,piece);
					return true;
				}
			}
		}
		return false;	}
	
	public boolean iteration4(Piece piece) {
		for (int row = 7; row >=0; row-=2) {
			for (int col = 0; col < 8; col++) {
				if (boardUI.validMoveMade(col,row,piece,whitesTurn)) {
					boardUI.updateBoard(col,row,piece);
					return true;
				}
			}
		}
		for (int row = 7; row >= 0; row=-2) {
			for (int col = 0; col < 8; col++) {
				if (boardUI.validMoveMade(col,row,piece,whitesTurn)) {
					boardUI.updateBoard(col,row,piece);
					return true;
				}
			}
		}
		return false;
	}
	
	public String getColor() {
		return this.color;
	}
	
}