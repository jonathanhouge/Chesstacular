package game;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

import pieces.Piece;

/** Tile class. Each tile represents a square on the chess board. Every
 * tile holds onto the rectangle that represents it visually, the 
 * piece it holds (if any), the color and outline it is, and the x & y location.
 * 
 * @author Jonathan Houge
 */
public class Tile {
	
	private Rectangle tile;
	private Piece piece;
	
	private Color color;
	private Color outline;
	
	private int x;
	private int y;
	
	/** 
	 * Constructor. Initialize piece location, tile color, x and y
	 * location, and tells piece what each square's width is.
	 * Also creates the rectangle that'll visually represent it.
	 * 
	 * @param color: color of the tile (black or white)
	 * @param outline: outline of the tile
	 * @param x: tile's x axis location on the canvas
	 * @param y: tile's y axis location on the canvas
	 * @param dimensions: the dimensions of the rectangle (width & height)
	 */
	public Tile(Color color, Color outline, int x, int y, int dimensions) {
		this.piece = null;
		this.color = color; this.outline = outline;
		this.x = x; this.y = y;
		this.tile = new Rectangle(this.x, this.y, dimensions, dimensions);
		Piece.setWidth(dimensions); }
	
	/** 
	 * Drawing method, default. 
	 * Makes the background the color of the tile (black or white),
	 * fills the tile. Makes the foreground the color of the outline,
	 * draws the tile's outline.
	 * If there's a piece, draw the piece after the tile. 
	 * 
	 * @param gc: event gc, let's us draw on the canvas
	 */
	public void draw(GC gc) {
		gc.setBackground(this.color);
		gc.fillRectangle(this.tile);
		
		gc.setForeground(this.outline);
		gc.drawRectangle(this.tile);
		
		if (piece != null) { piece.draw(gc); } }
	
	/** 
	 * Drawing method, special. 
	 * Makes the background a passed in color (green or cyan),
	 * fills the tile. Makes the foreground the color of the outline,
	 * draws the tile's outline.
	 * If there's a piece, draw the piece after the tile. 
	 * 
	 * @param gc: event gc, let's us draw on the canvas
	 * @param newColor: new color to draw it, either this tile holds the
	 * selected piece or it's a potential for the selected piece to move to.
	 */
	public void draw(GC gc, Color newColor) {
		gc.setBackground(newColor);
		gc.fillRectangle(tile);
		
		gc.setForeground(this.outline);
		gc.drawRectangle(this.tile);
		
		if (piece != null) { piece.draw(gc); } }
	
	//-- setters & getters
	
	/**
	 * Piece setter. Piece is initially set by ChessBoardUI. Some tiles don't have
	 * pieces. Only update piece location if there's an actual piece. 
	 * 
	 * @param piece: an actual piece or null, if double checks it. 
	 */
	public void setPiece(Piece piece) { 
		this.piece = piece;
		
		if (piece != null) { this.piece.setLocation(x, y); } }

	/**
	 * Piece check. Reports back if there's a piece present or not.
	 * 
	 * @return boolean, true if there's a piece, false if there isn't.
	 */
	public boolean hasPiece() { return this.piece != null; }
	
	/**
	 * Piece getter. Returns the associated piece.
	 * 
	 * @return piece on this tile.
	 */
	public Piece getPiece() { return this.piece; }
	
	/**
	 * toString method. Prints the associated piece (if there is one)
	 * 
	 * @return toString of the associated piece, if there is one, or null.
	 */
	public String toString() {
		if (this.piece == null)
			return "null";
		return piece.toString(); }
	
}