/** Gives the player the option to go up against a computer, whether one utilizing
 * random possible moves (the "easy" version) or one utilizing an algorithm 
 * designed to pick the best possible move (the "hard" version).
 * 
 * @author Ali Sartaz Khan
 */

package game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import pieces.King;
import pieces.Piece;

public class Robot {
	private String color;
	private Chessboard boardUI;
	List<Piece> pieces;
	Piece king;
	boolean whitesTurn;
	int count;
	
	/**
	 * Constructor takes in color of robot
	 *
	 * @param color: color of robot
	 */
	public Robot(String color) {
		this.color = color; this.count = 0;
		if (color.equals("White"))
			whitesTurn = true;
		else
			whitesTurn = false;
		pieces = new ArrayList<>();
		System.out.println("Robot is working and he is "+ this.getColor());
	}
	
	/**
	 * Setting board object
	 * 
	 * @param boardUI: Chessboard object for UI
	 */
	public void setBoard(Chessboard boardUI) {
		this.boardUI = boardUI;
	}
	
	/**
	 * Move robot piece 
	 */
	public void movePiece() {
		updatePieces();
		Random random = new Random();
		if (boardUI.determineKingCheckStatus(king.isWhite())) {
			System.out.println("King in check");
			for (int i =0; i< pieces.size();i++) {
				Piece selectedPiece = pieces.get(i);
				if (makeMove(selectedPiece))
					break;
			}
		}
		else {	
			int i = random.nextInt(pieces.size());
			Piece selectedPiece = pieces.get(i);
			
			if (count > 100) {
				return;
			}
	
			if (!makeMove(selectedPiece))
				movePiece();
		}
		
	}
	
	/** 
	 * Fill list with all of the robot's pieces
	 * 
	 * @param board: 2D tile array
	 */
	public void populatePiecesList(Tile[][] board) {
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col ++) {
				Tile t = board[row][col];
				if (t.getPiece() != null && t.getPiece().getColor().equals(this.color)) {
					if(t.getPiece() instanceof King) {
						King k = (King) t.getPiece();
						king = k;
						k.moved = true;
						pieces.add(k);
					}else {
						pieces.add(t.getPiece());
					}
				}
			}
		}
	}
	
	
	/**
	 * Updates the pieces list to check which pieces are killed off
	 */
	public void updatePieces() {
		List<Piece> list = new ArrayList<>();
		for (Piece p: pieces) 
			if (p.isDead())
				list.add(p);
		for (Piece p: list)
			pieces.remove(p);
	}
	
	
	/**
	 * Prints all the pieces out
	 * 
	 * @param board: 2D tile array
	 */
	public void printPieces(Tile[][] board) {
		updatePieces();
		for (Piece p: pieces) {
			if (p.getX() == 1000)
				System.out.println("out");
			else
				System.out.println(p);
		}
		
	}
	
	/**
	 * Makes a move using one of the 4 iteration patterns
	 * 
	 * @param piece: piece that will move
	 * @return true or false depending on whether robot can move piece
	 */
	public boolean makeMove(Piece piece) {		
		count++;
		Random rand = new Random();
		int iteration = rand.nextInt(4);
		if (iteration == 0)
			return iteration1(piece);
		else if(iteration ==1)
			return iteration2(piece);
		else if(iteration ==2)
			return iteration3(piece);
		else
			return iteration4(piece);			
	}
	
	
	/**
	 * Iteration pattern 1
	 * 
	 * @param piece: piece that will move
	 * @return true or false depending on whether robot can move piece
	 */
	public boolean iteration1(Piece piece) {
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (boardUI.validMoveMade(col,row,piece,whitesTurn)) {
					boardUI.updateBoard(col,row,piece);
					count = 0;
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Iteration pattern 2
	 * 
	 * @param piece: piece that will move
	 * @return true or false depending on whether robot can move piece
	 */
	public boolean iteration2(Piece piece) {
		for (int row = 0; row < 8; row+=2) {
			for (int col = 0; col < 8; col++) {
				if (boardUI.validMoveMade(col,row,piece,whitesTurn)) {
					boardUI.updateBoard(col,row,piece);
					count = 0;
					return true;
				}
			}
		}
		for (int row = 1; row < 8; row+=2) {
			for (int col = 0; col < 8; col++) {
				if (boardUI.validMoveMade(col,row,piece,whitesTurn)) {
					boardUI.updateBoard(col,row,piece);
					count = 0;
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Iteration pattern 3
	 * 
	 * @param piece: piece that will move
	 * @return true or false depending on whether robot can move piece
	 */
	public boolean iteration3(Piece piece) {
		for (int row = 7; row >= 0; row--) {
			for (int col = 7; col >= 0; col --) {
				if (boardUI.validMoveMade(col,row,piece,whitesTurn)) {
					boardUI.updateBoard(col,row,piece);
					count = 0;
					return true;
				}
			}
		}
		return false;	}
	
	/**
	 * Iteration pattern 4
	 * 
	 * @param piece: piece that will move
	 * @return true or false depending on whether robot can move piece
	 */
	public boolean iteration4(Piece piece) {
		for (int row = 7; row >=0; row-=2) {
			for (int col = 0; col < 8; col++) {
				if (boardUI.validMoveMade(col,row,piece,whitesTurn)) {
					boardUI.updateBoard(col,row,piece);
					count = 0;
					return true;
				}
			}
		}
		for (int row = 7; row >= 0; row=-2) {
			for (int col = 0; col < 8; col++) {
				if (boardUI.validMoveMade(col,row,piece,whitesTurn)) {
					boardUI.updateBoard(col,row,piece);
					count = 0;
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @return color of the robot
	 */
	public String getColor() {
		return this.color;
	}
	
}