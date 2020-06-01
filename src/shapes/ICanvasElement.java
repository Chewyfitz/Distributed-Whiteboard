package shapes;

import java.awt.Graphics;
import java.awt.Graphics2D;

public interface ICanvasElement{
	public enum _state {
		CREATED,
		EDITING,
		DONE
	}
	
	/**
	 * The current state of the shape (for synchronisation)
	 * 
	 * @return the state of the shape
	 */
	public _state getState();
	
	/** 
	 * Set the state as done, this is when the final
	 * version of the shape is sent to the server
	 */ 
	public void finish();
	
	/**
	 * Move the canvas element by amounts delta-x (dx) and delta-y (dy)
	 * @param dx - The x-delta (x1 - x0)
	 * @param dy - The y-delta (y1 - y0)
	 */
	public void move(int dx, int dy);
	
	/**
	 * Set the value of the second point for the canvas element.
	 * Params are self-evident.
	 * @param int x
	 * @param int y
	 */
	public void setSecondary(int x, int y);
	
	/**
	 * Pretty much just set the string for the text shape.
	 * In future could be expanded to add text to other elements as well
	 * 
	 * @param str - the string to update
	 */
	public void setText(String str);
	
	// These two are pipeline functions for rendering shapes on the canvas
	public void paint(Graphics g);
	public void paint(Graphics2D g2d);
}
