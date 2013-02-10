package onlinehotel.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import onlinehotel.model.Room;
import onlinehotel.model.db.SQLManager;

public class OfferPanel extends JPanel {

	private SQLManager sqlManager;

	private JComboBox roomSelection = new JComboBox();
	private JTextField discountField = new JTextField();
	
	public OfferPanel(SQLManager sqlManager) {
		super();
		this.sqlManager = sqlManager;
		// Panel
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(createFieldWithLabel(roomSelection, "Room : ", 9));
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(createFieldWithLabel(discountField, "Discount (%) : ", 12));
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(createAddChargeButton());
		add(Box.createRigidArea(new Dimension(300, 200)));
		setUpComponents();
	}
	
	private void setUpComponents() {
		List<Room> rooms = sqlManager.getAllRooms();
		for (Room room : rooms) {
			roomSelection.addItem(room);
		}
	}
	
	private JPanel createFieldWithLabel(JComponent textField, String label, int space) {
		JPanel tfPanel = new JPanel();
		tfPanel.setLayout(new BoxLayout(tfPanel, BoxLayout.LINE_AXIS));
		tfPanel.add(new JLabel(label));
		tfPanel.add(Box.createRigidArea(new Dimension(space, 0)));
		tfPanel.add(textField);
		tfPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		return tfPanel;
	}
	
	private JButton createAddChargeButton() {
		JButton button = new JButton("Save Discount");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveDiscount();				
			}
		});
		return button;
	}
	
	private void saveDiscount() {
		Room room = (Room)roomSelection.getSelectedItem();
		String discountStr = discountField.getText();
		try {
			Double discount = Double.parseDouble(discountStr);
			boolean ok = sqlManager.updateRoomWithDiscount(room, discount);
			if (ok) {
				JOptionPane.showMessageDialog(this,"Room updated!",
						"Process Complete",JOptionPane.INFORMATION_MESSAGE);
				discountField.setText(null);
			} else {
				JOptionPane.showMessageDialog(this,"Cannot update room with discount!",
						"Error",JOptionPane.ERROR_MESSAGE);
			}
		}catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(this,"Please give a valid discount percent!",
					"Error",JOptionPane.ERROR_MESSAGE);
		}
	}
}
