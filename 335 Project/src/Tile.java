/* Tile class. Each tile represents a square on the chess board.
 * 
 * Author: Jonathan Houge
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

import pieces.Piece;

public class Tile {
	
	private Rectangle tile;
	private Piece piece;
	private Color color;
	
	private int x;
	private int y;
	private String location; // for example, A4
	
	/* constructor. initialize piece location, tile color, x and y
	 * location, and tell piece what each square's width is. */
	public Tile(Color color, int x, int y, int dimensions) {
		this.piece = null;
		this.color = color;
		this.x = x; this.y = y;
		this.tile = new Rectangle(this.x, this.y, dimensions, dimensions); 
		Piece.setWidth(dimensions); }
	
	/* drawing. make the background the correct color, draw the tile.
	 * if there's a piece, draw the piece after the tile. */
	public void draw(GC gc) {
		gc.setBackground(color);
		gc.fillRectangle(tile);
		if (piece != null) { piece.draw(gc); } }
	
	//-- setters
	
	/* piece is initially set by ChessBoardUI. some tiles don't have
	 * pieces. only update piece location if there's an actual piece. */
	public void setPiece(Piece piece) { 
		this.piece = piece;
		if (piece != null) {
			this.piece.updateLocation(x, y); } }
	
	public void setColor(Color color) { this.color = color; }

	//-- getters
	public Piece getPiece() { return this.piece; }
	public Color getColor() { return this.color; }
}