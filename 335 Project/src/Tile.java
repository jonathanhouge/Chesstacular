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
	private String location;
	
	public Tile(Color color, int x, int y, int dimensions) {
		this.piece = null;
		this.color = color;
		this.x = x; this.y = y;
		this.tile = new Rectangle(this.x, this.y, dimensions, dimensions); 
		Piece.setWidth(dimensions); }
	
	public void draw(GC gc) {
		gc.setBackground(color);
		gc.fillRectangle(tile);
		if (piece != null) { piece.draw(gc); }
	}
	
	// setters
	public void setPiece(Piece piece) { 
		this.piece = piece;
		this.piece.updateLocation(x, y); }
	
	public void setColor(Color color) { this.color = color; }

	// getters
	public Piece getPiece() { return this.piece; }
	public Color getColor() { return this.color; }

}