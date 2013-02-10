package onlinehotel.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import onlinehotel.users.UserManager;


public class LoginFrame extends JFrame {

	private UserManager manager = new UserManager();
	
	// Components
	private JComboBox loginAsCombo = new JComboBox(new String[] {"Customer","Employee","Admin"});
	private JTextField usernameField = new JTextField(20);
	private JPasswordField passwordField = new JPasswordField(20);
	
	public LoginFrame() {
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setResizable(false);
	    this.setLocation(400,200);
	    //Add content to the window.
	    this.add(this.createLoginPanel(), BorderLayout.CENTER);
	}
	
	public void displayWindow() {
		this.pack();
        this.setVisible(true);
	}
	
	private JComponent createLoginPanel() {
		JPanel loginPanel = new JPanel();
		loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
		loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
		
		loginPanel.add(this.createLoginAsComponent());
		loginPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		loginPanel.add(this.createUsernameComponent());
		loginPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		loginPanel.add(this.createPasswordComponent());
		loginPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		loginPanel.add(this.createButtonsComponent());	
		return loginPanel;
	}
	
	private JComponent createLoginAsComponent() {
		// Components
		JLabel loginAsLabel = new JLabel("Login as : ");
		loginAsCombo.setSelectedIndex(0);
		loginAsCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JComboBox comboBox = (JComboBox)event.getSource();
				int value = comboBox.getSelectedIndex();
				if (value == 0) {
					usernameField.setEnabled(false);
					usernameField.setText(null);
					passwordField.setEnabled(false);
					passwordField.setText(null);
				} else {
					usernameField.setEnabled(true);
					passwordField.setEnabled(true);
				}
			}
		});
		// Panel
		JPanel loginAsPanel = new JPanel();
		loginAsPanel.setLayout(new BoxLayout(loginAsPanel, BoxLayout.LINE_AXIS));
		loginAsPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		loginAsPanel.add(loginAsLabel);
		loginAsPanel.add(Box.createRigidArea(new Dimension(20, 0)));
		loginAsPanel.add(loginAsCombo);
		return loginAsPanel;
	}
	
	private JComponent createUsernameComponent() {
		// Components
		JLabel label = new JLabel("Username : ");
		usernameField.setEnabled(false);
		// Panel
		JPanel component = new JPanel();
		component.setLayout(new BoxLayout(component, BoxLayout.LINE_AXIS));
		component.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		component.add(label);
		component.add(Box.createRigidArea(new Dimension(10, 0)));
		component.add(usernameField);
		return component;
	}
	
	private JComponent createPasswordComponent() {
		// Components
		JLabel label = new JLabel("Password : ");
		passwordField.setEnabled(false);
		// Panel
		JPanel component = new JPanel();
		component.setLayout(new BoxLayout(component, BoxLayout.LINE_AXIS));
		component.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		component.add(label);
		component.add(Box.createRigidArea(new Dimension(12, 0)));
		component.add(passwordField);
		return component;
	}
	
	private JComponent createButtonsComponent() {
		// Components
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int loginAs = loginAsCombo.getSelectedIndex();
				String username = usernameField.getText();
				String password = new String(passwordField.getPassword());
				loginUser(username, password, loginAs);
			}
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Cancel, going to terminate!");
				System.exit(0);
			}
		});
		// Panel
		JPanel component = new JPanel();
		component.setLayout(new BoxLayout(component, BoxLayout.LINE_AXIS));
		component.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		component.add(okButton);
		component.add(Box.createRigidArea(new Dimension(10, 0)));
		component.add(cancelButton);
		return component;
	}
	
	private void loginUser(String username, String password, final int mode) {
		boolean isValidUser = false;
		if(mode == 0) {
			isValidUser = true;
		} else if (mode == 1) {
			isValidUser = manager.isValidEmployee(username, password);
		} else {
			isValidUser = manager.isValidAdmin(username, password);
		}
		if(isValidUser) {
			System.out.println("User is valid");
			this.setVisible(false);
			SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                //Turn off metal's use of bold fonts
	            	MainTabbedFrame mainFrame = new MainTabbedFrame(mode);
	            }
	        });
		} else {
			System.out.println("User is NOT valid");
			JOptionPane.showMessageDialog(this,"Wrong username/password",
				    "Login error",JOptionPane.ERROR_MESSAGE);
		}
	}
	
}
