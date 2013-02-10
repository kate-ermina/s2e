package onlinehotel.model;

import java.text.DecimalFormat;

public class Room {

	private String roomNumber;
	private Integer numOfBeds;
	private Double price;
	private Double discount;
	private String descr;

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public Integer getNumOfBeds() {
		return numOfBeds;
	}

	public void setNumOfBeds(Integer numOfBeds) {
		this.numOfBeds = numOfBeds;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}
	
	
	public Double getPriceWithDiscount() {
		if(discount != null)
			return (price * ( 100 - discount)) / 100;
		else
			return price;
	}
	
	public String toString() {
		DecimalFormat df = new DecimalFormat("#.##");
		String s = this.descr + ", Price : " + df.format(this.getPriceWithDiscount())  + " \u20AC";
		return s;
	}
}
