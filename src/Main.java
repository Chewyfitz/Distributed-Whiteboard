import gui.Whiteboard;

public class Main {

    public static void main(String[] args) {
    	// Launch the whiteboard (This could probably be done from the Whiteboard class,
    	// but I think separating them is better)
        Whiteboard whiteboard = new Whiteboard();
        whiteboard.setVisible(true);
    }
}
