package com.dwidasa.engine.model.airline;

import java.util.Date;
import java.util.List;

public class AirSearchFlight extends AirBasicRequest {
	String fromAirport;
	String toAirport;
	Date departDate;
	Date returnDate;
	List<String> airlineCodes;
	int paxAdult;
	int paxChild;
	int paxInfant;
	boolean oneWay;
	public String getFromAirport() {
		return fromAirport;
	}
	public void setFromAirport(String fromAirport) {
		this.fromAirport = fromAirport;
	}
	public String getToAirport() {
		return toAirport;
	}
	public void setToAirport(String toAirport) {
		this.toAirport = toAirport;
	}
	public Date getDepartDate() {
		return departDate;
	}
	public void setDepartDate(Date departDate) {
		this.departDate = departDate;
	}
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	public List<String> getAirlineCodes() {
		return airlineCodes;
	}
	public void setAirlineCodes(List<String> airlineCodes) {
		this.airlineCodes = airlineCodes;
	}
	public int getPaxAdult() {
		return paxAdult;
	}
	public void setPaxAdult(int paxAdult) {
		this.paxAdult = paxAdult;
	}
	public int getPaxChild() {
		return paxChild;
	}
	public void setPaxChild(int paxChild) {
		this.paxChild = paxChild;
	}
	public int getPaxInfant() {
		return paxInfant;
	}
	public void setPaxInfant(int paxInfant) {
		this.paxInfant = paxInfant;
	}
	public boolean isOneWay() {
		return oneWay;
	}
	public void setOneWay(boolean oneWay) {
		this.oneWay = oneWay;
	}	
}
