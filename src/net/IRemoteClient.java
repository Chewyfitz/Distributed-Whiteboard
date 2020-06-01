package net;

import java.rmi.Remote;
import java.rmi.RemoteException;

import shapes.ICanvasElement;

public interface IRemoteClient extends Remote{
	
	public static final int FAILURE = -1;
	public static final int SUCCESS = 0;
	
	public String getUsername() throws RemoteException;
	public int newShape(ICanvasElement shape) throws RemoteException;
	
	public void setCanvasSize(int width, int height) throws RemoteException;
	
	public void addUser(String user) throws RemoteException;
	public void kickUser(String user) throws RemoteException;
	
	public void disconnect() throws RemoteException;

}
