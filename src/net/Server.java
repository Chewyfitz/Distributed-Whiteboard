package net;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import gui.Canvas;
import net.IRemoteCanvas;

public class Server extends Thread{

	public Canvas canvas;
	public IRemoteCanvas rCanvas;
	public RemoteCanvas remoteCanvas;
	
	public Server(Canvas canvas, int port){
		super();
		
		this.canvas = canvas;
		try {
			remoteCanvas = new RemoteCanvas(canvas);
			rCanvas = remoteCanvas;
			Registry registry = LocateRegistry.createRegistry(port);
			registry.bind("DistributedWhiteboard", rCanvas);
		} catch (RemoteException e) {
			// TODO: Server general exception
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			// TODO: Server port already bound
			e.printStackTrace();
		}
	}
	
	public void setSize(int width, int height) {
		remoteCanvas.setSize(width, height);
	}
	
	public ArrayList<String> getUserList() throws RemoteException{
		return rCanvas.getUserList();
	}
	
	

}
