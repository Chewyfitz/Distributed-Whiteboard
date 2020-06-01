package gui.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import gui.Whiteboard;

public class UsersMenu extends JMenu {

	private static final long serialVersionUID = 6668472519073134347L;
	
	private Whiteboard whiteboard;
	
	public JMenuItem userList;
	public JMenuItem userKick;
	
	public UsersMenu(Whiteboard wb) {
		super("Users");
		whiteboard = wb;
		
		userList = new JMenuItem("View User List");
		
		userList.setEnabled(false);
		userList.addActionListener(userListListener);
        
        add(userList);
	}
	
	private ActionListener userListListener = new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		if(whiteboard.userPanel != null) {
    			whiteboard.userPanel.setVisible(true);
    		} else {
    			userList.setEnabled(false);
    		}
    	}
    }; 


}
