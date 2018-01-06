package com.dwidasa.engine.model.airline;


public class AirlineBasic  {
    private String airlineId;
    private String airlineCode;
    private String airlineName;
    private String airlineType;
    //untuk airline connect
    private String airlineConnect;
    
    public AirlineBasic() {
    }

	public String getAirlineId() {
		return airlineId;
	}


	public void setAirlineId(String airlineId) {
		this.airlineId = airlineId;
	}


	public String getAirlineCode() {
		return airlineCode;
	}


	public void setAirlineCode(String airlineCode) {
		this.airlineCode = airlineCode;
	}


	public String getAirlineName() {
		return airlineName;
	}


	public void setAirlineName(String airlineName) {
		this.airlineName = airlineName;
	}


	public String getAirlineType() {
		return airlineType;
	}

	public void setAirlineType(String airlineType) {
		this.airlineType = airlineType;
	}

	public String getAirlineConnect() {
		return airlineConnect;
	}

	public void setAirlineConnect(String airlineConnect) {
		this.airlineConnect = airlineConnect;
	}

}