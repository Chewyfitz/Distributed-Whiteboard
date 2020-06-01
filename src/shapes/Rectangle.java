package shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;

public class Rectangle implements ICanvasElement, Serializable{
	
	// generated
	private static final long serialVersionUID = -6375189249997035284L;
	
	
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	private Color c;
	private Color bc;
	private _state state = _state.CREATED;
	
	private int weight;
	
	// Stored integer values for easy painting
	private int minx1;
	private int miny1;
	private int width;
	private int height;
	
	public Rectangle(int x1, int y1, int x2, int y2, int weight){
		init(x1, y1, x2, y2, weight, null, null);
	}
	public Rectangle(int x1, int y1, int x2, int y2, int weight, Color c) {
		init(x1, y1, x2, y2, weight, c, null);
	}
	public Rectangle(int x1, int y1, int x2, int y2, int weight, Color c, Color bc) {
		init(x1, y1, x2, y2, weight, c, bc);
	}
	private void init(int x1, int y1, int x2, int y2, int weight, Color c, Color bc) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.weight = weight;
		
		minx1 = min(x1, x2);
		miny1 = min(y1, y2);
		width = abs(x2-x1);
		height = abs(y2-y1);
		
		this.bc = bc;
		this.c = c;
	}
	@Override
	public void setText(String str) {}
	@Override
	public _state getState() {
		return this.state;
	}
	@Override
	public void finish() {
		this.state = _state.DONE;
	}
	
	@Override
	public void move(int dx, int dy) {
		x1 += dx;
		y1 += dy;
		x2 += dx;
		y2 += dy;
	}
	
	public int abs (int value) {
		return value < 0 ? -value : value;
	}
	public int min(int a, int b) {
		return a < b ? a : b;
	}
	
	@Override
	public void setSecondary(int x, int y) {
		x2 = x;
		y2 = y;
		
		// Store the result of these operations for easy painting
		minx1 = min(x1, x2);
		miny1 = min(y1, y2);
		width = abs(x2-x1);
		height = abs(y2-y1);
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		paint(g2d);
	}
	
	@Override
	public void paint(Graphics2D g2d) {
		g2d.setStroke(new BasicStroke(weight));
		g2d.setColor(bc);
		g2d.fillRect(minx1, miny1, width, height);
		g2d.setColor(c);
		g2d.drawRect(minx1, miny1, width, height);
	}

}
