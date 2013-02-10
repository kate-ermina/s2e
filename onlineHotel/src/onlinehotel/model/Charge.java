package onlinehotel.model;

import java.text.DecimalFormat;

public class Charge {

	private String descr;
	private Double price;

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String toString() {
		DecimalFormat df = new DecimalFormat("#.##");
		return descr + ", Price : " + df.format(price);
	}
}
