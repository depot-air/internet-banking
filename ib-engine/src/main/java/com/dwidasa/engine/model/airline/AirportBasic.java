package com.dwidasa.engine.model.airline;

import java.util.Locale;

public class AirportBasic {
    private String airportCode;
    private String airportName;
    private String airportCity;
    private String airportCountry;
    private String airportFullname;
    
    public AirportBasic() {
    }

	public String getAirportCode() {
		return airportCode;
	}


	public void setAirportCode(String airportCode) {
		this.airportCode = airportCode;
	}


	public String getAirportName() {
		return airportName;
	}


	public void setAirportName(String airportName) {
		this.airportName = airportName;
	}

	public String getAirportCity() {
		return airportCity;
	}

	public void setAirportCity(String airportCity) {
		this.airportCity = airportCity;
	}

	public String getAirportCountry() {
		return airportCountry;
	}

	public void setAirportCountry(String airportCountry) {
		this.airportCountry = airportCountry;
	}

	public String getAirportFullname() {
		return airportFullname;
	}


	public void setAirportFullname(String airportFullname) {
		this.airportFullname = airportFullname;
	}

}