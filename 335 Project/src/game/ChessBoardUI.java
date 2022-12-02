package game;
/* Chess Board UI.
 * 
 * Authors: Jonathan Houge & Khojiakbar Yokubjonov
 */

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;

import pieces.Piece;
import pieces.Pawn;
import pieces.King;
import pieces.Rook;
import pieces.Queen;
import pieces.Bishop;
import pieces.Knight;

// could perhaps make another instance of ChessBoardUI that's for testing - only white pieces, no turns, etc.
interface ChessBoardUI {
	public void draw(GC gc);
	public void createBoardData(GC gc);
}