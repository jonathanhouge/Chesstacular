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
	private String location;
	
	public Tile(Color color, int x, int y, int dimensions) {
		this.piece = null;
		this.color = color;
		this.tile = new Rectangle(x, y, dimensions, dimensions); 
		Piece.setWidth(dimensions);}
	
	public void draw(GC gc) {
		gc.setBackground(color);
		gc.fillRectangle(tile);
		if (piece != null) { /* draw the piece image */ }
	}
	
	// setters
	public void setPiece(Piece piece) { this.piece = piece; }
	public void setColor(Color color) { this.color = color; }

	// getters
	public Piece getPiece() { return this.piece; }
	public Color getColor() { return this.color; }

}