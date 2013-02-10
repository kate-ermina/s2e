package onlinehotel.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import onlinehotel.model.Charge;
import onlinehotel.model.CheckInDetails;
import onlinehotel.model.db.SQLManager;

public class CheckInCheckOutPanel extends JPanel {

	private SQLManager sqlManager;

	private JTextField bookingIdField = new JTextField();
	private JTextField roomNumberField = new JTextField();
	
	public CheckInCheckOutPanel(SQLManager sqlManager) {
		super();
		this.sqlManager = sqlManager;
		// Panel
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(createCheckInPanel());
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(new JSeparator(JSeparator.HORIZONTAL));
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(createCheckOutPanel());
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(new JSeparator(JSeparator.HORIZONTAL));
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(Box.createRigidArea(new Dimension(10, 380)));
	}
	
	private JComponent createCheckInPanel() {
		// Components
		JLabel bookingIdLabel = new JLabel("Booking Id : ");
		JButton checkInButton = new JButton("Check-in");
		checkInButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				completeCheckIn();
			}
		});
		// Panel
		JPanel checkInPanel = new JPanel();
		checkInPanel.setLayout(new BoxLayout(checkInPanel, BoxLayout.LINE_AXIS));
		checkInPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		checkInPanel.add(bookingIdLabel);
		checkInPanel.add(Box.createRigidArea(new Dimension(17, 0)));
		checkInPanel.add(bookingIdField);
		checkInPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		checkInPanel.add(checkInButton);
		return checkInPanel;
	}
	
	private JComponent createCheckOutPanel() {
		// Components
		JLabel roomNumLabel = new JLabel("Room Num. : ");
		JButton checkOutButton = new JButton("Check-out");
		checkOutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				completeCheckOut();
			}
		});
		// Panel
		JPanel checkOutPanel = new JPanel();
		checkOutPanel.setLayout(new BoxLayout(checkOutPanel, BoxLayout.LINE_AXIS));
		checkOutPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		checkOutPanel.add(roomNumLabel);
		checkOutPanel.add(Box.createRigidArea(new Dimension(17, 0)));
		checkOutPanel.add(roomNumberField);
		checkOutPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		checkOutPanel.add(checkOutButton);
		return checkOutPanel;
	}
	
	private JPanel createFieldWithLabel(Component field, String label, int space) {
		JPanel tfPanel = new JPanel();
		tfPanel.setLayout(new BoxLayout(tfPanel, BoxLayout.LINE_AXIS));
		tfPanel.add(new JLabel(label));
		tfPanel.add(Box.createRigidArea(new Dimension(space, 0)));
		tfPanel.add(field);
		return tfPanel;
	}
	
	private void completeCheckIn() {
		String bookingid = bookingIdField.getText();
		String roomNum = sqlManager.completeCheckIn(bookingid);
		if(bookingid != null && !bookingid.trim().equals("")) {
			if(roomNum != null) {
				JOptionPane.showMessageDialog(this,"Room for booking " + bookingid + " is room " + roomNum,
					    "Check-in",JOptionPane.INFORMATION_MESSAGE);
				bookingIdField.setText(null);
			} else {
				JOptionPane.showMessageDialog(this,"Check-in cannot be completed!",
					    "Check-in Eroor",JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this,"Please give a valid booking id!",
				    "Check-in Eroor",JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	private void completeCheckOut() {
		String roomNum = roomNumberField.getText();
		if(roomNum != null && !roomNum.trim().equals("")) {
			CheckInDetails details = sqlManager.getCheckInDetails(roomNum);
			if (details != null) {
				boolean ok = sqlManager.completeCheckOut(details);
				if (ok) {
					String bill = getBillAsStr(details);
					JOptionPane.showMessageDialog(this,"Check-out completed!\n Bill : \n" + bill,
						    "Check-in",JOptionPane.INFORMATION_MESSAGE);
					roomNumberField.setText(null);
				} else {
					JOptionPane.showMessageDialog(this,"Cannot complete check-out",
						    "Check-out Eroor",JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(this,"Cannot complete check-out",
					    "Check-out Eroor",JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this,"Please give a valid room number!",
				    "Check-out Eroor",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private String getBillAsStr(CheckInDetails details) {
		StringBuilder buffer = new StringBuilder();
		Double sum = 0.0;
		List<Charge> list = details.getAdditionalCharges();
		for (Charge c : list) {
			buffer.append(c).append("\n");
			sum += c.getPrice();
		}
		long d1=details.getCheckOutTime().getTime();
		long d2=details.getCheckInTime().getTime();
		long days = Math.abs((d1-d2)/(1000*60*60*24));
		sum += days * details.getBookingDetails().getPriceWithDiscount();
		buffer.append("Total cost is : ").append(sum);
		return buffer.toString();
	}
}
