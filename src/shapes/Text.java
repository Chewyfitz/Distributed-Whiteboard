package shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;

public class Text  implements ICanvasElement, Serializable {

	// generated
	private static final long serialVersionUID = 5223254896371042482L;
	
	
	private int x;
	private int y;
	private Color c;
	private Color bc;
	private _state state = _state.CREATED;
	
	private int weight;
	private Font font;
	
	private String str = "New String";
	
	public Text(int x, int y, int weight){
		init(x, y, weight, null, null);
	}
	public Text(int x, int y, int weight, Color c) {
		init(x, y, weight, c, null);
	}
	public Text(int x, int y, int weight, Color c, Color bc) {
		init(x, y, weight, c, bc);
	}
	private void init(int x, int y, int weight, Color c, Color bc) {
		this.x = x;
		this.y = y;
		this.bc = bc;
		this.c = c;
		this.weight = weight;
		font = new Font("sans", Font.PLAIN, weight);
	}
	
	@Override
	public _state getState() {
		return this.state;
	}
	@Override
	public void finish() {
		this.state = _state.DONE;
	}
	
	@Override
	public void setText(String str) {
		this.str = str;
	}
	
	@Override
	public void move(int dx, int dy) {
		x += dx;
		y += dy;
	}
	
	@Override
	public void setSecondary(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		paint(g2d);
	}
	
	@Override
	public void paint(Graphics2D g2d) {
		g2d.setStroke(new BasicStroke(weight));
		g2d.setFont(font);
		g2d.setColor(c);
		g2d.setBackground(bc);
		g2d.drawString(str, x, y);
	}
}
