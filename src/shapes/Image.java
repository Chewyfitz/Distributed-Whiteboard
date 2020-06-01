package shapes;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;

import javax.imageio.ImageIO;


public class Image implements ICanvasElement, Serializable {
	
	// generated
	private static final long serialVersionUID = 7992950769256054090L;
	
	
	int x;
	int y;
	byte[] image;
	
	public Image(byte[] rawImage) {
		init(0, 0, rawImage);
	}
	public Image(String imagePath) {
		try {
			File f = new File(imagePath);
			byte[] image = Files.readAllBytes(f.toPath());
			init(0,0,image);
		} catch (IOException e) {
			// TODO: Do something for Image read error
			e.printStackTrace();
		}
	}
	public Image(File f) {
		byte[] image;
		try {
			image = Files.readAllBytes(f.toPath());
			init(0,0,image);
		} catch (IOException e) {
			// TODO: Do something for Image read error
			e.printStackTrace();
		}
	}
	private void init(int _x, int _y, byte[] rawImage) {
		x = _x;
		y = _y;
		image = rawImage;
		
	}
	
	private BufferedImage getBufferedImage() {
		try {
			final BufferedImage img = ImageIO.read(new ByteArrayInputStream(image));
			return img;
		} catch (IOException e) {
			// TODO: Do something for Image read error
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public _state getState() {
		return _state.DONE;
	}

	@Override
	public void finish() {
	}

	@Override
	public void move(int dx, int dy) {
	}

	@Override
	public void setSecondary(int x, int y) {
	}

	@Override
	public void setText(String str) {
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		paint(g2d);
	}

	@Override
	public void paint(Graphics2D g2d) {
		g2d.drawImage(getBufferedImage(), x, y, null);
	}

}
