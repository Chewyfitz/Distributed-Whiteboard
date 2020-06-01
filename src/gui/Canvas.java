package gui;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.IRemoteClient;
import shapes.ICanvasElement;

@SuppressWarnings("serial")
public class Canvas extends JPanel {
	ArrayList<ICanvasElement> canvasElements;

	private ICanvasElement tempElement;
	 
	public static final int LOCALMODE = 0;
	public static final int CLIENTSERVERMODE = 1;
	public int clientServermode = 0;
	
	// the whiteboard object to deal with callbacks
	public Whiteboard whiteboard;
	
	public Canvas(Whiteboard whiteboard){
		super();
		
		this.whiteboard = whiteboard;
		canvasElements = new ArrayList<>();
		
		this.addMouseMotionListener(new MouseMotionAdapter() {
        	@Override
        	public void mouseDragged(MouseEvent e) {
        		tempElement.setSecondary(e.getX(), e.getY());
        		repaint();
        	}
        });
        this.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		tempElement = 
        				makeShape(whiteboard.toolBar.selectedTool.getSelection().getActionCommand()
        				, (int) whiteboard.toolBar.lineWeight.getValue()
        				, (int) whiteboard.toolBar.fontWeight.getValue()
        				, whiteboard.toolBar.colourChooser.getColor()
        				, whiteboard.toolBar.fillColourChooser.getColor()
        				, e.getX()
        				, e.getY()
        				);
        		addElement(tempElement);
        	}
        	@Override
        	public void mousePressed(MouseEvent e) {
        		tempElement = 
        				makeShape(whiteboard.toolBar.selectedTool.getSelection().getActionCommand()
        				, (int) whiteboard.toolBar.lineWeight.getValue()
        				, (int) whiteboard.toolBar.fontWeight.getValue()
        				, whiteboard.toolBar.colourChooser.getColor()
        				, whiteboard.toolBar.fillColourChooser.getColor()
        				, e.getX()
        				, e.getY()
        				);
        		addElement(tempElement);
        	}
        	@Override
        	public void mouseReleased(MouseEvent e) {
        		tempElement.setSecondary(e.getX(), e.getY());
        		if(whiteboard.toolBar.selectedTool.getSelection().getActionCommand().equals("Text")){
					String returnString = stringValuePopup();
					if(returnString != null) {
						tempElement.setText(returnString);
					} else {
						canvasElements.remove(tempElement);
					}
		        }
        		if(clientServermode == Canvas.CLIENTSERVERMODE) {
        			whiteboard.callback(tempElement);
        		}
        		repaint();
        	}
        });
	}
	
	private ICanvasElement makeShape(String tool, int weight, int fontWeight, Color colour, Color fillColour, int x0, int y0) {
		ICanvasElement shape;
		switch(tool) {
		case "Text":
			shape = new shapes.Text(x0, y0, fontWeight, colour, fillColour);
			break;
		case "Circle":
			shape = new shapes.Circle(x0, y0, x0, y0, weight, colour, fillColour);
			break;
		case "Rectangle":
			shape = new shapes.Rectangle(x0, y0, x0, y0, weight, colour, fillColour);
			break;
		case "Line":
		default:
			shape = new shapes.Line(x0, y0, x0, y0, weight, colour, fillColour);
		}
		return shape;
	}
	
	private String stringValuePopup() {
		return (String) JOptionPane.showInputDialog("Set Text: ");
	}
	
	public void addElement(ICanvasElement e) {
		getCanvasElements().add(e);
		this.repaint();
	}
	
	public ArrayList<ICanvasElement> getCanvasElements() {
		return canvasElements;
	}

	public void clearCanvas() {
		if(!getCanvasElements().isEmpty()) {
			getCanvasElements().clear();
		}
		this.repaint();
	}
	
	public int save(File outputFile) {
		int status = -1;
		
		BufferedImage bImg = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D cg = bImg.createGraphics();
		this.paint(cg);
		try {
			if(ImageIO.write(bImg, "png", outputFile)) {
				whiteboard.alert("Save successful!");
				status = 0;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return status;
		}
		
		return status;
	}
	
	// This method is here because it needs to be rendered properly. By all means it should be somewhere else.
	public int addClient(IRemoteClient client) throws RemoteException {
		int result = 0;
		JPanel clientConfirm = new JPanel();
		clientConfirm.add(new gui.InfoTextPane("New client: "+client.getUsername()+", will you allow them to join?"));
		System.out.println("New Client: "+client.getUsername());
		
		result = JOptionPane.showConfirmDialog(this, clientConfirm, "Host a Whiteboard", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		return result;
	}

	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		
		if(!getCanvasElements().isEmpty()) {
			for(ICanvasElement e: getCanvasElements()) {
				e.paint(g2d);
			}
		}
	}
	
	
		
	public void setClientServerMode(int csm) {
		this.clientServermode = csm;
	}
}
