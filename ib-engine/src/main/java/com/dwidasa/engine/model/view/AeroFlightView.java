package com.dwidasa.engine.model.view;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.dwidasa.engine.model.AeroBookInfo;
import com.dwidasa.engine.model.AeroConnectingFlight;
import com.dwidasa.engine.model.AeroFlightClass;
import com.dwidasa.engine.model.AeroPassengerSummary;

@JsonIgnoreProperties(ignoreUnknown=true)
public class AeroFlightView extends PaymentView {
	public static final class DEPART_RETURN {
    	public static final String DEPART = "1";	//also for oneway flight only
    	public static final String RETURN = "2";	//also for return flight
    }
    private String airlineId;
    private String airlineName;
    private String flightType;	// 	Flight type consists of direct/transit/connecting
    
    private String departureAirportCode;
    private String departureAirportFullName;
    private String arrivalAirportCode;
    private String arrivalAirportFullName;
    private Date flightDate;
    
    private String flightNumber;
    private Date etd;	//estimated time departure
    private Date eta;	//estimated time arrival
    private Date etaConnecting;	//estimated time arrival untuk penerbangan connecting
    
    private String via;
    private Boolean isConnectingFlight;
    private AeroConnectingFlight aeroConnectingFlight;
    private AeroConnectingFlight aeroConnectingFlight2;
    
	private List<AeroFlightClass> aeroFlightClasses;

    private BigDecimal ticketPrice;
    private String selectedClassId;
    private BigDecimal comission;
    private AeroFlightClass selectedClass;
    private Boolean isDepartureFlight;	//departure or arrival, true = departure, false = arrival
    private String sessionId;
    
    private AeroPassengerSummary adultPassengerSummary;
    private AeroPassengerSummary childPassengerSummary;
    private AeroPassengerSummary infantPassengerSummary;
    
    private AeroBookInfo bookInfo;
    
	public String getAirlineId() {
		return airlineId;
	}

	public void setAirlineId(String airlineId) {
		this.airlineId = airlineId;
	}

	public String getAirlineName() {
		return airlineName;
	}

	public void setAirlineName(String airlineName) {
		this.airlineName = airlineName;
	}

	public String getFlightType() {
		return flightType;
	}

	public void setFlightType(String flightType) {
		this.flightType = flightType;
	}

	public String getDepartureAirportCode() {
		return departureAirportCode;
	}

	public void setDepartureAirportCode(String departureAirportCode) {
		this.departureAirportCode = departureAirportCode;
	}

	public String getArrivalAirportCode() {
		return arrivalAirportCode;
	}

	public void setArrivalAirportCode(String arrivalAirportCode) {
		this.arrivalAirportCode = arrivalAirportCode;
	}

	public String getDepartureAirportFullName() {
		return departureAirportFullName;
	}

	public void setDepartureAirportFullName(String departureAirportFullName) {
		this.departureAirportFullName = departureAirportFullName;
	}

	public String getArrivalAirportFullName() {
		return arrivalAirportFullName;
	}

	public void setArrivalAirportFullName(String arrivalAirportFullName) {
		this.arrivalAirportFullName = arrivalAirportFullName;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public Date getEtd() {
		return etd;
	}

	public void setEtd(Date etd) {
		this.etd = etd;
	}

	public Date getEta() {
		return eta;
	}

	public void setEta(Date eta) {
		this.eta = eta;
	}

	public Date getEtaConnecting() {
		return etaConnecting;
	}

	public void setEtaConnecting(Date etaConnecting) {
		this.etaConnecting = etaConnecting;
	}

	public String getVia() {
		return (via == null || via.equals("")) ? "-" : via;
	}

	public void setVia(String via) {
		this.via = via;
	}
	
	public Date getFlightDate() {
		return flightDate;
	}

	public void setFlightDate(Date flightDate) {
		this.flightDate = flightDate;
	}

	public Boolean getIsConnectingFlight() {
		return isConnectingFlight;
	}

	public void setIsConnectingFlight(Boolean isConnectingFlight) {
		this.isConnectingFlight = isConnectingFlight;
	}

	public AeroConnectingFlight getAeroConnectingFlight() {
		return aeroConnectingFlight;
	}

	public void setAeroConnectingFlight(AeroConnectingFlight aeroConnectingFlight) {
		this.aeroConnectingFlight = aeroConnectingFlight;
	}

    public AeroConnectingFlight getAeroConnectingFlight2() {
		return aeroConnectingFlight2;
	}

	public void setAeroConnectingFlight2(AeroConnectingFlight aeroConnectingFlight2) {
		this.aeroConnectingFlight2 = aeroConnectingFlight2;
	}

	public List<AeroFlightClass> getAeroFlightClasses() {
		return aeroFlightClasses;
	}

	public void setAeroFlightClasses(List<AeroFlightClass> aeroFlightClasses) {
		this.aeroFlightClasses = aeroFlightClasses;
	}

	public BigDecimal getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(BigDecimal ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public String getSelectedClassId() {
		return selectedClassId;
	}

	public void setSelectedClassId(String selectedClassId) {
		this.selectedClassId = selectedClassId;
	}

	public BigDecimal getComission() {
		return comission;
	}

	public void setComission(BigDecimal comission) {
		this.comission = comission;
	}

	public AeroFlightClass getSelectedClass() {
		return selectedClass;
	}

	public void setSelectedClass(AeroFlightClass selectedClass) {
		this.selectedClass = selectedClass;
	}

	public Boolean getIsDepartureFlight() {
		return isDepartureFlight;
	}

	public void setIsDepartureFlight(Boolean isDepartureFlight) {
		this.isDepartureFlight = isDepartureFlight;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public AeroPassengerSummary getAdultPassengerSummary() {
		return adultPassengerSummary;
	}

	public void setAdultPassengerSummary(AeroPassengerSummary adultPassengerSummary) {
		this.adultPassengerSummary = adultPassengerSummary;
	}

	public AeroPassengerSummary getChildPassengerSummary() {
		return childPassengerSummary;
	}

	public void setChildPassengerSummary(AeroPassengerSummary childPassengerSummary) {
		this.childPassengerSummary = childPassengerSummary;
	}

	public AeroPassengerSummary getInfantPassengerSummary() {
		return infantPassengerSummary;
	}

	public void setInfantPassengerSummary(
			AeroPassengerSummary infantPassengerSummary) {
		this.infantPassengerSummary = infantPassengerSummary;
	}

	public AeroBookInfo getBookInfo() {
		return bookInfo;
	}

	public void setBookInfo(AeroBookInfo bookInfo) {
		this.bookInfo = bookInfo;
	}

}
