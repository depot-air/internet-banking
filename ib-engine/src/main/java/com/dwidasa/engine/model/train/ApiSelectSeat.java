package com.dwidasa.engine.model.train;

import java.math.BigDecimal;
import java.util.List;

public class ApiSelectSeat {

	private String subclass;
	private String seatClass;
	private BigDecimal adultFare;
	private int available;
	
	public String getSubclass() {
		return subclass;
	}
	public void setSubclass(String subclass) {
		this.subclass = subclass;
	}
	public String getSeatClass() {
		return seatClass;
	}
	public void setSeatClass(String seatClass) {
		this.seatClass = seatClass;
	}
	public BigDecimal getAdultFare() {
		return adultFare;
	}
	public void setAdultFare(BigDecimal adultFare) {
		this.adultFare = adultFare;
	}
	public int getAvailable() {
		return available;
	}
	public void setAvailable(int available) {
		this.available = available;
	}
	
	public String getStrClass() {
		if ("E".equals(seatClass)) {
			return "Eksekutif";
		} else if ("B".equals(seatClass)) {
			return "Bisnis";
		} else if ("K".equals(seatClass)) {
			return "Ekonomi";
		}
		return "-";
	}
	
}
