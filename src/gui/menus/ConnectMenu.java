package gui.menus;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

import gui.Canvas;
import gui.InfoTextPane;
import gui.UserPanel;
import gui.Whiteboard;
import net.Client;
import net.Server;

public class ConnectMenu extends JMenu {

	private static final long serialVersionUID = 269999785165049946L;
	
	private Whiteboard whiteboard;
	
	public JMenuItem connect;
    public JMenuItem host;
    public JMenuItem disconnect;

    
    public JPanel connectPanel;
    public JFormattedTextField callbackPort;
    public JFormattedTextField ipAddrInput;
    public JFormattedTextField remotePort;
    public JFormattedTextField clientUsername;

    public JPanel hostPanel;
    public JFormattedTextField hostPort;
    public JTextField serverUsername;

    int port = 8080;
    int selfPort = 8081;
    NumberFormat portFormat;
	
	public ConnectMenu(Whiteboard wb) {
		super("Connect");
		whiteboard = wb;
		
		// Create Menu Items
		connect = new JMenuItem("Connect to Host"); 
		host = new JMenuItem("Host a Whiteboard");        
		disconnect = new JMenuItem("Disconnect");
		
		// Set Options
		disconnect.setEnabled(false);
		
		// Create panels
		portFormat = new DecimalFormat("#");
	    portFormat.setMinimumIntegerDigits(4); // Don't allow a port number less than 1000.
	    portFormat.setGroupingUsed(false);
		connectPane();
		hostPane();
		
        connect.addActionListener(connectActionListener);
        host.addActionListener(connectActionListener);
        disconnect.addActionListener(connectActionListener);
        
        // Add items to menu
        add(connect);
        add(host);
        add(disconnect);
		
	}
	
	private void connectPane(){
		// Panel for "Connect to Host"
		connectPanel = new JPanel(new GridLayout(0,1,5,5));
		
		// Create Panel components for this panel		
		JPanel clientPortPanel = new JPanel();
        clientPortPanel.add(new JLabel("Callback Port: "));
        
		callbackPort = new JFormattedTextField(new NumberFormatter(portFormat));
        callbackPort.setColumns(7);
        callbackPort.setValue(selfPort); // Default hosting port
        clientPortPanel.add(callbackPort);
        
        JPanel clientIPPanel = new JPanel();
        clientIPPanel.add(new JLabel("IP Address: "));
        
        ipAddrInput = new JFormattedTextField();
        ipAddrInput.setColumns(16);
        clientIPPanel.add(ipAddrInput);
        
        remotePort = new JFormattedTextField(new NumberFormatter(portFormat));
        remotePort.setColumns(7);
        remotePort.setValue(port);
        clientIPPanel.add(remotePort);
        
        JPanel clientUsernamePanel = new JPanel();
        clientUsernamePanel.add(new JLabel("Username: "));
        
        clientUsername = new JFormattedTextField();
        clientUsername.setColumns(20);
        clientUsernamePanel.add(clientUsername);
        
        // Add some info text and put everything on the panel.
        InfoTextPane connectInfo = new InfoTextPane("Enter the host's IP address and Port, then click \"OK\" to connect.");
        connectPanel.add(connectInfo);
        connectPanel.add(clientUsernamePanel);
        connectPanel.add(clientIPPanel);
        connectPanel.add(clientPortPanel);
	}
	
	private void hostPane(){
		// Panel for "Host a Whiteboard"
		hostPanel = new JPanel(new GridLayout(0,1,5,5));
        
		// Create Panel components for this panel
        JPanel hostPortPanel = new JPanel();
        hostPortPanel.add(new JLabel("Port: "));
        
        hostPort = new JFormattedTextField(new NumberFormatter(portFormat));
        hostPort.setColumns(7);
        hostPort.setValue(port); // Default hosting port
        hostPortPanel.add(hostPort);
        
        JPanel serverUsernamePanel = new JPanel();
        serverUsernamePanel.add(new JLabel("Username: "));
        
        serverUsername = new JFormattedTextField();
        serverUsername.setColumns(20);
        serverUsernamePanel.add(serverUsername);
        
        // Add some info text and put everything on the panel.
        InfoTextPane hostInfo = new InfoTextPane("Input a port number to host the whiteboard server\nClients can connect via your IP address and this port number.");
        hostPanel.add(hostInfo);
        hostPanel.add(hostPortPanel);
	}
	
	
	
	
	private  ActionListener connectActionListener = new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		int result = 5; // 5 is a nice number and nobody will convince me otherwise
    		// also it isn't 0.
    		switch(((JMenuItem) e.getSource()).getText()) {
    			case "Connect to Host":
    				// Set the canvas Size
    				result = JOptionPane.showConfirmDialog(whiteboard.canvas, connectPanel, "Connect to Host", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    				System.out.println(clientUsername.getText() + " | " + result + ": " + remotePort.getValue() + ", " + callbackPort.getValue());
    				if(result != 0) break;
    				whiteboard.canvas.clearCanvas();
    				
    				whiteboard.client = 
						new Client(clientUsername.getText()
							, Integer.parseInt(remotePort.getValue().toString())
    						, (String)ipAddrInput.getValue() 
    						, Integer.parseInt(callbackPort.getValue().toString())
    						, whiteboard.canvas
    						);
    				if(whiteboard.client.state == Client.CONNECTED) {
    					connect.setEnabled(false);
        				host.setEnabled(false);
        				disconnect.setEnabled(true);
        				whiteboard.canvasMenu.resize.setEnabled(false);
        				whiteboard.fileMenu.open.setEnabled(false);
        				whiteboard.fileMenu.save.setEnabled(false);
        				whiteboard.fileMenu.saveAs.setEnabled(false);
        				whiteboard.canvasMenu.resize.setEnabled(false);
        				whiteboard.usersMenu.userList.setEnabled(true);
        				try {
							whiteboard.userPanel = new UserPanel(whiteboard.client.getUserList(), UserPanel.Mode.CLIENTMODE);
							whiteboard.userPanel.setVisible(true);
						} catch (RemoteException e1) {
							// TODO: UserList get exception
							e1.printStackTrace();
						}
        				whiteboard.canvas.setClientServerMode(Canvas.CLIENTSERVERMODE);
        				whiteboard.clientServermode = Whiteboard.CLIENTMODE;
    				}
    				break;
    			case "Host a Whiteboard":
    				// Set the canvas Size
    				result = JOptionPane.showConfirmDialog(whiteboard.canvas, hostPanel, "Host a Whiteboard", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    				System.out.println(result + ": " + hostPort.getValue());
    				if(result != 0) break;
    				whiteboard.server = new Server(whiteboard.canvas, (int) hostPort.getValue());
    				connect.setEnabled(false);
    				host.setEnabled(false);
					try {
						whiteboard.userPanel = new UserPanel(whiteboard.server.getUserList(), UserPanel.Mode.SERVERMODE, whiteboard.server);
						whiteboard.userPanel.setVisible(true);
					} catch (RemoteException e1) {
						// TODO: Something if Server Userpanel doesn't work
						e1.printStackTrace();
					}
					whiteboard.canvas.setClientServerMode(Canvas.CLIENTSERVERMODE);
					whiteboard.clientServermode = Whiteboard.SERVERMODE;
    				break;
    			case "Disconnect":
    				if(whiteboard.clientServermode == Whiteboard.CLIENTMODE) {
    					try {
							whiteboard.client.disconnect();
							whiteboard.canvas.clearCanvas();
						} catch (RemoteException e1) {
							// TODO: Something if disconnecting doesn't work; 
							e1.printStackTrace();
						}
    				}
    				break;
				default:
					System.err.println("Unexpected option: "+((JMenuItem) e.getSource()).getText());
    		}
    	}
    };

}


