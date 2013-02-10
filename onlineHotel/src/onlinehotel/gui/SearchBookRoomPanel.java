package onlinehotel.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
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
import javax.swing.JSeparator;
import javax.swing.JTextField;

import onlinehotel.model.ContactDetails;
import onlinehotel.model.Customer;
import onlinehotel.model.Room;
import onlinehotel.model.db.SQLManager;

public class SearchBookRoomPanel extends JPanel {

	private static String[] MONTHS_ARRAY = {
			"-- Month --",
			"January","February","March",
			"April","May","June",
			"July","August","September",
			"October","November","December"
	};
	
	private static int[] MAX_DAYS_PER_MONTH = {
		31,//"-- Month --"
		31,//"January"
		29,//"February"
		31,//"March"
		30,//"April"
		31,//"May"
		30,//"June"
		31,//"July"
		31,//"August"
		30,//"September"
		31,//"October"
		30,//"November"
		31//"December"
	};
	
	// Check-in fields
	private JComboBox checkInDay = new JComboBox();
	private JComboBox checkInMonth = new JComboBox(MONTHS_ARRAY);;
	// Check-out fields
	
	private JComboBox checkOutDay = new JComboBox(new String[] {"3", "4"});
	private JComboBox checkOutMonth = new JComboBox(MONTHS_ARRAY);
	// Number of beds
	private JComboBox bedsNum = new JComboBox(new String[] {"------","2", "3", "4"});
	// Booking 
	private JComboBox roomsList = new JComboBox();
	private JButton bookButton = new JButton("Book");
	// Customer details
	private JTextField firstNameField = new JTextField(20);
	private JTextField lastNameField = new JTextField(20);
	private JTextField phoneNumberField = new JTextField(20);
	private JTextField cellNumberField = new JTextField(20);
	private JTextField idCardNumberField = new JTextField(20);
	private JTextField emailField = new JTextField(20);
	private JTextField addressField = new JTextField(20);
	
	private SQLManager sqlManager;
	
	public SearchBookRoomPanel(SQLManager sqlManager) {
		super();
		this.sqlManager = sqlManager;
		// Panel
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
		add(this.createCheckInPanel());
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(this.createCheckOutPanel());
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(this.createNumOfBedsPanel());
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(this.createSearchResetButtonsPanel());	
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(new JSeparator(JSeparator.HORIZONTAL));
		add(Box.createRigidArea(new Dimension(0, 10)));
		createBookRoomPanel();	
		// Initially all fields related to booking must be deactivated
		this.resetBookPanel();
	}
	
	private JComponent createCheckInPanel() {
		// Components
		JLabel checkInLabel = new JLabel("Check-in date : ");
		updateDaysComboBox(checkInDay, 0);
		checkInMonth.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				int monthSelection = ((JComboBox)event.getSource()).getSelectedIndex();
				updateDaysComboBox(checkInDay, monthSelection);
			}
		});
		// Panel
		JPanel checkInPanel = new JPanel();
		checkInPanel.setLayout(new BoxLayout(checkInPanel, BoxLayout.LINE_AXIS));
		checkInPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		checkInPanel.add(checkInLabel);
		checkInPanel.add(Box.createRigidArea(new Dimension(17, 0)));
		checkInPanel.add(checkInMonth);
		checkInPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		checkInPanel.add(checkInDay);
		return checkInPanel;
	}
	
	private void updateDaysComboBox(JComboBox combo, int monthSelection) {
		combo.removeAllItems();
		combo.addItem("-- Day --");
		int maxDays = MAX_DAYS_PER_MONTH[monthSelection];
		for(int i=1; i <= maxDays; i++) {
			combo.addItem(""+i);
		}
	}
	
	private JComponent createCheckOutPanel() {
		// Components
		JLabel checkOutLabel = new JLabel("Check-out date : ");
		updateDaysComboBox(checkOutDay, 0);
		checkOutMonth.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				int monthSelection = ((JComboBox)event.getSource()).getSelectedIndex();
				updateDaysComboBox(checkOutDay, monthSelection);
			}
		});
		//Panel
		JPanel checkOutPanel = new JPanel();
		checkOutPanel.setLayout(new BoxLayout(checkOutPanel, BoxLayout.LINE_AXIS));
		checkOutPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		checkOutPanel.add(checkOutLabel);
		checkOutPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		checkOutPanel.add(checkOutMonth);
		checkOutPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		checkOutPanel.add(checkOutDay);
		return checkOutPanel;
	}
	
	private JComponent createNumOfBedsPanel() {
		// Components
		JLabel bedsLabel = new JLabel("Num. of beds :   ");
		// Panel
		JPanel bedsPanel = new JPanel();
		bedsPanel.setLayout(new BoxLayout(bedsPanel, BoxLayout.LINE_AXIS));
		bedsPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		bedsPanel.add(bedsLabel);
		bedsPanel.add(Box.createRigidArea(new Dimension(14, 0)));
		bedsPanel.add(bedsNum);
		bedsPanel.add(Box.createRigidArea(new Dimension(200, 0)));
		return bedsPanel;
	}
	
	private JComponent createSearchResetButtonsPanel() {
		// Buttons
		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				searchDBForRooms();
			}
		});
		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkInMonth.setSelectedIndex(0);
				checkOutMonth.setSelectedIndex(0);
				bedsNum.setSelectedIndex(0);
				resetBookPanel();
			}
		});
		// Panel
		JPanel searchResetPanel = new JPanel();
		searchResetPanel.setLayout(new BoxLayout(searchResetPanel, BoxLayout.LINE_AXIS));
		searchResetPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		searchResetPanel.add(searchButton);
		searchResetPanel.add(Box.createRigidArea(new Dimension(14, 0)));
		searchResetPanel.add(resetButton);
		searchResetPanel.add(Box.createRigidArea(new Dimension(90, 0)));
		return searchResetPanel;
	}
	
	private void createBookRoomPanel() {
		// Components
		JLabel bookRoomLabel = new JLabel("Select a room : ");
		bookButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				roomBookingAction();
			}
		});
		// Room List
		JPanel roomListPanel = new JPanel();
		roomListPanel.setLayout(new BoxLayout(roomListPanel, BoxLayout.LINE_AXIS));
		roomListPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		roomListPanel.add(bookRoomLabel);
		roomListPanel.add(Box.createRigidArea(new Dimension(14, 0)));
		roomListPanel.add(roomsList);
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(roomListPanel);
		// firstname
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(createTextFieldWithLabel(firstNameField, "Firstname : ", 15));
		// lastname
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(createTextFieldWithLabel(lastNameField, "Lastname : ", 15));
		// phoneNumber
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(createTextFieldWithLabel(phoneNumberField, "Phone : ", 35));
		// cellNumber
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(createTextFieldWithLabel(cellNumberField, "Cell Phone : ", 10));
		// idCardNumber
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(createTextFieldWithLabel(idCardNumberField, "ID card : ", 32));
		// e-mail
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(createTextFieldWithLabel(emailField, "e-mail : ", 36));
		// address
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(createTextFieldWithLabel(addressField, "Address : ", 25));		
		// Book Button
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(bookButton);
	}
	
	private JPanel createTextFieldWithLabel(JTextField textField, String label, int space) {
		JPanel tfPanel = new JPanel();
		tfPanel.setLayout(new BoxLayout(tfPanel, BoxLayout.LINE_AXIS));
		tfPanel.add(new JLabel(label));
		tfPanel.add(Box.createRigidArea(new Dimension(space, 0)));
		tfPanel.add(textField);
		return tfPanel;
	}
	
	private void resetBookPanel() {
		roomsList.removeAllItems();
		roomsList.addItem("--------------------------------------------------");
		roomsList.setEnabled(false);
		bookButton.setEnabled(false);
		//
		firstNameField.setEnabled(false);
		lastNameField.setEnabled(false);
		phoneNumberField.setEnabled(false);
		cellNumberField.setEnabled(false);
		idCardNumberField.setEnabled(false);
		emailField.setEnabled(false);
		addressField.setEnabled(false);
		//
		firstNameField.setText(null);
		lastNameField.setText(null);
		phoneNumberField.setText(null);
		cellNumberField.setText(null);
		idCardNumberField.setText(null);
		emailField.setText(null);
		addressField.setText(null);
		//
		checkInDay.setEnabled(true);
		checkInMonth.setEnabled(true);
		checkOutDay.setEnabled(true);
		checkOutMonth.setEnabled(true);
	}
	
	private void searchDBForRooms() {
		// Check input
		int checkInDayValue = checkInDay.getSelectedIndex();
		int checkInMonthValue = checkInMonth.getSelectedIndex();
		int checkOutDayValue = checkOutDay.getSelectedIndex();
		int checkOutMonthValue = checkOutMonth.getSelectedIndex();
		int bedsNumValue = bedsNum.getSelectedIndex() + 1; 
		if (checkInDayValue == 0 || checkInMonthValue == 0 || 
				checkOutDayValue == 0 || checkOutMonthValue == 0 || bedsNumValue== 0 ) 
		{
			JOptionPane.showMessageDialog(this,"All fields must be filled!",
				    "Search Error",JOptionPane.ERROR_MESSAGE);
		} else if (
			(checkInMonthValue > checkOutMonthValue) || 
			(checkInMonthValue == checkOutMonthValue && checkInDayValue >= checkOutDayValue) ) 
		{
			JOptionPane.showMessageDialog(this,"Please correct check-in/check-out dates!",
				    "Search Error",JOptionPane.ERROR_MESSAGE);
		} else {
			// Search DB for available rooms and show results
			Date checkInDate = getDate(checkInMonthValue, checkInDayValue);
			Date checkOutDate = getDate(checkOutMonthValue, checkOutDayValue);
			List<Room> availableRooms = sqlManager.searchAvailableRooms(bedsNumValue, checkInDate, checkOutDate);
			if (availableRooms != null && availableRooms.size() > 0) {
				this.setAvailableRooms(availableRooms);
			} else {
				JOptionPane.showMessageDialog(this,"No results found within your search criteria",
					    "",JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
	private void setAvailableRooms(List<Room> rooms) {
		bookButton.setEnabled(true);
		roomsList.removeAllItems();
		roomsList.addItem("--------------------------------------------------");
		for (Room room : rooms) {
			roomsList.addItem(room);
		}
		roomsList.setEnabled(true);
		firstNameField.setEnabled(true);
		lastNameField.setEnabled(true);
		phoneNumberField.setEnabled(true);
		cellNumberField.setEnabled(true);
		idCardNumberField.setEnabled(true);
		emailField.setEnabled(true);
		addressField.setEnabled(true);
		//
		checkInDay.setEnabled(false);
		checkInMonth.setEnabled(false);
		checkOutDay.setEnabled(false);
		checkOutMonth.setEnabled(false);
	}
	
	private Date getDate(int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.set(2013, month - 1, day);
		return cal.getTime();
	}
	
	private void roomBookingAction() {
		if (roomsList.getSelectedIndex() == 0) {
			JOptionPane.showMessageDialog(this,"You must select a room!",
				    "Field Error",JOptionPane.ERROR_MESSAGE);
		} else {
			String address = addressField.getText();
			String email = emailField.getText();
			String phone = phoneNumberField.getText();
			String cellPhone = cellNumberField.getText();
			String firstName = firstNameField.getText();
			String lastName = lastNameField.getText();
			String idNum = idCardNumberField.getText();
			if( address != null && !address.trim().equals("") &&
					email != null && !email.trim().equals("") &&
					phone != null && !phone.trim().equals("") &&
					cellPhone != null && !cellPhone.trim().equals("") &&
					firstName != null && !firstName.trim().equals("") &&
					lastName != null && !lastName.trim().equals("") &&
					idNum != null && !idNum.trim().equals("")
			) {
				Room selectedRoom = (Room)roomsList.getSelectedItem();
				// Contact Details
				ContactDetails cd = new ContactDetails();
				cd.setAddress(address);
				cd.setCellNumber(cellPhone);
				cd.setEmail(email);
				cd.setPhoneNumber(phone);
				// Customer 
				Customer c = new Customer();
				c.setFirstName(firstNameField.getText());
				c.setLastName(lastNameField.getText());
				c.setIdCardNumber(idCardNumberField.getText());
				c.setContactDetails(cd);
				// Check-in, Check-out dates
				int checkInDayValue = checkInDay.getSelectedIndex();
				int checkInMonthValue = checkInMonth.getSelectedIndex();
				int checkOutDayValue = checkOutDay.getSelectedIndex();
				int checkOutMonthValue = checkOutMonth.getSelectedIndex();
				String checkInDate = getDateAsString(checkInMonthValue, checkInDayValue);
				String checkOutDate = getDateAsString(checkOutMonthValue, checkOutDayValue);
				int bookingId = sqlManager.addBookingForCustomer(c, selectedRoom, checkInDate, checkOutDate);
				if (bookingId >= 0) {
					JOptionPane.showMessageDialog(this,"Your booking id is " + bookingId,
					    "Booking Complete",JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(this,"Booking cannot be complete!",
						    "Booking Error",JOptionPane.ERROR_MESSAGE);
				}
				this.resetBookPanel();
			} else {
				JOptionPane.showMessageDialog(this,"Please fill in all fields!",
					    "Field Error",JOptionPane.ERROR_MESSAGE);
			}
		}	
	}
	
	private String getDateAsString(int month, int day) {
		return "2013-" + month + "-" + day;
	}
}
