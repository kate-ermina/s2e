package onlinehotel.model.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import onlinehotel.model.BookingDetails;
import onlinehotel.model.Charge;
import onlinehotel.model.CheckInDetails;
import onlinehotel.model.ContactDetails;
import onlinehotel.model.Customer;
import onlinehotel.model.Room;

public class SQLManager {

	private Connection connection;

	public SQLManager() {
		connection = SQLUtilities.connect();
	}

	public void close() {
		SQLUtilities.disconnect(connection);
	}

	public List<Room> searchAvailableRooms(Integer bedsNum, Date checkIn, Date checkOut) {
		List<Room> rooms = getAllRooms(bedsNum);
		List<Room> availableRooms = new ArrayList<Room>();
		for (Room r : rooms) {
			List<BookingDetails> bookingDetails = getOrderedBookingDetailsForRoom(r.getRoomNumber());
			if (roomIsAvailable(checkIn, checkOut, bookingDetails)) {
				availableRooms.add(r);
			}
		}
		return availableRooms;
	}

	public List<Room> getAllRooms() {
		List<Room> rooms = new ArrayList<Room>();
		String query = "select ROOM_NUMBER, PRICE, DISCOUNT, DESCR, NUM_OF_BEDS from room";
		ResultSet resultSet = SQLUtilities.executeSQL(query, connection);
		if (resultSet != null) {
			try {
				while (resultSet.next()) {
					String roomNumber = resultSet.getString("ROOM_NUMBER");
					Double price = resultSet.getDouble("PRICE");
					Double discount = resultSet.getDouble("DISCOUNT");
					String descr = resultSet.getString("DESCR");
					Integer numOfBeds = resultSet.getInt("NUM_OF_BEDS");
					Room r = new Room();
					r.setDescr(descr);
					r.setPrice(price);
					r.setRoomNumber(roomNumber);
					r.setDiscount(discount);
					r.setNumOfBeds(numOfBeds);
					rooms.add(r);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rooms;
	}
	
	private List<Room> getAllRooms(Integer bedsNum) {
		List<Room> rooms = new ArrayList<Room>();
		String query = "select ROOM_NUMBER, PRICE, DISCOUNT, DESCR, NUM_OF_BEDS from room " 
				+ "where NUM_OF_BEDS = " + bedsNum;
		ResultSet resultSet = SQLUtilities.executeSQL(query, connection);
		if (resultSet != null) {
			try {
				while (resultSet.next()) {
					String roomNumber = resultSet.getString("ROOM_NUMBER");
					Double price = resultSet.getDouble("PRICE");
					Double discount = resultSet.getDouble("DISCOUNT");
					String descr = resultSet.getString("DESCR");
					Integer numOfBeds = resultSet.getInt("NUM_OF_BEDS");
					Room r = new Room();
					r.setDescr(descr);
					r.setPrice(price);
					r.setRoomNumber(roomNumber);
					r.setDiscount(discount);
					r.setNumOfBeds(numOfBeds);
					rooms.add(r);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rooms;
	}

	private List<BookingDetails> getOrderedBookingDetailsForRoom(String roomNumber) {
		List<BookingDetails> bookingDetails = new ArrayList<BookingDetails>();
		String query = "select BOOKING_ID, ROOM_NUMBER, CHECK_IN_DATE, " 
				+ "CHECK_OUT_DATE, PRICE_ON_BOOKING, DISCOUNT_ON_BOOKING "
				+ "from booking_details where ROOM_NUMBER = "
				+ roomNumber + " order by CHECK_IN_DATE";
		ResultSet resultSet = SQLUtilities.executeSQL(query, connection);
		if (resultSet != null) {
			try {
				while (resultSet.next()) {
					Integer id = resultSet.getInt("BOOKING_ID");
					String rm = resultSet.getString("ROOM_NUMBER");
					Double price = resultSet.getDouble("PRICE_ON_BOOKING");
					Double discount = resultSet.getDouble("DISCOUNT_ON_BOOKING");
					Date bookingCheckInDate = resultSet.getDate("CHECK_IN_DATE");
					Date bookingCheckOutDate = resultSet.getDate("CHECK_OUT_DATE");
					
					BookingDetails b = new BookingDetails();
					b.setBookingId(id);
					b.setRoomNumber(rm);
					b.setPriceOnBooking(price);
					b.setDiscountOnBooking(discount);
					b.setCheckInDate(bookingCheckInDate);
					b.setCheckOutDate(bookingCheckOutDate);
					bookingDetails.add(b);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return bookingDetails;
	}
	
	private boolean roomIsAvailable(Date checkIn, Date checkOut, List<BookingDetails> bookingDetails) {
		if(bookingDetails.isEmpty()) {
			return true;
		} else {
			BookingDetails currentBooking = null;
			BookingDetails previousBooking = null;
			for (BookingDetails details : bookingDetails) {
				previousBooking = currentBooking;
				currentBooking = details;
				if(previousBooking == null) {
					if ( currentBooking.getCheckInDate().after(checkOut) ) 
						return true;
				} else {
					if (currentBooking.getCheckInDate().after(checkOut) && previousBooking.getCheckOutDate().before(checkIn) )
						return true;
				}
			}
			// In case there is only one
			if ( currentBooking.getCheckOutDate().before(checkIn) ) 
				return true;
			return false;
		}
	}
	
	public int addBookingForCustomer(Customer c, Room r, String checkIn, String checkOut) {
		int bookingID = -1;
		try {
			// Save contact details
			int cdID = saveContactDetails(c.getContactDetails());
			// Save Customer
			int cusID = saveCustomer(c, cdID);
			// Save booking
			bookingID = saveBooking(r, checkIn, checkOut, cusID); 
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return bookingID;
	}
	
	private int saveContactDetails(ContactDetails cd) throws SQLException {
		String query = "insert into contact_details(PHONE_NUMBER, CELL_PHONE, ADDRESS, EMAIL) " 
				+ "VALUES('" + cd.getPhoneNumber()  + "',' " 
				+ cd.getCellNumber()  + "', '" + cd.getAddress()  
				+ "', '" + cd.getEmail()  + "')";
		return SQLUtilities.executeInsert(query, connection);
	}
	
	private int saveCustomer(Customer c, int cdID) throws SQLException {
		String query = "insert into customer(FIRST_NAME, LAST_NAME, CONTACT_DETAILS_ID, ID_CARD_NUMBER) " 
				+ "VALUES('" + c.getFirstName()  + "',' " 
				+ c.getLastName()  + "', '" + cdID  
				+ "', '" + c.getIdCardNumber() + "')";
		return SQLUtilities.executeInsert(query, connection);
	}
	
	private int saveBooking(Room r, String checkIn, String checkOut, int cusID) throws SQLException {
		String query = "insert into booking_details(ROOM_NUMBER, CHECK_IN_DATE, CHECK_OUT_DATE, CUSTOMER_ID, "
				+"PRICE_ON_BOOKING, DISCOUNT_ON_BOOKING) " 
				+ "VALUES('" + r.getRoomNumber()  + "',' " 
				+ checkIn  + "', '" + checkOut  
				+ "', '" + cusID + "', '" + r.getPrice() + "', '" + r.getDiscount() + "')";
		return SQLUtilities.executeInsert(query, connection);
	}
	
	public String completeCheckIn(String bookingId) {
		BookingDetails b = getBookingDetailsById(bookingId);
		if (b != null) {
			int id = addCheckInDetailsRecord(b);
			System.out.println("ID : " + id);
			if (id > 0) {
				return b.getRoomNumber();
			}
		}
		return null;
	}
	
	private int addCheckInDetailsRecord(BookingDetails bd) {
		String query = "insert into check_in_details(ROOM_NUMBER, CHECK_IN_TIME, CHECK_OUT_TIME, "
				+"BOOKING_DETAILS_ID, ACTIVE) " 
				+ "VALUES('" + bd.getRoomNumber()  + "',' " 
				+ bd.getCheckInDateStr()  + "', '" + bd.getCheckOutDateStr()  
				+ "', '" + bd.getBookingId() + "', 'YES')";
		System.out.println("Insert : " + query);
		return SQLUtilities.executeInsert(query, connection);
	}
	
	private BookingDetails getBookingDetailsById(String id) {
		BookingDetails bookingDetails = null;
		String query = "select BOOKING_ID, ROOM_NUMBER, CHECK_IN_DATE, " 
				+ "CHECK_OUT_DATE, PRICE_ON_BOOKING, DISCOUNT_ON_BOOKING "
				+ "from booking_details where BOOKING_ID = " + id;
		ResultSet resultSet = SQLUtilities.executeSQL(query, connection);
		if (resultSet != null) {
			try {
				if (resultSet.next()) {
					String rm = resultSet.getString("ROOM_NUMBER");
					Double price = resultSet.getDouble("PRICE_ON_BOOKING");
					Double discount = resultSet.getDouble("DISCOUNT_ON_BOOKING");
					Date bookingCheckInDate = resultSet.getDate("CHECK_IN_DATE");
					Date bookingCheckOutDate = resultSet.getDate("CHECK_OUT_DATE");
					
					bookingDetails = new BookingDetails();
					try {
						bookingDetails.setBookingId(Integer.parseInt(id));
					}catch(NumberFormatException e) {
						e.printStackTrace();
					}
					bookingDetails.setRoomNumber(rm);
					bookingDetails.setPriceOnBooking(price);
					bookingDetails.setDiscountOnBooking(discount);
					bookingDetails.setCheckInDate(bookingCheckInDate);
					bookingDetails.setCheckOutDate(bookingCheckOutDate);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return bookingDetails;
	}
	
	public CheckInDetails getCheckInDetails(String roomNumber) {
		CheckInDetails details = null;
		String query = "select ID, ROOM_NUMBER, CHECK_IN_TIME, CHECK_OUT_TIME, " 
				+ "BOOKING_DETAILS_ID "
				+ "from check_in_details where ACTIVE = 'YES' AND ROOM_NUMBER = " + roomNumber;
		ResultSet resultSet = SQLUtilities.executeSQL(query, connection);
		if (resultSet != null) {
			try {
				if (resultSet.next()) {
					String rm = resultSet.getString("ROOM_NUMBER");
					Date bookingCheckInDate = resultSet.getDate("CHECK_IN_TIME");
					Date bookingCheckOutDate = resultSet.getDate("CHECK_OUT_TIME");
					int bookingId = resultSet.getInt("BOOKING_DETAILS_ID");
					int id = resultSet.getInt("ID");
					
					details = new CheckInDetails();
					try {
						details.setBookingDetails(getBookingDetailsById(bookingId+""));
					}catch(NumberFormatException e) {
						e.printStackTrace();
					}
					details.setRoomNumber(rm);
					details.setId(id);
					details.setCheckInTime(bookingCheckInDate);
					details.setCheckOutTime(bookingCheckOutDate);
					details.setAdditionalCharges(getAdditionalChargesForCheckIn(id));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return details;
	} 
	
	private List<Charge> getAdditionalChargesForCheckIn(int checkInId) {
		List<Charge> list = new ArrayList<Charge>();
		String query = "select DESCR, PRICE from charge where CHECK_IN_DETAILS_ID = " + checkInId;
		ResultSet resultSet = SQLUtilities.executeSQL(query, connection);
		if (resultSet != null) {
			try {
				while (resultSet.next()) {
					String descr = resultSet.getString("DESCR");
					Double price = resultSet.getDouble("PRICE");
					
					Charge c = new Charge();
					c.setPrice(price);
					c.setDescr(descr);
					list.add(c);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public int addNewCharge(String roomNumber, String descr, Double price) {
		CheckInDetails details = getCheckInDetails(roomNumber);
		if(details != null) {
			DecimalFormat df = new DecimalFormat("#.##");
			String query = "insert into charge(DESCR, PRICE, CHECK_IN_DETAILS_ID) " 
					+ "VALUES('" + descr  + "',' " 
					+ df.format(price)  + "', '" + details.getId() + "')";
			return SQLUtilities.executeInsert(query, connection);
		} else {
			return -1;
		}
	}
	
	public boolean completeCheckOut(CheckInDetails details) {
		if(details != null) {
			String query = "update check_in_details set ACTIVE = 'NO' where ID = " + details.getId();
			return SQLUtilities.executeUpdate(query, connection);
		}
		return false;
	}
	
	public boolean updateRoomWithDiscount(Room room, Double discount) {
		if(room != null && discount != null) {
			DecimalFormat df = new DecimalFormat("#.##");
			String query = "update room set DISCOUNT = '" + df.format(discount) 
					+ "' where ROOM_NUMBER = " + room.getRoomNumber();
			return SQLUtilities.executeUpdate(query, connection);
		}
		return false;
	}
}
