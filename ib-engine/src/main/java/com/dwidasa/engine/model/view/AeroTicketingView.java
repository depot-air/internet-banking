package com.dwidasa.engine.model.view;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.dwidasa.engine.model.AeroCustomer;
import com.dwidasa.engine.model.AeroPassenger;
import com.dwidasa.engine.model.airline.PacBook;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 11:39
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AeroTicketingView extends PaymentView {
    private String departAirlineCode;
    private String departAirlineName;
    private String returnAirlineCode;
    private String returnAirlineName;
    private String departureAirportCode;
    private String departureAirportName;
    private String destinationAirportCode;
    private String destinationAirportName;
    private Date departDate;
    private Date returnDate;
    private Boolean isDepartOnly;
    private Integer totalAdult;
    private Integer totalChildren;
    private Integer totalInfant;
    private AeroFlightView departureFlight;
    private AeroFlightView returnFlight;
    private String sessionId;
    private Boolean isCheckingDepart;
    private BigDecimal ticketPrice;
    private BigDecimal ticketComission;
    private BigDecimal totalAgentComission;    
    private BigDecimal agentPrice;

    private BigDecimal assuranceComission;
    private BigDecimal agentMargin;

    private BigDecimal assurancePrice;
    private BigDecimal totalAgentPrice;
    private BigDecimal totalCustomerPrice;
    
    private List<AeroPassenger> passengers;
    private AeroCustomer aeroCustomer;
    private PacBook pacBook;    
    private String bookId;
    private String assuranceType;
    private String assurancePolis;
    
	public AeroTicketingView() {
		super();
		isDepartOnly = true;		
	}
	public String getDepartAirlineCode() {
		return departAirlineCode;
	}
	public void setDepartAirlineCode(String departAirlineCode) {
		this.departAirlineCode = departAirlineCode;
	}
	public String getDepartAirlineName() {
		return departAirlineName;
	}
	public void setDepartAirlineName(String departAirlineName) {
		this.departAirlineName = departAirlineName;
	}
	public String getReturnAirlineCode() {
		return returnAirlineCode;
	}
	public void setReturnAirlineCode(String returnAirlineCode) {
		this.returnAirlineCode = returnAirlineCode;
	}
	public String getReturnAirlineName() {
		return returnAirlineName;
	}
	public void setReturnAirlineName(String returnAirlineName) {
		this.returnAirlineName = returnAirlineName;
	}

	public String getDepartureAirportCode() {
		return departureAirportCode;
	}
	public void setDepartureAirportCode(String departureAirportCode) {
		this.departureAirportCode = departureAirportCode;
	}
	public String getDepartureAirportName() {
		return departureAirportName;
	}
	public void setDepartureAirportName(String departureAirportName) {
		this.departureAirportName = departureAirportName;
	}
	public String getDestinationAirportCode() {
		return destinationAirportCode;
	}
	public void setDestinationAirportCode(String destinationAirportCode) {
		this.destinationAirportCode = destinationAirportCode;
	}
	public String getDestinationAirportName() {
		return destinationAirportName;
	}
	public void setDestinationAirportName(String destinationAirportName) {
		this.destinationAirportName = destinationAirportName;
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
	public Boolean getIsDepartOnly() {
		return isDepartOnly;
	}
	public void setIsDepartOnly(Boolean isDepartOnly) {
		this.isDepartOnly = isDepartOnly;
	}
	public Integer getTotalAdult() {
		return totalAdult;
	}
	public void setTotalAdult(Integer totalAdult) {
		this.totalAdult = totalAdult;
	}
	public Integer getTotalChildren() {
		return totalChildren;
	}
	public void setTotalChildren(Integer totalChildren) {
		this.totalChildren = totalChildren;
	}
	public Integer getTotalInfant() {
		return totalInfant;
	}
	public void setTotalInfant(Integer totalInfant) {
		this.totalInfant = totalInfant;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public Boolean getIsCheckingDepart() {
		return isCheckingDepart;
	}
	public void setIsCheckingDepart(Boolean isCheckingDepart) {
		this.isCheckingDepart = isCheckingDepart;
	}
	public AeroFlightView getDepartureFlight() {
		return departureFlight;
	}
	public void setDepartureFlight(AeroFlightView departureFlight) {
		this.departureFlight = departureFlight;
	}
	public AeroFlightView getReturnFlight() {
		return returnFlight;
	}
	public void setReturnFlight(AeroFlightView returnFlight) {
		this.returnFlight = returnFlight;
	}
	
	public BigDecimal getTicketPrice() {
		return (ticketPrice == null) ? BigDecimal.ZERO : ticketPrice;
	}
	public void setTicketPrice(BigDecimal ticketPrice) {
		this.ticketPrice = ticketPrice;
	}
	
	public BigDecimal getTicketComission() {
		return (ticketComission == null) ? BigDecimal.ZERO : ticketComission;
	}
	public void setTicketComission(BigDecimal ticketComission) {
		this.ticketComission = ticketComission;
	}
	public BigDecimal getAgentPrice() {
		return (agentPrice == null) ? BigDecimal.ZERO : agentPrice;
	}
	public void setAgentPrice(BigDecimal agentPrice) {
		this.agentPrice = agentPrice;
	}
	public BigDecimal getAssuranceComission() {
		return (assuranceComission == null) ? BigDecimal.ZERO : assuranceComission;
	}
	public void setAssuranceComission(BigDecimal assuranceComission) {
		this.assuranceComission = assuranceComission;
	}
	public BigDecimal getAgentMargin() {
		return (agentMargin == null) ? BigDecimal.ZERO : agentMargin;
	}
	public void setAgentMargin(BigDecimal agentMargin) {
		this.agentMargin = agentMargin;
	}
	public BigDecimal getTotalAgentComission() {
		return (totalAgentComission == null) ? BigDecimal.ZERO : totalAgentComission;
	}
	public void setTotalAgentComission(BigDecimal totalAgentComission) {
		this.totalAgentComission = totalAgentComission;
	}
	public BigDecimal getAssurancePrice() {
		return (assurancePrice == null) ? BigDecimal.ZERO : assurancePrice;
	}
	public void setAssurancePrice(BigDecimal assurancePrice) {
		this.assurancePrice = assurancePrice;
	}
	public BigDecimal getTotalAgentPrice() {
		return (totalAgentPrice == null) ? BigDecimal.ZERO : totalAgentPrice;
	}
	public void setTotalAgentPrice(BigDecimal totalAgentPrice) {
		this.totalAgentPrice = totalAgentPrice;
	}
	public BigDecimal getTotalCustomerPrice() {
		return (totalCustomerPrice == null) ? BigDecimal.ZERO : totalCustomerPrice;
	}
	public void setTotalCustomerPrice(BigDecimal totalCustomerPrice) {
		this.totalCustomerPrice = totalCustomerPrice;
	}
	public List<AeroPassenger> getPassengers() {
		return passengers;
	}
	public void setPassengers(List<AeroPassenger> passengers) {
		this.passengers = passengers;
	}
	public AeroCustomer getAeroCustomer() {
		return aeroCustomer;
	}
	public void setAeroCustomer(AeroCustomer aeroCustomer) {
		this.aeroCustomer = aeroCustomer;
	}
	public String getBookId() {
		return bookId;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}	
	public String getAssuranceType() {
		return assuranceType;
	}
	public void setAssuranceType(String assuranceType) {
		this.assuranceType = assuranceType;
	}
	public String getAssurancePolis() {
		return assurancePolis;
	}
	public void setAssurancePolis(String assurancePolis) {
		this.assurancePolis = assurancePolis;
	}
	public PacBook getPacBook() {
		return pacBook;
	}
	public void setPacBook(PacBook pacBook) {
		this.pacBook = pacBook;
	}

    public static void main(String[] strings) {
        String str = "{\"withInsurance\":false,\"departFlight\":{\"total\":415125,\"eta\":\"11 01 2015 08:50:00\",\"airlineName\":\"Airasia\",\"via\":\"-\",\"selectedClassId\":\"DQZ1\",\"etd\":\"11 01 2015 06:00:00\",\"isConnectingFlight\":false,\"referenceNumber\":null,\"billerCode\":null,\"billerName\":null,\"amount\":null,\"terminalId\":\"MBS\",\"accountNumber\":null,\"transactionDateString\":\"11\\/01\\/2015 06:40 \",\"traceNumber\":null,\"description\":null,\"merchantType\":\"6017\",\"ticketPrice\":406900,\"departureAirportCode\":\"CGK\",\"arrivalAirportCode\":\"DPS\",\"responseCode\":null,\"airlineId\":\"1\",\"transactionDate\":\"11 01 2015 06:40:53\",\"fee\":null,\"childPassengerSummary\":null,\"toAccountType\":\"00\",\"providerName\":null,\"sessionId\":\"4PACx9aWRsToaX9FWv19T3XA6JyjnNqCB_0ZsMszFxA\",\"flightDate\":\"11 01 2015 06:00:00\",\"bit22\":null,\"aeroFlightClasses\":[],\"flightType\":\"direct\",\"isDepartureFlight\":true,\"cardData1\":null,\"cardData2\":null,\"bookInfo\":null,\"flightNumber\":\"QZ 7510\",\"adultPassengerSummary\":{\"total\":406900,\"tax\":32900,\"basic\":329000,\"updatedby\":null,\"pax\":1,\"passengerType\":\"Adult\",\"service\":40000,\"iwjr\":1},\"aeroConnectingFlight2\":null,\"aeroConnectingFlight\":null,\"customerId\":null,\"inputType\":null,\"comission\":8225,\"arrivalAirportFullName\":\"DENPASAR\",\"bit48\":null,\"currencyCode\":\"360\",\"etaConnecting\":\"11 01 2015 08:50:00\",\"productCode\":null,\"feeIndicator\":null,\"selectedClass\":{\"id\":null,\"price\":406900,\"updatedby\":null,\"created\":null,\"updated\":null,\"classId\":\"DQZ1\",\"value\":null,\"className\":\"Promo\",\"createdby\":null,\"classLabel\":null,\"avaliableSeat\":5},\"providerCode\":\"AERO\",\"customerReference\":null,\"referenceName\":null,\"transactionType\":\"9l\",\"infantPassengerSummary\":null,\"departureAirportFullName\":\"JAKARTA\",\"accountType\":\"10\",\"cardNumber\":\"6278790127040710\"}}";

    }
}
