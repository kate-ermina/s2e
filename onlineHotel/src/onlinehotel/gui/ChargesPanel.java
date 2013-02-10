package onlinehotel.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import onlinehotel.model.db.SQLManager;

public class ChargesPanel extends JPanel {

	private SQLManager sqlManager;

	private JTextField roomNumField = new JTextField();
	private JTextField descrField = new JTextField();
	private JTextField priceField = new JTextField();
	
	public ChargesPanel(SQLManager sqlManager) {
		super();
		this.sqlManager = sqlManager;
		// Panel
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(createTextFieldWithLabel(roomNumField, "Room Num. : ", 9));
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(createTextFieldWithLabel(descrField, "Description : ", 12));
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(createTextFieldWithLabel(priceField, "Price : ", 46));
		add(Box.createRigidArea(new Dimension(0, 10)));	
		add(createAddChargeButton());
		add(Box.createRigidArea(new Dimension(10, 350)));
	}
	
	private JPanel createTextFieldWithLabel(JTextField textField, String label, int space) {
		JPanel tfPanel = new JPanel();
		tfPanel.setLayout(new BoxLayout(tfPanel, BoxLayout.LINE_AXIS));
		tfPanel.add(new JLabel(label));
		tfPanel.add(Box.createRigidArea(new Dimension(space, 0)));
		tfPanel.add(textField);
		tfPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		return tfPanel;
	}
	
	private JButton createAddChargeButton() {
		JButton button = new JButton("Add Charge");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addCharge();				
			}
		});
		return button;
	}
	
	private void addCharge() {
		String roomNumber = roomNumField.getText();
		String descr = descrField.getText();
		String priceStr = priceField.getText();
		if(roomNumber == null || roomNumber.trim().equals("")) {
			JOptionPane.showMessageDialog(this,"Please give a valid room number!",
				"Error",JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(descr == null || descr.trim().equals("")) {
			JOptionPane.showMessageDialog(this,"Please give a description!",
				"Error",JOptionPane.ERROR_MESSAGE);
			return;
		}
		try {
			Double price = Double.parseDouble(priceStr);
			int id = sqlManager.addNewCharge(roomNumber, descr, price);
			if (id < 0) {
				JOptionPane.showMessageDialog(this,"Cannot add new charge!",
						"Error",JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this,"Charge with description \"" 
						+ descr + "\"and price " + priceStr + " added for room " + roomNumber,
						"Process Complete",JOptionPane.INFORMATION_MESSAGE);
				roomNumField.setText(null);
				descrField.setText(null);
				priceField.setText(null);
			}
		}catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(this,"Please give a valid price!",
					"Error",JOptionPane.ERROR_MESSAGE);
		}
	}
}
