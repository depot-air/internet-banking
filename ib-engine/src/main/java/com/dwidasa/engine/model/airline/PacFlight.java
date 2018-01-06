package com.dwidasa.engine.model.airline;

import java.util.Date;

public class PacFlight {
	private String flightNo;
	private String airlineCode;
	private String fromAirport;
	private String toAirport;
	private Date departDate;
	private Date departTime;
	private Date arriveTime;

	public String getFlightNo() {
		return flightNo;
	}

	public void setFlightNo(String flightNo) {
		this.flightNo = flightNo;
	}

	public String getAirlineCode() {
		return airlineCode;
	}

	public void setAirlineCode(String airlineCode) {
		this.airlineCode = airlineCode;
	}

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

	public Date getDepartTime() {
		return departTime;
	}

	public void setDepartTime(Date departTime) {
		this.departTime = departTime;
	}

	public Date getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(Date arriveTime) {
		this.arriveTime = arriveTime;
	}

	public  String toString()
	{
	    return " [VoltrasFlight: " + flightNo + " " + 
	    		airlineCode + " " + 
	    		fromAirport + " " +
	    		toAirport + " " +
	    		departDate + " " +
	    		departTime + " " +
	    		arriveTime + "] ";
	}
}
