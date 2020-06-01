package net;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

import gui.Canvas;
import net.IRemoteCanvas;
import shapes.ICanvasElement;

public class RemoteCanvas extends UnicastRemoteObject implements IRemoteCanvas{

	private static final long serialVersionUID = -2882115257452011596L;
	Canvas canvas;
	HashMap<String, IRemoteClient> usernames;
	HashMap<IRemoteClient, String> reverseUsernames;
	public ArrayList<IRemoteClient> users;
	
	
	protected RemoteCanvas(Canvas c) throws RemoteException {
		super();
		usernames = new HashMap<String, IRemoteClient>();
		reverseUsernames = new HashMap<IRemoteClient, String>();
		users = new ArrayList<IRemoteClient>();
		canvas = c;
	}

	/**
	 * Connect to the server to get all of the canvas elements so that it can be drawn
	 * on the client side
	 */
	@Override
	public ArrayList<ICanvasElement> connect(String username) throws RemoteException {
		return canvas.getCanvasElements();
	}
	
	private int registerUsername(String username, IRemoteClient client) {
		int success = -1;
		if(!usernames.containsKey(username)) {
			usernames.put(username, client);
			reverseUsernames.put(client, username);
			canvas.whiteboard.userPanel.addUser(username);
			for(IRemoteClient u : users) {
				try {
					u.addUser(username);
				} catch (RemoteException e1) {
					deadClient(reverseUsernames.get(u));
				}
			}
			users.add(client);
			return 0;
		}
		return success;
	}
	
	public ServerState heartbeat(Client u) throws RemoteException {
		ServerState state = new ServerState(canvas.getCanvasElements().size(), users.size());
		return state;
	}

	@Override
	public int rename(Client u, String username) throws RemoteException {
		// TODO: Change name of client in client list
		return 0;
	}

	@Override
	public int disconnect(String username) throws RemoteException {
		if(usernames.containsKey(username)) {
			IRemoteClient client = usernames.get(username);
			users.remove(client);
			reverseUsernames.remove(client);
			usernames.remove(username);
			try {
				client.disconnect();
			} catch (RemoteException e) {
				// Do nothing, client is dead.
			}
			for(IRemoteClient u : users) {
				try {
					u.kickUser(username);
				} catch (RemoteException e1) {
					deadClient(reverseUsernames.get(u));
				}
			}
			canvas.whiteboard.userPanel.kick(username);
		} else {
			return -1;
		}
		return 0;
	}
	public void deadClient(String username) {
		try {
			disconnect(username);
		} catch (RemoteException e){}
	}
	public void disconnectAll() {
		for(IRemoteClient c: users) {
			try {
				c.disconnect();
			} catch (RemoteException e) {
				// Do nothing
			}
		}
	}

	@Override
	public ArrayList<ICanvasElement> getElements(int[] ids) throws RemoteException {
		ArrayList<ICanvasElement> elements = new ArrayList<>();
		for(int i : ids ) {
			elements.add(i, canvas.getCanvasElements().get(i));
		}
		return elements;
	}
	
	@Override
	public ArrayList<String> getUserList() throws RemoteException {
		ArrayList<String> users = new ArrayList<>();
		users.addAll(usernames.keySet());
		return users;
	}
	
	@Override
	public int registerCallback(IRemoteClient client) throws RemoteException {
		System.out.println("rc Got client: " + client.getUsername());
		if(canvas.addClient(client) == 0) {
			if(registerUsername(client.getUsername(), client) == 0) {				
				return 0;
			} else {
				return -1;
			}
		} else {
			return -2;
		}
	}

	@Override
	public int newShape(ICanvasElement e) {
		canvas.addElement(e);
		for(IRemoteClient u : users) {
			try {
				u.newShape(e);
			} catch (RemoteException e1) {
				deadClient(reverseUsernames.get(u));
			}
		}
		return 0;
	}
	
	public void setSize(int width, int height){
		for(IRemoteClient u : users) {
			try {
				u.setCanvasSize(width, height);
			} catch (RemoteException e1) {
				deadClient(reverseUsernames.get(u));
			}
		}
	}
	
}
