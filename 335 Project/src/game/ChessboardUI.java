package game;

import org.eclipse.swt.graphics.GC;

/**
 * Chessboard interface. Want to make sure that
 * every implementation has a draw and createBoardData method.
 * 
 * @author Jonathan Houge & Khojiakbar Yokubjonov
 */
interface ChessboardUI {
	public void draw(GC gc);
	public void createBoardData(GC gc);
}