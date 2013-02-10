package onlinehotel.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class CheckInDetails {

	private int id;
	private String roomNumber;
	private BookingDetails bookingDetails;
	private Customer customer;
	private Date checkInTime;
	private Date checkOutTime;
	private List<Charge> additionalCharges;

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public BookingDetails getBookingDetails() {
		return bookingDetails;
	}

	public void setBookingDetails(BookingDetails bookingDetails) {
		this.bookingDetails = bookingDetails;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Date getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(Date checkInTime) {
		this.checkInTime = checkInTime;
	}

	public Date getCheckOutTime() {
		return checkOutTime;
	}

	public void setCheckOutTime(Date checkOutTime) {
		this.checkOutTime = checkOutTime;
	}

	public List<Charge> getAdditionalCharges() {
		return additionalCharges;
	}

	public void setAdditionalCharges(List<Charge> additionalCharges) {
		this.additionalCharges = additionalCharges;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
