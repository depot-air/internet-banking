package com.dwidasa.engine.model.airline;

import java.util.List;

public class PacAirItem {
	private List<PacFlight> pacFlights;
	private List<PacFlightClassGroup> pacFlightClassGroups;
	private boolean boolDepart;
	private String sessionId;
	
	public List<PacFlight> getPacFlights() {
		return pacFlights;
	}

	public void setPacFlights(List<PacFlight> pacFlights) {
		this.pacFlights = pacFlights;
	}

	public List<PacFlightClassGroup> getPacFlightClassGroups() {
		return pacFlightClassGroups;
	}

	public void setPacFlightClassGroups(
			List<PacFlightClassGroup> pacFlightClassGroups) {
		this.pacFlightClassGroups = pacFlightClassGroups;
	}

	public boolean isBoolDepart() {
		return boolDepart;
	}

	public void setBoolDepart(boolean boolDepart) {
		this.boolDepart = boolDepart;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public  String toString()
	{
		String res = "boolDepart=" + boolDepart + " ";
		for (PacFlight pacFlight : pacFlights) {
			res += pacFlight.toString() + " - ";
		}
		for (PacFlightClassGroup pacClassGroup : pacFlightClassGroups) {
			res += pacClassGroup.toString() + " - ";
		}
	    return res;
	}
}
