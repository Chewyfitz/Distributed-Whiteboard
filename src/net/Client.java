package net;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import gui.Canvas;
import gui.Whiteboard;
import net.IRemoteCanvas;
import shapes.ICanvasElement;

public class Client extends Thread implements IRemoteClient{
	
	public static final int DISCONNECTED = -1;
	public static final int CONNECTED = 0;
	
	public static final int clientPort = 8081;
	public String username;
	public int state;
	
	private int port;
	private int callbackPort;
	private Registry r;
	public IRemoteCanvas rCanvas;
	private Canvas canvas;
	
	/**
	 * A Server's representation of a client.
	 * @param u - Username
	 */
	public Client(String u) {
		username = u;
	}
	
	/**
	 * @param u - Username
	 */
	public Client(String u, int p, String ip, int cbp, Canvas canv) {
		username = u;
		port = p;
		callbackPort = cbp;
		canvas = canv;
		
		try {
			// Connect to RMI registry
			r = LocateRegistry.getRegistry(ip, port);
			
			//Retrieve stub for remote canvas
			rCanvas = (IRemoteCanvas) r.lookup("DistributedWhiteboard");
			UnicastRemoteObject.exportObject(this, callbackPort);
			// TODO: Client connection failure
			// TODO: Client connection rejected
			// TODO: Client connection Username exists
			if(rCanvas.registerCallback(this) == -1) {
				state = -1;
			} else {
				state = 0;
				connect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addUser(String user) throws RemoteException{
		canvas.whiteboard.userPanel.addUser(user);
	}
	
	public void kickUser(String user) throws RemoteException{
		canvas.whiteboard.userPanel.kick(user);
	}
	
	public ArrayList<String> getUserList() throws RemoteException{
		return rCanvas.getUserList();
	}
	
	private int connect() throws RemoteException {
		ArrayList<ICanvasElement> serverCanvasElems = rCanvas.connect(username);
		for(ICanvasElement e : serverCanvasElems) {
			System.out.println("Adding Element");
			canvas.addElement(e);
		}
		return 0;
	}
	
	@Override
	public int newShape(ICanvasElement shape) throws RemoteException {
		canvas.addElement(shape);
		canvas.getCanvasElements().size();
		return 0;
	}
	@Override
	public void setCanvasSize(int width, int height) throws RemoteException{
		canvas.setSize(width, height);
		canvas.whiteboard.setSize(width, height + canvas.whiteboard.toolBar.getHeight());
	}
	
	@Override
	public String getUsername() throws RemoteException{
		return username;
	}
	
	@Override
	public void disconnect() throws RemoteException {
		canvas.setClientServerMode(Canvas.LOCALMODE);
		canvas.whiteboard.clientServermode = Whiteboard.LOCALMODE;
		canvas.whiteboard.killClient();
		canvas.whiteboard.connectMenu.host.setEnabled(true);
		canvas.whiteboard.connectMenu.connect.setEnabled(true);
		canvas.whiteboard.connectMenu.disconnect.setEnabled(false);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				canvas.whiteboard.alert("You have been disconnected.");
			}
		}, 1);
	}
}
