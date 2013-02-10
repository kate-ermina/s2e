package onlinehotel.gui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class OnlineHotel {

	/**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from
     * the event dispatch thread.
     */
    private static void createAndShowGUI() {
    	LoginFrame frame = new LoginFrame();
		frame.displayWindow();
    }
    
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
            	UIManager.put("swing.boldMetal", Boolean.FALSE);
            	createAndShowGUI();
            }
        });
    }

}
