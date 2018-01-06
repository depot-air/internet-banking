package com.dwidasa.engine.model.airline;

import com.dwidasa.engine.model.view.AeroFlightView;

public class AirSearchFlightDetail extends AirBasicRequest {
	AeroFlightView departFlight;
	AeroFlightView returnFlight;
	boolean withInsurance;
	
	public AeroFlightView getDepartFlight() {
		return departFlight;
	}
	public void setDepartFlight(AeroFlightView departFlight) {
		this.departFlight = departFlight;
	}
	public AeroFlightView getReturnFlight() {
		return returnFlight;
	}
	public void setReturnFlight(AeroFlightView returnFlight) {
		this.returnFlight = returnFlight;
	}
	public boolean isWithInsurance() {
		return withInsurance;
	}
	public void setWithInsurance(boolean withInsurance) {
		this.withInsurance = withInsurance;
	}
	
}
