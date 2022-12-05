/* Chess Board UI.
 * 
 * Authors: Jonathan Houge & Khojiakbar Yokubjonov
 */

package game;

import org.eclipse.swt.graphics.GC;

// could perhaps make another instance of ChessBoardUI that's for testing - only white pieces, no turns, etc.
interface ChessBoardUI {
	public void draw(GC gc);
	public void createBoardData(GC gc);
}