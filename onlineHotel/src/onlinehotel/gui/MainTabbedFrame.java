package onlinehotel.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import onlinehotel.model.db.SQLManager;

public class MainTabbedFrame extends JFrame {

	private static final int CUSTOMER_MODE = 0;
	private static final int EMPLOYEE_MODE = 1;
	private static final int ADMIN_MODE = 2;
	
	private SQLManager sqlManager;
	
	public MainTabbedFrame(int mode) {
		super("Hotel Application");
		
		sqlManager = new SQLManager();
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
	    this.setLocation(320,80);
	    this.addWindowListener(new WindowAdapter() {
	    	@Override
	    	public void windowClosed(WindowEvent e) {
	    		super.windowClosed(e);
	    		sqlManager.close();
	    		System.exit(0);
	    	}
		});
	    //Add content to the window.
	    this.add(this.createMainTabbedPanel(mode), BorderLayout.CENTER);
	    
	    //Display the window.
	    this.pack();
	    this.setVisible(true);
	}
	
	private JPanel createMainTabbedPanel(int mode) {
		JPanel panel = new JPanel(new GridLayout(1, 1));
		JTabbedPane tabbedPane = new JTabbedPane();
		
		if(mode == CUSTOMER_MODE) {
			JComponent tab = new SearchBookRoomPanel(sqlManager);
			tabbedPane.addTab("Search/Book Room", null, tab, "Search for available rooms and book a selected room");
		} else if (mode == EMPLOYEE_MODE) {
			JComponent tab1 = new SearchBookRoomPanel(sqlManager);
			tabbedPane.addTab("Search/Book Room", null, tab1, "Search for available rooms and book a selected room");
			JComponent tab2 = new CheckInCheckOutPanel(sqlManager);
			tabbedPane.addTab("Check-in/Check-out", null, tab2, "Check-in/Check-out");
			JComponent tab3 = new ChargesPanel(sqlManager);
			tabbedPane.addTab("Charges", null, tab3, "Charges");
		} else {
			JComponent tab = new OfferPanel(sqlManager);
			tabbedPane.addTab("Offers Management", null, tab, "Offers Management");
		}
		
		// Add the tabbed pane to this panel.
		panel.add(tabbedPane);

		// The following line enables to use scrolling tabs.
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
				
		return panel;
	}
	
	protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }
}
