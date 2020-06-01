package gui;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.awt.event.ActionEvent;

public class UserPanel extends JFrame {
	
	// generated
	private static final long serialVersionUID = 137421849102229635L;
	
	
	public enum Mode {
		SERVERMODE,
		CLIENTMODE
	}
	public ArrayList<String> userList;
	public HashMap<String, JPanel> userLists;
	
	private JPanel listContainer;
	private Mode mode;
	private net.Server server;
	
	public UserPanel(ArrayList<String> ul, Mode m, net.Server server) {
		super("User Panel");
		this.server = server;
		init(ul, m);
	}
	
	public UserPanel(ArrayList<String> ul, Mode m){
		super("User Panel");
		init(ul, m);
	}
	
	private void init(ArrayList<String> ul, Mode m) {
		listContainer = new JPanel();
		mode = m;
		
		getContentPane().add(listContainer, BorderLayout.NORTH);
		getContentPane().setVisible(true);
		setSize(200, 200);
		listContainer.setLayout(new GridLayout(0, 1, 0, 0));
		
		userLists = new HashMap<>();
		userList = ul;
		for(String u: ul) {
			addUser(u);
		}
	}
	
	public void addUser(String username) {
		JPanel panel = new JPanel();
		
		JTextField userName = new JTextField(username);
		Component horizontalGlue = Box.createHorizontalGlue();
		JButton kickButton = new JButton("Kick");
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		userName.setEnabled(false);
		userName.setEditable(false);
		if(mode == Mode.CLIENTMODE) {
			kickButton.setEnabled(false);
		}
		
		kickButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				kick(username);
			}
		});
		
		panel.add(userName);
		panel.add(horizontalGlue);
		panel.add(kickButton);
		
		listContainer.add(panel);
		userLists.put(username, panel);
		revalidate();
		repaint();
	}
	
	public void kick(String username) {
		if(mode == Mode.SERVERMODE) {
			try {
				server.rCanvas.disconnect(username);
			} catch (RemoteException e) {
				// TODO: Something if kick doesn't work
				e.printStackTrace();
			}
		}
		JPanel tmp;
		try {
			tmp = userLists.get(username);
			listContainer.remove(tmp);
		} catch (NullPointerException e) {
			// Do nothing, this isn't unexpected.
		}
		userLists.remove(username);
		
		revalidate();
		repaint();
		System.out.println("Kicked: "+username);
	}
}
