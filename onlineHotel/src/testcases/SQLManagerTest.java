package testcases;

import java.util.Calendar;
import java.util.List;

import junit.framework.TestCase;
import onlinehotel.model.Charge;
import onlinehotel.model.CheckInDetails;
import onlinehotel.model.ContactDetails;
import onlinehotel.model.Customer;
import onlinehotel.model.Room;
import onlinehotel.model.db.SQLManager;

public class SQLManagerTest extends TestCase {

	private SQLManager manager;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		manager = new SQLManager();
	}
	
	public void testSearchAvailableRooms() {
		// Check-in date 31/01/2012
		Calendar checkInCal = Calendar.getInstance();
		checkInCal.set(2012, 0, 31);
		// Check-in date 10/02/2012
		Calendar checkOutCal = Calendar.getInstance();
		checkOutCal.set(2012, 1, 10);
		List<Room> rooms = manager.searchAvailableRooms(2, checkInCal.getTime(), checkOutCal.getTime());
		assertTrue(rooms.size() == 3);
		
	}
	
	public void testGetAllRooms(){
		List<Room> rooms = manager.getAllRooms();
		assertFalse(rooms.isEmpty());
	}

	public void testCheckInCheckOutProcess() {
		// Check-in date 31/01/2012
		Calendar checkInCal = Calendar.getInstance();
		checkInCal.set(2012, 0, 31);
		// Check-in date 10/02/2012
		Calendar checkOutCal = Calendar.getInstance();
		checkOutCal.set(2012, 1, 10);
		
		// 1 - Check Availability
		List<Room> rooms = manager.searchAvailableRooms(3, checkInCal.getTime(), checkOutCal.getTime());
		assertFalse(rooms.isEmpty());
		// 2 - Book the first room from the list
		Room room2Book = rooms.get(0);
		int bookingId = manager.addBookingForCustomer(getDummyCustomer(), room2Book, getDateAsString(1,31), getDateAsString(2,10));
		assertTrue(bookingId >= 0);
		// 3 - Complete Check-in
		String roomNumber = manager.completeCheckIn(""+bookingId);
		assertNotNull(roomNumber);
		assertTrue(roomNumber.equals(room2Book.getRoomNumber()));
		// 4 - add additional expenses
		int chargeId = manager.addNewCharge(roomNumber, "e1", 10.0);
		assertTrue(chargeId >= 0);
		chargeId = manager.addNewCharge(roomNumber, "e2", 20.0);
		assertTrue(chargeId >= 0);
		chargeId = manager.addNewCharge(roomNumber, "e3", 34.0);
		assertTrue(chargeId >= 0);
		// 5 - get check-in details
		CheckInDetails details = manager.getCheckInDetails(roomNumber);
		assertNotNull(details);
		assertFalse(details.getAdditionalCharges().isEmpty());
		assertTrue(details.getAdditionalCharges().size() == 3);
		// 6 - complete check-out
		assertTrue(manager.completeCheckOut(details));
	}
	
	private Customer getDummyCustomer() {
		ContactDetails contectDetails = new ContactDetails();
		contectDetails.setAddress("TestAddress");
		contectDetails.setCellNumber("0000000000");
		contectDetails.setEmail("test@yahoo.gr");
		contectDetails.setPhoneNumber("111111111");
		Customer customer = new Customer();
		customer.setFirstName("TestName");
		customer.setLastName("TestLastName");
		customer.setIdCardNumber("3456633");
		customer.setContactDetails(contectDetails);
		return customer;
		
	}
	
	private String getDateAsString(int month, int day) {
		return "2013-" + month + "-" + day;
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		manager.close();
	}
	
	
}
