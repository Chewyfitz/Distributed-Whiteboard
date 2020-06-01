package net;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import shapes.ICanvasElement;

public interface IRemoteCanvas extends Remote {
	public class ServerState {
		public int numShapes;
		public int numClients;
		ServerState(int s, int c){
			numShapes = s;
			numClients = c;
		}
	}
	
	public ArrayList<ICanvasElement> connect(String uname) throws RemoteException;
	
	public ServerState heartbeat(Client u) throws RemoteException;
	
	public int rename(Client u, String username) throws RemoteException;
	public int disconnect(String username) throws RemoteException;
	public int registerCallback(IRemoteClient client) throws RemoteException;
	
	public ArrayList<ICanvasElement> getElements(int[] ids) throws RemoteException;
	public ArrayList<String> getUserList() throws RemoteException;
	
	public int newShape(ICanvasElement e) throws RemoteException;

}
