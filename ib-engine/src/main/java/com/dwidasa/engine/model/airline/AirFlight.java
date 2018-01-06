package com.dwidasa.engine.model.airline;

import java.util.Date;

public class AirFlight extends AirBasicRequest {
	String flightNo;
	AirlineBasic airline;
	AirportBasic fromAirport;
	AirportBasic toAirport;
	Date departDatetime;
	Date arriveDatetime;
	String flightType;	//direct/connecting/traansit
	String via;
	AirFlight connectingFlight;
	Boolean isDepart;
	
	public String getFlightNo() {
		return flightNo;
	}
	public void setFlightNo(String flightNo) {
		this.flightNo = flightNo;
	}
	public AirlineBasic getAirline() {
		return airline;
	}
	public void setAirline(AirlineBasic airline) {
		this.airline = airline;
	}
	public AirportBasic getFromAirport() {
		return fromAirport;
	}
	public void setFromAirport(AirportBasic fromAirport) {
		this.fromAirport = fromAirport;
	}
	public AirportBasic getToAirport() {
		return toAirport;
	}
	public void setToAirport(AirportBasic toAirport) {
		this.toAirport = toAirport;
	}
	public Date getDepartDatetime() {
		return departDatetime;
	}
	public void setDepartDatetime(Date departDatetime) {
		this.departDatetime = departDatetime;
	}
	public Date getArriveDatetime() {
		return arriveDatetime;
	}
	public void setArriveDatetime(Date arriveDatetime) {
		this.arriveDatetime = arriveDatetime;
	}
	public String getFlightType() {
		return flightType;
	}
	public void setFlightType(String flightType) {
		this.flightType = flightType;
	}
	public String getVia() {
		return via;
	}
	public void setVia(String via) {
		this.via = via;
	}
	public AirFlight getConnectingFlight() {
		return connectingFlight;
	}
	public void setConnectingFlight(AirFlight connectingFlight) {
		this.connectingFlight = connectingFlight;
	}
	public Boolean getIsDepart() {
		return isDepart;
	}
	public void setIsDepart(Boolean isDepart) {
		this.isDepart = isDepart;
	}
	
}
