package gui;
import javax.swing.*;

import java.awt.*;
import java.rmi.RemoteException;

import gui.menus.CanvasMenu;
import gui.menus.ConnectMenu;
import gui.menus.FileMenu;
import gui.menus.HelpMenu;
import gui.menus.UsersMenu;
import net.Client;
import net.Server;
import shapes.ICanvasElement;

@SuppressWarnings("serial")
public class Whiteboard extends JFrame {
	
	public static final int LOCALMODE = 0;
	public static final int CLIENTMODE = 1;
	public static final int SERVERMODE = 2;
	public int clientServermode = 0;
	
	public int width = 800;
	public int height = 600;
	
	public Canvas canvas;
	
	private JMenuBar mainMenu;
	public ToolBar toolBar;
	
	public UserPanel userPanel;
	
	public Server server;
	public Client client;
	
	public FileMenu fileMenu;
	public ConnectMenu connectMenu;
	public CanvasMenu canvasMenu;
	public UsersMenu usersMenu;
	public HelpMenu helpMenu;

	public Whiteboard(){
		setResizable(false);
        setTitle("Distributed Whiteboard");
        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Set the UI style to match the platform
        try { 
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Call to second function because I don't want the constructor to be too bulky
        this.mainMenu = createMainMenu();
        getContentPane().add(mainMenu, BorderLayout.NORTH);
        
        // Create the canvas object
        this.canvas = new Canvas(this);
        canvas.setBackground(Color.WHITE);
        getContentPane().add(canvas, BorderLayout.CENTER);
        
        // Call to second function because I don't want the constructor to be too bulky
        toolBar = new ToolBar(this);
        getContentPane().add(toolBar, BorderLayout.SOUTH);
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
        	@Override
        	public void run() {
        		switch(clientServermode) {
        		case CLIENTMODE:
        			try {
						client.disconnect();
					} catch (RemoteException e1) {}
        			break;
        		case SERVERMODE:
        			server.remoteCanvas.disconnectAll();
    			case LOCALMODE:
    			default:
    				break;
        		}
        	}
        });
        
        for(Component c: this.getComponents()){
            c.setVisible(true);
        }
    }
	
	public void alert(String message) {
		JOptionPane.showMessageDialog(this, message);
	}
	
	public void killClient() {
		client = null;
		userPanel.setVisible(false);
		userPanel = null;
	}
	public void killServer() {
		server = null;
	}
	
	// ===================== Constructor Functions ============================
	private JMenuBar createMainMenu() {
		// Menu Bar
        JMenuBar mainMenu = new JMenuBar();
        
        // Abstracted menus into separate classes for separation of concerns
        
        fileMenu = new FileMenu(this);
        mainMenu.add(fileMenu);
        
        connectMenu = new ConnectMenu(this);
        mainMenu.add(connectMenu);
        
        canvasMenu = new CanvasMenu(this);
        mainMenu.add(canvasMenu);
        
        usersMenu = new UsersMenu(this);
        mainMenu.add(usersMenu);
        
        helpMenu = new HelpMenu(this);
        mainMenu.add(helpMenu);
	
		return mainMenu;
	}
	
	public void callback(ICanvasElement s) {
		if (this.clientServermode == Whiteboard.SERVERMODE){
			// Fire off a new object event
			try {
				server.rCanvas.newShape(s);
			} catch (RemoteException e) {
				// TODO: Server shape sending general exception
				e.printStackTrace();
			}
		} else if (this.clientServermode == Whiteboard.CLIENTMODE) {
			try {
				client.rCanvas.newShape(s);
			} catch (RemoteException e) {
				// TODO: Client shape sending exception
				e.printStackTrace();
			}
		}
	}
}
