package shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;

public class Line implements ICanvasElement, Serializable{

	// generated
	private static final long serialVersionUID = -5297844394451485351L;
	
	
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	private Color c;
	private Color bc;
	private _state state = _state.CREATED;
	
	private int weight;
	
	public Line(int x1, int y1, int x2, int y2, int weight){
		init(x1, y1, x2, y2, weight, null, null);
	}
	public Line(int x1, int y1, int x2, int y2, int weight, Color c) {
		init(x1, y1, x2, y2, weight, c, null);
	}
	public Line(int x1, int y1, int x2, int y2, int weight, Color c, Color bc) {
		init(x1, y1, x2, y2, weight, c, bc);
	}
	private void init(int x1, int y1, int x2, int y2, int weight, Color c, Color bc) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.weight = weight;
		this.c = c;
		this.state = _state.EDITING;
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
	
	@Override
	public void setSecondary(int x, int y) {
		x2 = x;
		y2 = y;
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		paint(g2d);
	}
	
	@Override
	public void paint(Graphics2D g2d) {
		g2d.setStroke(new BasicStroke(weight));
		g2d.setColor(c);
		g2d.setBackground(bc);
		g2d.drawLine(x1, y1, x2, y2);
	}
}
