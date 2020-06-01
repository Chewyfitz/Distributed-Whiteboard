package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import com.jgoodies.forms.factories.DefaultComponentFactory;

public class ToolBar extends JToolBar {
	
	// generated
	private static final long serialVersionUID = -1095840009332172965L;
	
	public Whiteboard whiteboard;
	
	public final ButtonGroup selectedTool = new ButtonGroup();
	public JSpinner lineWeight = new JSpinner();
	public JSpinner fontWeight = new JSpinner();
	
	public JButton btnSelectColour;
	public JFrame colourFrame;
	public JColorChooser colourChooser = new JColorChooser(Color.BLACK);
	
	JButton btnSelectFillColour;
	public JFrame fillColourFrame;
	public JColorChooser fillColourChooser = new JColorChooser(new Color(0, 0, 0, 0));
	
	public ToolBar(Whiteboard wb){
		whiteboard = wb;
		
        addToolBarButton(selectedTool, "Line");
        addToolBarButton(selectedTool, "Rectangle");
        addToolBarButton(selectedTool, "Circle");
        addToolBarButton(selectedTool, "Text");
        
        // Set the first tool (Line) as selected so we can avoid null pointer references.
        selectedTool.setSelected(selectedTool.getElements().nextElement().getModel(), true);
        
        JSeparator separator = new JSeparator();
        separator.setRequestFocusEnabled(false);
        separator.setMaximumSize(new Dimension(10, 32767));
        separator.setOrientation(SwingConstants.VERTICAL);
        add(separator);
        
        // Size selectors for line weight and font size
        JLabel lblLineWeight = DefaultComponentFactory.getInstance().createLabel("Weight: ");
        add(lblLineWeight);
        
        lineWeight.setMaximumSize(new Dimension(50, 32767));
        lineWeight.setModel(new SpinnerNumberModel(1, 1, null, 1));
        lblLineWeight.setLabelFor(lineWeight);
        add(lineWeight);
        
        JLabel lblFontWeight = DefaultComponentFactory.getInstance().createLabel("Font Size: ");
        add(lblFontWeight);
        
        fontWeight.setMaximumSize(new Dimension(50, 32767));
        fontWeight.setModel(new SpinnerNumberModel(16, 1, null, 1));
        lblFontWeight.setLabelFor(fontWeight);
        add(fontWeight);
        
        // Colour selectors for line colour and fill colour
        // (fill colour only applies to circle and rectangle)
        colourFrame = new JFrame("Line Colour");
		colourFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		colourFrame.getContentPane().add(colourChooser);
		colourFrame.pack();
		
		btnSelectColour = new JButton("Line Colour");        
		btnSelectColour.addMouseListener(lineColourVisible);
        colourFrame.addComponentListener(updateButtonColour);
        add(btnSelectColour);
        
        // Fill Colour
        fillColourFrame = new JFrame("Fill Colour");
        fillColourFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        fillColourFrame.getContentPane().add(fillColourChooser);
        fillColourFrame.pack();
		
        btnSelectFillColour = new JButton("Fill Colour");
        btnSelectFillColour.addMouseListener(fillColourVisible);
        
        fillColourFrame.addComponentListener(updateButtonFillColour);
        add(btnSelectFillColour);
		
	}
	
	private MouseAdapter lineColourVisible = new MouseAdapter() {
    	@Override
    	public void mouseClicked(MouseEvent e) {
    		colourFrame.setVisible(true);
    	}
    };
    
    private ComponentAdapter updateButtonColour = new ComponentAdapter() {
		public void componentHidden(ComponentEvent e) {
			btnSelectColour.setBackground(colourChooser.getColor());
		}
		public void componentShown(ComponentEvent e) {
			// Do nothing
		}
	};
	
	private MouseAdapter fillColourVisible = new MouseAdapter() {
    	@Override
    	public void mouseClicked(MouseEvent e) {
    		fillColourFrame.setVisible(true);
    	}
    };
    
    private ComponentAdapter updateButtonFillColour = new ComponentAdapter() {
		public void componentHidden(ComponentEvent e) {
			btnSelectFillColour.setBackground(fillColourChooser.getColor());
		}
		public void componentShown(ComponentEvent e) {
			// Do nothing
		}
	};

	private void addToolBarButton(ButtonGroup st, String l) {
		JToggleButton b = new JToggleButton(" "+l+" ");
		b.setActionCommand(l);
		st.add(b);
		add(b);
	}

}
