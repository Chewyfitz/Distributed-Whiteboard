package gui.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import gui.Whiteboard;

public class FileMenu extends JMenu{

	private static final long serialVersionUID = -7070803912820774891L;

	Whiteboard whiteboard;
	public JFileChooser fileChooser = new JFileChooser();
	FileNameExtensionFilter filter;
	
	JMenuItem newFile;
	JMenuItem open;
	JMenuItem save;
	JMenuItem saveAs;
	JMenuItem close; 
	
	public FileMenu(Whiteboard wb) {
		super("File");
		whiteboard = wb;
		setFilter();
		
        // Define Menu items
        newFile = new JMenuItem("New");
        open = new JMenuItem("Open");
        save = new JMenuItem("Save");
        saveAs = new JMenuItem("Save As...");
        close = new JMenuItem("Close");
        
        // Set event listeners
        newFile.addActionListener(fileMenuListener);
        open.addActionListener(fileMenuListener);
        save.addActionListener(fileMenuListener);
        saveAs.addActionListener(fileMenuListener);
        close.addActionListener(fileMenuListener);
		
        // Add Menu items to menu (decides order)
        add(newFile);
        add(open);
        add(save);
        add(saveAs);
        add(close);
	}
	
	private void setFilter() {
		filter = new FileNameExtensionFilter("PNG, GIF, JPG", "png", "gif", "jpg");
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(filter);
	}
	
	private ActionListener fileMenuListener = new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		File file = null;
    		switch(((JMenuItem) e.getSource()).getText()) {
    			case "New":
    				// Clear the canvas
    				whiteboard.canvas.clearCanvas();
    				break;
    			case "Open":
    				if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        				file = fileChooser.getSelectedFile();
    				}
    				if(file != null) {
    					shapes.Image img =new shapes.Image(file);
    					whiteboard.canvas.addElement(img);
    					if(whiteboard.clientServermode == Whiteboard.SERVERMODE) {
    						whiteboard.server.remoteCanvas.newShape(img);
    					}
    				}
    				break;
    			case "Save":
    				// Does the same as "Save As" for now (lots of work to cache the file path)
    			case "Save As...":
    				if(fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
        				file = fileChooser.getSelectedFile();
    				}
    				if(file != null) {
    					whiteboard.canvas.save(file);
    				}
    				break;
    			case "Close":
				default:
					whiteboard.dispatchEvent(new WindowEvent(whiteboard, WindowEvent.WINDOW_CLOSING));
    		}
    	}
    };
	
}
