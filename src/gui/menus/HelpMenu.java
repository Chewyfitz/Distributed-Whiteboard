package gui.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import gui.InfoTextPane;
import gui.Whiteboard;

public class HelpMenu extends JMenu {

	private static final long serialVersionUID = 6835933589956737168L;
	
	public JMenuItem helpAbout;
	public JPanel resizePanel;
	public InfoTextPane about;
	
	@SuppressWarnings("unused")
	private Whiteboard whiteboard;
	
	public String helpText = "Distributed Whiteboard v1.0 by Aidan Fitzpatrick (835833)\nfor COMP90015 Distributed Systems 2020";
	
	public HelpMenu(Whiteboard wb) {
		super("Help");
		whiteboard = wb;
				
		// Create menu items
		helpAbout = new JMenuItem("About");
		
		// Add Items to menu
		add(helpAbout);
		
		// Create Option Panes
        resizePanel = new JPanel();

        // TODO: Add in an about object to generate the about text programmatically
        about = new InfoTextPane(helpText);

        resizePanel.add(about);
        
        helpAbout.addActionListener(helpMenuActionListener);
		
	}
	
	private ActionListener helpMenuActionListener = new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		switch(((JMenuItem) e.getSource()).getText()) {
    			case "About":
    				// Set the canvas Size
    				JOptionPane.showConfirmDialog(null, resizePanel, "About", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
    				break;
				default:
					System.err.println("Unexpected option: "+((JMenuItem) e.getSource()).getText());
    		}
    	}
    };

}
