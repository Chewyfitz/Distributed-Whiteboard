package gui;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class InfoTextPane extends JTextPane{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InfoTextPane(String infoText){
		super();
		setText(infoText);
		setEditable(false);
		setOpaque(false);
		
		// Align Center
		SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setAlignment(attr, StyleConstants.ALIGN_CENTER);
        getStyledDocument().setParagraphAttributes(0, infoText.length(), attr, false);

	}
}
