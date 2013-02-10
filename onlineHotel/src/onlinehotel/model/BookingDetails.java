package onlinehotel.model;

import java.util.Calendar;
import java.util.Date;


public class BookingDetails {

	private String roomNumber;
	private Double priceOnBooking;
	private Double discountOnBooking;
	private Customer customer;
	private Date checkInDate;
	private Date checkOutDate;
	private Integer bookingId;

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public Double getPriceOnBooking() {
		return priceOnBooking;
	}

	public void setPriceOnBooking(Double priceOnBooking) {
		this.priceOnBooking = priceOnBooking;
	}

	public Double getDiscountOnBooking() {
		return discountOnBooking;
	}

	public void setDiscountOnBooking(Double discountOnBooking) {
		this.discountOnBooking = discountOnBooking;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Date getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(Date checkInDate) {
		this.checkInDate = checkInDate;
	}

	public Date getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(Date checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public Integer getBookingId() {
		return bookingId;
	}

	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}
	
	public String getCheckInDateStr() {
		if (checkInDate != null) {
			return this.getStrFromDate(checkInDate);
		} 
		return "";
	}
	
	public String getCheckOutDateStr() {
		if (checkOutDate != null) {
			return this.getStrFromDate(checkOutDate);
		} 
		return "";
	}
	
	private String getStrFromDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = c.get(Calendar.MONTH) + 1;
		return "2013-" + month + "-" + c.get(Calendar.DAY_OF_MONTH);
	}
	
	public Double getPriceWithDiscount() {
		if(discountOnBooking != null)
			return (priceOnBooking * ( 100 - discountOnBooking)) / 100;
		else
			return priceOnBooking;
	}

}
