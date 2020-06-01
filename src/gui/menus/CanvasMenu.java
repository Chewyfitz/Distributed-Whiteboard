package gui.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import gui.Whiteboard;

public class CanvasMenu extends JMenu {

	private static final long serialVersionUID = -1790501384551545301L;
	
	private Whiteboard whiteboard;
	
	public JMenuItem resize;
	
	public JPanel resizePanel;
	public JSpinner heightSpinner;
	public JSpinner widthSpinner;
	
	public CanvasMenu(Whiteboard wb){
		super("Canvas");
		whiteboard = wb;
		
		// Create Menu Items
        resize = new JMenuItem("Resize Canvas");
        
        // Create Option Panes
        resizePanel = new JPanel();
        heightSpinner = new JSpinner();
        widthSpinner = new JSpinner();
        
        resizePanel.add(heightSpinner);
        resizePanel.add(new JLabel(" x "));
        resizePanel.add(widthSpinner);
        heightSpinner.setValue(whiteboard.height);
        widthSpinner.setValue(whiteboard.width);
        
        // Add listeners
        resize.addActionListener(canvasMenuListener);
        
        add(resize);
		
	}
	
	private ActionListener canvasMenuListener = new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		switch(((JMenuItem) e.getSource()).getText()) {
    			case "Resize Canvas":
    				// Set the canvas Size
    				int result = JOptionPane.showConfirmDialog(null, resizePanel, "Set Size", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    				if(result != 0) break;
    				
    				int width = (int) widthSpinner.getValue();
    				int height = (int) heightSpinner.getValue();
    				whiteboard.canvas.setSize(width, height);
    				whiteboard.setSize(width, height + whiteboard.toolBar.getHeight());
    				if(whiteboard.clientServermode == Whiteboard.SERVERMODE) {
    					whiteboard.server.setSize(width, height);
    				}
    				break;
				default:
					System.err.println("Unexpected option: "+((JMenuItem) e.getSource()).getText());
    		}
    	}
    };

}
