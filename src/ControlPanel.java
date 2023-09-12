/*
OG:
https://docs.oracle.com/javase/tutorial/uiswing/examples/components/
FormattedTextFieldDemoProject/src/components/FormattedTextFieldDemo.java
 */ 
import java.awt.*;

import javax.swing.*;

import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.beans.PropertyChangeEvent;

import java.text.*;

public class ControlPanel extends JPanel
                                    implements PropertyChangeListener {

	//flag so that we can programmatically change values without activating the listener
	private boolean isProgrammatic;
	
	public static ControlPanel controlPanel;
	
    //Strings for the labels
    private static String maxString = "Max: ";
    private static String powerString = "Power: ";
    private static String sizeString = "Size: ";
    private static String cordsString = "Cords: ";

    //Fields for data entry
    private JFormattedTextField maxField;
    private JFormattedTextField powerField;
    private JFormattedTextField sizeField;
    private JFormattedTextField cordsField;

    //Formats to format and parse numbers
    private NumberFormat doubleFormat;
    private NumberFormat intFormat;

    public ControlPanel() {
        super(new BorderLayout());
        setUpFormats();

        //Create the labels.
        JLabel maxLabel = new JLabel(maxString);
        JLabel powerLabel = new JLabel(powerString);
        JLabel sizeLabel = new JLabel(sizeString);
        JLabel cordsLabel = new JLabel(cordsString);

        //Create the text fields and set them up.
        maxField = new JFormattedTextField(intFormat);
        maxField.setColumns(26);

        powerField = new JFormattedTextField(intFormat);
        powerField.setColumns(26);

        sizeField = new JFormattedTextField(doubleFormat);
        sizeField.setColumns(26);
        
        cordsField = new JFormattedTextField();
        cordsField.setColumns(26);
        
        setFields();
        
        maxField.addPropertyChangeListener("value", this);
        powerField.addPropertyChangeListener("value", this);
        sizeField.addPropertyChangeListener("value", this);
        cordsField.addPropertyChangeListener("value", this);

        //Tell accessibility tools about label/textfield pairs.
        maxLabel.setLabelFor(maxField);
        powerLabel.setLabelFor(powerField);
        sizeLabel.setLabelFor(sizeField);
        cordsLabel.setLabelFor(cordsField);

        //Lay out the labels in a panel.
        JPanel labelPane = new JPanel(new GridLayout(0,1));
        labelPane.add(maxLabel);
        labelPane.add(powerLabel);
        labelPane.add(sizeLabel);
        labelPane.add(cordsLabel);

        //Layout the text fields in a panel.
        JPanel fieldPane = new JPanel(new GridLayout(0,1));
        fieldPane.add(maxField);
        fieldPane.add(powerField);
        fieldPane.add(sizeField);
        fieldPane.add(cordsField);

        //Put the panels in this panel, labels on left,
        //text fields on right.
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(labelPane, BorderLayout.CENTER);
        add(fieldPane, BorderLayout.LINE_END);
    }
    
    public void setFields() {
    	isProgrammatic = true;
    	System.out.println("programmatic set fields");
        maxField.setValue(Main.max);
        powerField.setValue(Main.power);
        sizeField.setValue(Main.size);
        cordsField.setValue(Main.xc+","+Main.yc);
        isProgrammatic = false;
    }
    
    /** Called when a field's "value" property changes. */
    public void propertyChange(PropertyChangeEvent e) {
    	//so only user input activates the event
    	if (isProgrammatic)
    		return;
    	
    	System.out.println("-----user changed control panel field-----");
    	Object source = e.getSource();
    	
    	if (source == maxField) {
    		Main.max = ((Number)maxField.getValue()).intValue();
    	}
    	else if (source == powerField) {
    		Main.power = ((Number)powerField.getValue()).intValue();
    	}
    	else if (source == sizeField) {
    		Main.size = ((Number)sizeField.getValue()).doubleValue();
    	}
    	else if (source == cordsField) {
    		double[] cordArray = parseCords(cordsField.getValue().toString());
    		if (cordArray.length!=2) { //if there was invalid cords the length will be 1 and spit out an error
    			return;
    		}
			Main.xc = cordArray[0];
			Main.yc = cordArray[1];
    	}
    	Main.Update();
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("ControlPanel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add contents to the window.
        controlPanel = new ControlPanel();
        frame.add(controlPanel);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        try {
			SwingUtilities.invokeAndWait(new Runnable() {
			    public void run() {
			        //Turn off metal's use of bold fonts
			    UIManager.put("swing.boldMetal", Boolean.FALSE);
			        createAndShowGUI();
			    }
			});
		} catch (InvocationTargetException e) {
			System.err.println("InvocationTargetException error "+e);
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.err.println("InterruptedException error "+e);
			e.printStackTrace();
		}
    }

    //Create and set up number formats. These objects also
    //parse numbers input by user.
    private void setUpFormats() {
        doubleFormat = NumberFormat.getNumberInstance();
        doubleFormat.setMaximumFractionDigits(16);

        intFormat = NumberFormat.getNumberInstance();
        intFormat.setMaximumFractionDigits(0);
    }
    private double[] parseCords(String text) {
    	text.replaceAll("\\s+",""); //get rid of spaces
        String[] parts = text.split(",");
        try {
	        double x = Double.parseDouble(parts[0]);
	        double y = Double.parseDouble(parts[1]);
	        return new double[] {x,y};
        } catch (Exception e) {
            System.err.println("Invalid coordinate format.");
            return new double[] {-1};
        }
    }
}


