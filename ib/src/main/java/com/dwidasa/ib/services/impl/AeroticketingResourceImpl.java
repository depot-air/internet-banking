package com.dwidasa.ib.services.impl;

import com.dwidasa.engine.AirConstants;
import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.ParameterDao;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.*;
import com.dwidasa.engine.model.airline.PacBook;
import com.dwidasa.engine.model.airline.PacResponseError;
import com.dwidasa.engine.model.view.AeroFlightView;
import com.dwidasa.engine.model.view.AeroTicketingView;
import com.dwidasa.engine.service.AirlineService;
import com.dwidasa.engine.service.AirportService;
import com.dwidasa.engine.service.CustomerRegisterService;
import com.dwidasa.engine.service.view.*;
import com.dwidasa.engine.util.ListUtils;
import com.dwidasa.ib.annotations.NoValidate;
import com.dwidasa.ib.annotations.PublicPage;
import com.dwidasa.ib.services.AeroticketingResource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.jpos.iso.ISOComponent;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/13/11
 * Time: 2:07 PM
 */
@PublicPage
public class AeroticketingResourceImpl implements AeroticketingResource {
	private static Logger logger = Logger.getLogger( AeroticketingResourceImpl.class );

    @Inject private AeroTicketingViewService aeroTicketingViewService;
    @Inject private AeroTicketingPriceDetailViewService priceDetailViewService;
    @Inject private AeroTicketingSearchDetailViewService aeroTicketingSearchDetailViewService;
    @Inject private AeroTicketingSearchBookingViewService aeroTicketingSearchBookingViewService;
    @Inject private AirlineService airlineService;
    @Inject private AirportService airportService;
    @Inject private CustomerRegisterService customerRegisterService;
    @Inject private ParameterDao parameterDao;

    Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();

    public AeroticketingResourceImpl() {
    }

    public String getAeroPriceDetail(String jsonView, String jsonFlight) {
        logger.info("jsonView=" + jsonView);
        logger.info("jsonFlight=" + jsonFlight);
//            AeroFlightView view = PojoJsonMapper.fromJson(json, AeroFlightView.class);
        AeroTicketingView ticketingView = gson.fromJson(jsonView, AeroTicketingView.class);
        AeroFlightView flightView = gson.fromJson(jsonFlight, AeroFlightView.class);

        if (flightView.getCardNumber() == null) {
            flightView.setCardNumber(ticketingView.getCardNumber());
        }
        if (flightView.getAccountType() == null) {
            flightView.setAccountType(ticketingView.getAccountType());
        }
        if (flightView.getToAccountType() == null) {
            flightView.setToAccountType(ticketingView.getToAccountType());
        }
        if (flightView.getTransactionDate() == null) {
            flightView.setTransactionDate(ticketingView.getTransactionDate());
        }
        if (flightView.getMerchantType() == null) {
            flightView.setMerchantType(ticketingView.getMerchantType());
        }
        if (flightView.getTerminalId() == null) {
            flightView.setTerminalId(ticketingView.getTerminalId());
        }
        if (flightView.getCurrencyCode() == null) {
            flightView.setCurrencyCode(ticketingView.getCurrencyCode());
        }

        AeroFlightView fromIgate;
        try {
            fromIgate = priceDetailViewService.getPriceDetail(flightView);
        }
        catch (BusinessException e) {
            e.printStackTrace();
            PacResponseError responseError = new PacResponseError();
            responseError.setCode(e.getErrorCode());
            responseError.setDescription(e.getFullMessage());
            String res = gson.toJson(responseError);
            logger.info("exception res=" + res);
            return res;
        }

        logger.info("fromIgate.getChildPassengerSummary()=" + fromIgate.getChildPassengerSummary());
        logger.info("fromIgate.getInfantPassengerSummary()=" + fromIgate.getInfantPassengerSummary());
        if (fromIgate.getChildPassengerSummary() == null) fromIgate.setChildPassengerSummary(flightView.getChildPassengerSummary());
        if (fromIgate.getInfantPassengerSummary() == null) fromIgate.setInfantPassengerSummary(flightView.getInfantPassengerSummary());
        return PojoJsonMapper.toJson(fromIgate);
    }

    public String aeroPriceDetail(Long customerId, String sessionId, String jsonView, String jsonFlight) {
        return getAeroPriceDetail(jsonView, jsonFlight);
    }

    public String voltrasAirFareDetail(Long customerId, String sessionId, String json, String jsonDepart, String jsonReturn) {
        return getVoltrasAirFare(json, jsonDepart, jsonReturn);
    }

    public String getVoltrasAirFare(String json, String jsonDepart, String jsonReturn) {
        logger.info("json=" + json);
        logger.info("jsonDepart=" + jsonDepart);
        AeroTicketingView view = PojoJsonMapper.fromJson(json, AeroTicketingView.class);
        AeroFlightView departFlight = PojoJsonMapper.fromJson(jsonDepart, AeroFlightView.class);
        //clear classes
        departFlight.setAeroFlightClasses(new ArrayList<AeroFlightClass>());
        view.setDepartureFlight(departFlight);
        if (!view.getIsDepartOnly()) {
            logger.info("jsonReturn=" + jsonReturn);
            AeroFlightView returnFlight = PojoJsonMapper.fromJson(jsonReturn, AeroFlightView.class);
            returnFlight.setAeroFlightClasses(new ArrayList<AeroFlightClass>());
            view.setReturnFlight(returnFlight);
        }

        view.setTransactionType(AirConstants.AEROTICKETING.TRANSACTION_TYPE.PRICE_DETAIL);
        view.setProviderCode(AirConstants.VOLTRAS.PROVIDER.CODE);
        logger.info("view=" + view);
        try {
            AeroTicketingView newView = priceDetailViewService.getVoltrasFare(view);
            logger.info("newView=" + newView);
            if (newView != null && newView.getTotal() != null) {
                view.setTotal(newView.getTotal());
            }
        } catch (BusinessException e) {
            e.printStackTrace();
            PacResponseError responseError = new PacResponseError();
            responseError.setCode(e.getErrorCode());
            responseError.setDescription(e.getFullMessage());
            String res = gson.toJson(responseError);
            logger.info("exception res=" + res);
            return res;
        }
//        String res = PojoJsonMapper.toJson(view);

        String res = gson.toJson(view);

        logger.info("res=" + res);
        return res;
    }

    @Override
    public String getOfficeInformation() {
        try {
            //AeroFlightView view = new AeroFlightView();
            String deposito = priceDetailViewService.getDepositInformation();
            logger.info("deposito=" + deposito);
            //view.setBit48(deposito);
            String res = PojoJsonMapper.toJson(deposito);
            logger.info("res=" + res);
            return res;
        } catch (Exception ex) {
            logger.info("ex.getMessage()=" + ex.getMessage() );
            return null;
        }
    }

    @Override
    public String search(Long customerId, String sessionId, String json) {
        try {
            logger.info("json=" + json);
            AeroTicketingView view = PojoJsonMapper.fromJson(json, AeroTicketingView.class);

            List<AeroFlightView> allFlights = aeroTicketingViewService.getSearch(view);
            String res = PojoJsonMapper.toJson(allFlights);
            logger.info("res=" + res);
            return res;
        } catch (BusinessException e) {
            e.printStackTrace();
            PacResponseError responseError = new PacResponseError();
            responseError.setCode(e.getErrorCode());
            responseError.setDescription(e.getFullMessage());
            String res = gson.toJson(responseError);
            logger.info("exception res=" + res);
            return res;
        }
    }

    @Override
    public String aeroSearchDetail(Long customerId, String sessionId, String jsonView,String jsonDepart, String jsonReturn) {
        try {
//            AeroTicketingView view = new AeroTicketingView();
        	AeroTicketingView view = gson.fromJson(jsonView, AeroTicketingView.class);
            view.setTransactionType(AirConstants.AEROTICKETING.TRANSACTION_TYPE.SEARCH_DETAIL);

            logger.info("jsonView="+jsonView);
            logger.info("jsonDepart=" + jsonDepart);
            AeroFlightView departFlight = gson.fromJson(jsonDepart, AeroFlightView.class);
            view.setDepartureFlight(departFlight);
            if (jsonReturn != null && !jsonReturn.equals("") && !jsonReturn.equals("{}")) {
                logger.info("jsonReturn=" + jsonReturn);
                AeroFlightView returnFlight = gson.fromJson(jsonReturn, AeroFlightView.class);
                view.setReturnFlight(returnFlight);
                view.setIsDepartOnly(false);
            } else {
                view.setIsDepartOnly(true);
            }
           
            view = aeroTicketingSearchDetailViewService.getSeachDetail(view);
            String res = PojoJsonMapper.toJson(view);
            logger.info("res=" + res);
            return res;

        } catch (BusinessException e) {
            e.printStackTrace();
            PacResponseError responseError = new PacResponseError();
            responseError.setCode(e.getErrorCode());
            responseError.setDescription(e.getFullMessage());
            String res = gson.toJson(responseError);
            logger.info("exception res=" + res);
            return res;
        }
    }

    @Override
    public String booking(Long customerId, String sessionId, String json) {
        try {
            logger.info("json=" + json);
            AeroTicketingView view = PojoJsonMapper.fromJson(json, AeroTicketingView.class);

            view = aeroTicketingSearchBookingViewService.getSearchBooking(view);
            String res = PojoJsonMapper.toJson(view);
            logger.info("res=" + res);
            return res;

        } catch (BusinessException e) {
            e.printStackTrace();
            PacResponseError responseError = new PacResponseError();
            responseError.setCode(e.getErrorCode());
            responseError.setDescription(e.getFullMessage());
            String res = gson.toJson(responseError);
            logger.info("exception res=" + res);
            return res;
        }
    }

    @Override
    public String voltrasRetrieve(Long customerId, String sessionId, String json) {
        try {
            logger.info("json=" + json);
            AeroTicketingView view = PojoJsonMapper.fromJson(json, AeroTicketingView.class);
            view.setTransactionType(AirConstants.VOLTRAS.TRANSACTION_TYPE.RETRIEVE);
            view = aeroTicketingSearchBookingViewService.getVoltrasRetrieve(view);
            PacBook pacBook = view.getPacBook();
            if (pacBook.getPnrIdReturn() != null && !pacBook.getPnrIdReturn().equals(pacBook.getPnrIdDepart()) ) {
                view = aeroTicketingSearchBookingViewService.getVoltrasRetrieve(view);
            }
            String res = PojoJsonMapper.toJson(view);
            logger.info("res=" + res);
            return res;

        } catch (BusinessException e) {
            e.printStackTrace();
            PacResponseError responseError = new PacResponseError();
            responseError.setCode(e.getErrorCode());
            responseError.setDescription(e.getFullMessage());
            String res = gson.toJson(responseError);
            logger.info("exception res=" + res);
            return res;
        }
    }

    @Override
    public String voltrasBooking(Long customerId, String sessionId, String json) {
        try {
            logger.info("1. json=" + json);
            json = booking(customerId, sessionId, json);
            logger.info("2. setelah booking json=" + json);
            AeroTicketingView aeroTicketingView = gson.fromJson(json, AeroTicketingView.class);

            json = voltrasRetrieve(customerId, sessionId, json);
            logger.info("3. setelah retrieve json=" + json);
            PacBook pacBook = aeroTicketingView.getPacBook();
            if (!aeroTicketingView.getIsDepartOnly() && pacBook.getPnrIdReturn() != null && !pacBook.getPnrIdReturn().equals(pacBook.getPnrIdDepart()) ) {
                json = voltrasRetrieve(customerId, sessionId, json);
                logger.info("4. setelah retrieve return json=" + json);
            }

        } catch (BusinessException e) {
            e.printStackTrace();
            PacResponseError responseError = new PacResponseError();
            responseError.setCode(e.getErrorCode());
            responseError.setDescription(e.getFullMessage());
            String res = gson.toJson(responseError);
            logger.info("exception res=" + res);
            return res;
        }
        return json;
    }

    @Override
    public String voltrasTicket(Long customerId, String sessionId, String json) {

        try {
            return voltrasCancel(customerId, sessionId, json);
            /*logger.info("json=" + json);
            AeroTicketingView view = PojoJsonMapper.fromJson(json, AeroTicketingView.class);

            view = aeroTicketingSearchBookingViewService.getVoltrasTicketing(view);
            PacBook pacBook = view.getPacBook();
            if (pacBook.getPnrIdReturn() != null && !pacBook.getPnrIdReturn().equals(pacBook.getPnrIdDepart()) ) {
                view = aeroTicketingSearchBookingViewService.getVoltrasTicketing(view);
            }
            String res = PojoJsonMapper.toJson(view);
            logger.info("res=" + res);
            return res;*/
        } catch (BusinessException e) {
             e.printStackTrace();
             PacResponseError responseError = new PacResponseError();
             responseError.setCode(e.getErrorCode());
             responseError.setDescription(e.getFullMessage());
             String res = gson.toJson(responseError);
             logger.info("exception res=" + res);
             return res;
         }
    }

    @Override
    public String voltrasCancel(Long customerId, String sessionId, String json) {
        try {
            logger.info("json=" + json);
            AeroTicketingView view = PojoJsonMapper.fromJson(json, AeroTicketingView.class);
            view.setTransactionType(AirConstants.VOLTRAS.TRANSACTION_TYPE.CANCEL);

            view = aeroTicketingSearchBookingViewService.getVoltrasCancel(view);
            PacBook pacBook = view.getPacBook();
            if (pacBook.getPnrIdReturn() != null && !pacBook.getPnrIdReturn().equals(pacBook.getPnrIdDepart()) ) {
                view = aeroTicketingSearchBookingViewService.getVoltrasCancel(view);
            }
            String res = PojoJsonMapper.toJson(view);
            logger.info("res=" + res);
            return res;
        } catch (BusinessException e) {
            e.printStackTrace();
            PacResponseError responseError = new PacResponseError();
            responseError.setCode(e.getErrorCode());
            responseError.setDescription(e.getFullMessage());
            String res = gson.toJson(responseError);
            logger.info("exception res=" + res);
            return res;
        }
    }

    @Override
    public String aeroIssue(Long customerId, String sessionId, String json) {
        try {
            logger.info("json=" + json);
            AeroTicketingView view = PojoJsonMapper.fromJson(json, AeroTicketingView.class);

            view = aeroTicketingSearchBookingViewService.getAeroIssue(view);
            String res = PojoJsonMapper.toJson(view);
            logger.info("res=" + res);
            return res;
        } catch (BusinessException e) {
            e.printStackTrace();
            PacResponseError responseError = new PacResponseError();
            responseError.setCode(e.getErrorCode());
            responseError.setDescription(e.getFullMessage());
            String res = gson.toJson(responseError);
            logger.info("exception res=" + res);
            return res;
        }
    }

    @Override
    public String sendEmail(Long customerId, String sessionId, String json) {
        try {
            logger.info("json=" + json);
            AeroTicketingView view = PojoJsonMapper.fromJson(json, AeroTicketingView.class);

            String res = aeroTicketingSearchBookingViewService.sendEmail(view);
            logger.info("res=" + res);
            return res;
        } catch (BusinessException e) {
            e.printStackTrace();
            PacResponseError responseError = new PacResponseError();
            responseError.setCode(e.getErrorCode());
            responseError.setDescription(e.getFullMessage());
            String res = gson.toJson(responseError);
            logger.info("exception res=" + res);
            return res;
        }
    }

    @Override
    public String getAirports() {
        List<Airport> airports = airportService.getAll();
        String res = PojoJsonMapper.toJson(airports);
        return res;
    }

    @Override
    public String getAirlines() {
        List<Airline> airlines = airlineService.getAll();
        String res = PojoJsonMapper.toJson(airlines);
        return res;
    }

    @Override
    public String getAirportsPost() {
        return getAirports();
    }

    @Override
    public String getOftenAirportsPost(Long customerId, String fromOrTo) {
        List<Airport> oftenAirports = airportService.getAirports(customerId,
                AirConstants.AEROTICKETING.TRANSACTION_TYPE.POSTING_SEARCH, AirConstants.AEROTICKETING.PROVIDER.CODE, fromOrTo);
        String res = PojoJsonMapper.toJson(oftenAirports);
        return res;
    }

    @Override
    public String getOftenRoutesPost(Long customerId) {
        List<CustomerRegister> crs = customerRegisterService.getAirportsFromTo("" + customerId,
                AirConstants.AEROTICKETING.TRANSACTION_TYPE.POSTING_SEARCH, AirConstants.AEROTICKETING.PROVIDER.CODE, "FROM");
        String res = PojoJsonMapper.toJson(crs);
        return res;
    }

    @Override
    public String getFlightListPost(String  json) {
        System.out.println("json=" + json);
        if (json.substring(0, 5).equals("json=")) {
           json = json.substring(5);
        }
        System.out.println("after json=" + json);
        logger.info("after json=" + json);
        AeroTicketingView aeroTicketingView = gson.fromJson(json, AeroTicketingView.class);
        List<AeroFlightView> allFlights = new ArrayList<AeroFlightView>();
        String res;
        try {
            allFlights = aeroTicketingViewService.getSearch(aeroTicketingView);
        }
        catch (BusinessException e) {
            e.printStackTrace();
            PacResponseError responseError = new PacResponseError();
            responseError.setCode(e.getErrorCode());
            responseError.setDescription(e.getFullMessage());
            res = gson.toJson(responseError);
            logger.info("exception res=" + res);
            return res;
        }
        res = gson.toJson(allFlights);
        return res;
    }

    @Override
    public String getOftenAirports(Long customerId, String fromOrTo) {
        return getOftenAirportsPost(customerId, fromOrTo);
    }

    @Override
    public String getOftenRoutes(Long customerId) {
        return getOftenRoutesPost(customerId);
    }

    @Override
    public String getFlightList(String  json) {
        return getFlightListPost(json);
    }

    @Override
    public String getComission() {
        //set margin / komisi di awal, sehingga harga yg muncul di awal sudah termasuk margin / komisi
        Parameter pcomission = parameterDao.get(Constants.MPARAMETER_NAME.KOMISI_TIKET_PESAWAT);
        BigDecimal commission = BigDecimal.ZERO;
        try {
            commission = BigDecimal.valueOf(Double.parseDouble(pcomission.getParameterValue()));
        } catch (Exception ex) {
            //do nothing
        }
        return commission.toPlainString();
    }

    @Override
    public String getCountries() {
        List<String> countries = ListUtils.getCountries();
        return gson.toJson(countries);
    }

    @Override
    public String getRecentContacts(Long customerId, String sessionId) {
        List<CustomerRegister> crsContacts = new ArrayList<CustomerRegister>();
        List<CustomerRegister> customerRegisters = customerRegisterService.getAeroPassangers(customerId,
                AirConstants.AEROTICKETING.TRANSACTION_TYPE.ISSUE, "Contact");
        if (customerRegisters.size() > 10) { customerRegisters.remove(customerRegisters.size() - 1); }
        for (CustomerRegister cr : customerRegisters) { crsContacts.add(cr); }

        return gson.toJson(crsContacts);
    }

    @Override
    public String getRecentPassengers(Long customerId, String sessionId, String passengerType) {
        List<CustomerRegister> crsPassengers = new ArrayList<CustomerRegister>();
        List<CustomerRegister> customerRegisters = customerRegisterService.getAeroPassangers(customerId,
            AirConstants.AEROTICKETING.TRANSACTION_TYPE.ISSUE, passengerType);
        if (customerRegisters.size() > 10) { customerRegisters.remove(customerRegisters.size() - 1); }
        for (CustomerRegister cr : customerRegisters) { crsPassengers.add(cr); }

        return gson.toJson(crsPassengers);
    }

    public static  void main(String[] args) {
//        String json = "{\"isCheckingDepart\":null,\"departureFlight\":{\"airlineName\":\"Garuda\",\"airlineId\":\"GA\",\"sessionId\":null,\"departureAirportCode\":\"SUB\",\"ticketPrice\":1480300.0,\"arrivalAirportCode\":\"BPN\",\"departureAirportFullName\":null,\"arrivalAirportFullName\":null,\"flightNumber\":\"GA 303\",\"isConnectingFlight\":true,\"aeroConnectingFlight\":{\"connectingFlightFrom\":\"CGK\",\"connectingFlightDate\":\"17 04 2014 08:50:00\",\"connectingFlightFno\":\"GA 564\",\"connectingFlightEtd\":\"17 04 2014 08:50:00\",\"connectingFlightEta\":\"17 04 2014 12:05:00\",\"connectingFlightClassname\":\"K\",\"connectingFlightPrice\":2066800.0,\"connectingFlightSeat\":9,\"connectingFlightTo\":\"BPN\",\"throughFare\":false,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"aeroConnectingFlight2\":null,\"aeroFlightClasses\":[{\"value\":null,\"className\":\"Y\",\"price\":1480300.0,\"avaliableSeat\":9,\"classId\":\"Y\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"D\",\"price\":3349200.0,\"avaliableSeat\":2,\"classId\":\"D\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"C\",\"price\":3516400.0,\"avaliableSeat\":9,\"classId\":\"C\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"J\",\"price\":3853000.0,\"avaliableSeat\":9,\"classId\":\"J\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null}],\"selectedClassId\":\"Y\",\"selectedClass\":{\"value\":null,\"className\":\"Y\",\"price\":1480300.0,\"avaliableSeat\":9,\"classId\":\"Y\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"isDepartureFlight\":true,\"adultPassengerSummary\":{\"value\":\"Adult 1\",\"total\":1480300.0,\"service\":null,\"passengerType\":\"Adult\",\"pax\":1,\"basic\":1480300.0,\"tax\":null,\"iwjr\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"childPassengerSummary\":{\"value\":\"Child 0\",\"total\":1480300.0,\"service\":null,\"passengerType\":\"Child\",\"pax\":0,\"basic\":1480300.0,\"tax\":null,\"iwjr\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"infantPassengerSummary\":{\"value\":\"Infant 0\",\"total\":1480300.0,\"service\":null,\"passengerType\":\"Infant\",\"pax\":0,\"basic\":1480300.0,\"tax\":null,\"iwjr\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"flightType\":\"connecting\",\"etd\":\"17 04 2014 05:30:00\",\"eta\":\"17 04 2014 07:05:00\",\"via\":\"-\"," +
//                "\"flightDate\":\"17 04 2014 05:30:00\",\"comission\":null,\"bookInfo\":null,\"responseCode\":null,\"total\":null,\"currencyCode\":null,\"description\":null,\"referenceNumber\":null,\"referenceName\":null,\"toAccountType\":null,\"providerName\":null,\"transactionDateString\":\"20/05/2014 11:34 \",\"traceNumber\":null,\"customerId\":null,\"billerCode\":null,\"cardNumber\":\"6278790011900001\",\"terminalId\":\"VOLT\",\"amount\":null,\"fee\":null,\"cardData2\":null,\"billerName\":null,\"bit48\":null,\"cardData1\":null,\"bit22\":null,\"customerReference\":null,\"transactionType\":\"9m\",\"accountNumber\":\"1574087868\",\"productCode\":null,\"providerCode\":\"VOLT\",\"accountType\":\"10\",\"merchantType\":\"6014\",\"transactionDate\":\"20 05 2014 11:34:12\",\"productName\":null,\"feeIndicator\":null,\"save\":null,\"inputType\":null},\"departAirlineName\":null,\"returnFlight\":{\"airlineName\":\"Garuda\",\"airlineId\":\"GA\",\"sessionId\":null,\"departureAirportCode\":\"BPN\",\"ticketPrice\":1577100.0,\"arrivalAirportCode\":\"SUB\",\"departureAirportFullName\":null,\"arrivalAirportFullName\":null,\"flightNumber\":\"GA 561\",\"isConnectingFlight\":true,\"aeroConnectingFlight\":{\"connectingFlightFrom\":\"CGK\",\"connectingFlightDate\":\"21 04 2014 08:50:00\",\"connectingFlightFno\":\"GA 308\",\"connectingFlightEtd\":\"21 04 2014 08:50:00\",\"connectingFlightEta\":\"21 04 2014 10:25:00\",\"connectingFlightClassname\":\"N\",\"connectingFlightPrice\":1363900.0,\"connectingFlightSeat\":9,\"connectingFlightTo\":\"SUB\",\"throughFare\":false,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"aeroConnectingFlight2\":null,\"aeroFlightClasses\":[{\"value\":null,\"className\":\"T\",\"price\":1577100.0,\"avaliableSeat\":9,\"classId\":\"T\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"Q\",\"price\":1847700.0,\"avaliableSeat\":9,\"classId\":\"Q\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"N\",\"price\":2011600.0,\"avaliableSeat\":9,\"classId\":\"N\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"K\",\"price\":2101800.0,\"avaliableSeat\":9,\"classId\":\"K\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"M\",\"price\":2111700.0,\"avaliableSeat\":9,\"classId\":\"M\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"B\",\"price\":2151300.0,\"avaliableSeat\":9,\"classId\":\"B\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"Y\",\"price\":2181000.0,\"avaliableSeat\":9,\"classId\":\"Y\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"D\",\"price\":5330300.0,\"avaliableSeat\":2,\"classId\":\"D\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"C\",\"price\":5599800.0,\"avaliableSeat\":9,\"classId\":\"C\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"J\",\"price\":6139900.0,\"avaliableSeat\":9,\"classId\":\"J\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null}],\"selectedClassId\":\"T\",\"selectedClass\":{\"value\":null,\"className\":\"T\",\"price\":1577100.0,\"avaliableSeat\":9,\"classId\":\"T\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"isDepartureFlight\":false,\"adultPassengerSummary\":{\"value\":\"Adult 1\",\"total\":1577100.0,\"service\":null,\"passengerType\":\"Adult\",\"pax\":1,\"basic\":1577100.0,\"tax\":null,\"iwjr\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"childPassengerSummary\":{\"value\":\"Child 0\",\"total\":1577100.0,\"service\":null,\"passengerType\":\"Child\",\"pax\":0,\"basic\":1577100.0,\"tax\":null,\"iwjr\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"infantPassengerSummary\":{\"value\":\"Infant 0\",\"total\":1577100.0,\"service\":null,\"passengerType\":\"Infant\",\"pax\":0,\"basic\":1577100.0,\"tax\":null,\"iwjr\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"flightType\":\"connecting\",\"etd\":\"21 04 2014 06:30:00\",\"eta\":\"21 04 2014 07:35:00\",\"via\":\"-\"," +
//                "\"flightDate\":\"21 04 2014 06:30:00\",\"comission\":null,\"bookInfo\":null,\"responseCode\":null,\"total\":null,\"currencyCode\":null,\"description\":null,\"referenceNumber\":null,\"referenceName\":null,\"toAccountType\":null,\"providerName\":null,\"transactionDateString\":\"20/05/2014 11:34 \",\"traceNumber\":null,\"customerId\":null,\"billerCode\":null,\"cardNumber\":\"6278790011900001\",\"terminalId\":\"VOLT\",\"amount\":null,\"fee\":null,\"cardData2\":null,\"billerName\":null,\"bit48\":null,\"cardData1\":null,\"bit22\":null,\"customerReference\":null,\"transactionType\":\"9m\",\"accountNumber\":\"1574087868\",\"productCode\":null,\"providerCode\":\"VOLT\",\"accountType\":\"10\",\"merchantType\":\"6014\",\"transactionDate\":\"20 05 2014 11:34:12\",\"productName\":null,\"feeIndicator\":null,\"save\":null,\"inputType\":null},\"returnAirlineName\":null,\"sessionId\":null,\"departAirlineCode\":\"GA\",\"returnAirlineCode\":\"GA\",\"departureAirportCode\":\"CGK\",\"departureAirportName\":\"JAKARTA\",\"destinationAirportCode\":\"SUB\",\"destinationAirportName\":\"SURABAYA\",\"isDepartOnly\":false,\"totalChildren\":0,\"totalInfant\":0,\"ticketPrice\":3057400.0,\"ticketComission\":0,\"assuranceComission\":0,\"agentMargin\":0,\"totalAgentComission\":0,\"assurancePrice\":0,\"totalAgentPrice\":0,\"totalCustomerPrice\":0,\"aeroCustomer\":null,\"departDate\":\"30 05 2014 00:00:00\",\"returnDate\":\"31 05 2014 00:00:00\",\"totalAdult\":1,\"agentPrice\":3057400.0,\"passengers\":[{\"value\":\"null null\",\"parent\":null,\"country\":\"Indonesia\",\"passengerType\":\"Adult\",\"passengerTitle\":null,\"passengerFirstName\":null,\"passengerLastName\":null,\"passengerIdCard\":null,\"passengerDob\":\"20 05 2014 11:34:12\",\"expirePaspor\":null,\"countryPaspor\":null,\"paspor\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null}],\"bookId\":null,\"pacBook\":null,\"responseCode\":null,\"total\":2407000.0,\"currencyCode\":\"360\",\"description\":null,\"referenceNumber\":null,\"referenceName\":null,\"toAccountType\":\"00\",\"providerName\":null,\"transactionDateString\":\"20/05/2014 11:34 \",\"traceNumber\":null,\"customerId\":102114,\"billerCode\":null,\"cardNumber\":\"6278790011900001\",\"terminalId\":\"I1516288\",\"amount\":null,\"fee\":null,\"cardData2\":null,\"billerName\":null,\"bit48\":null,\"cardData1\":null,\"bit22\":null,\"customerReference\":null,\"transactionType\":\"9m\",\"accountNumber\":\"1574087868\",\"productCode\":null,\"providerCode\":\"VOLT\",\"accountType\":\"10\",\"merchantType\":\"6014\",\"transactionDate\":\"20 05 2014 11:34:12\",\"productName\":null,\"feeIndicator\":null,\"save\":false,\"inputType\":null}";
//        AeroTicketingView view = PojoJsonMapper.fromJson(json, AeroTicketingView.class);
//        System.out.println("view.getDepartureFlight().getFlightDate()=" + view.getDepartureFlight().getFlightDate());
//
//        String jsonDepart = "{\"airlineName\":\"Garuda\",\"airlineId\":\"GA\",\"sessionId\":null,\"departureAirportCode\":\"SUB\",\"ticketPrice\":1480300,\"arrivalAirportCode\":\"BPN\",\"departureAirportFullName\":null,\"arrivalAirportFullName\":null,\"flightNumber\":\"GA 305\",\"isConnectingFlight\":true,\"aeroConnectingFlight\":{\"connectingFlightFrom\":\"CGK\",\"connectingFlightDate\":\"17 04 2014 08:50:00\",\"connectingFlightFno\":\"GA 564\",\"connectingFlightEtd\":\"17 04 2014 08:50:00\",\"connectingFlightEta\":\"17 04 2014 12:05:00\",\"connectingFlightClassname\":\"K\",\"connectingFlightPrice\":2066800,\"connectingFlightSeat\":9,\"connectingFlightTo\":\"BPN\",\"throughFare\":false,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"aeroConnectingFlight2\":null,\"aeroFlightClasses\":[{\"value\":null,\"className\":\"Y\",\"price\":1480300,\"avaliableSeat\":9,\"classId\":\"Y\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"D\",\"price\":3349200,\"avaliableSeat\":2,\"classId\":\"D\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"C\",\"price\":3516400,\"avaliableSeat\":9,\"classId\":\"C\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"J\",\"price\":3853000,\"avaliableSeat\":9,\"classId\":\"J\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null}],\"selectedClassId\":\"Q\",\"selectedClass\":{\"value\":null,\"className\":\"Y\",\"price\":1480300,\"avaliableSeat\":9,\"classId\":\"Y\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"isDepartureFlight\":true,\"adultPassengerSummary\":{\"value\":\"Adult 1\",\"total\":1480300,\"service\":null,\"passengerType\":\"Adult\",\"pax\":1,\"basic\":1480300,\"tax\":null,\"iwjr\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"childPassengerSummary\":{\"value\":\"Child 0\",\"total\":1480300,\"service\":null,\"passengerType\":\"Child\",\"pax\":0,\"basic\":1480300,\"tax\":null,\"iwjr\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"infantPassengerSummary\":{\"value\":\"Infant 0\",\"total\":1480300,\"service\":null,\"passengerType\":\"Infant\",\"pax\":0,\"basic\":1480300,\"tax\":null,\"iwjr\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"flightType\":\"connecting\",\"etd\":\"17 04 2014 06:30:00\",\"eta\":\"17 04 2014 06:30:00\",\"via\":\"-\"," +
//                "\"flightDate\":\"17 04 2014 05:30:00\",\"comission\":null,\"bookInfo\":null,\"responseCode\":null,\"total\":null,\"currencyCode\":null,\"description\":null,\"referenceNumber\":null,\"referenceName\":null,\"toAccountType\":null,\"providerName\":null,\"transactionDateString\":\"20/05/2014 11:34 \",\"traceNumber\":null,\"customerId\":null,\"billerCode\":null,\"cardNumber\":\"6278790011900001\",\"terminalId\":\"VOLT\",\"amount\":null,\"fee\":null,\"cardData2\":null,\"billerName\":null,\"bit48\":null,\"cardData1\":null,\"bit22\":null,\"customerReference\":null,\"transactionType\":\"9m\",\"accountNumber\":\"1574087868\",\"productCode\":null,\"providerCode\":\"VOLT\",\"accountType\":\"10\",\"merchantType\":\"6014\",\"transactionDate\":\"20 05 2014 11:34:12\",\"productName\":null,\"feeIndicator\":null,\"save\":null,\"inputType\":null}";
//        AeroFlightView departFlight = PojoJsonMapper.fromJson(jsonDepart, AeroFlightView.class);
//        System.out.println("departFlight.getFlightDate()=" + departFlight.getFlightDate());

/*
        String deposito = "0";
        System.out.println(PojoJsonMapper.toJson(deposito));

        String json = "{\"sessionId\":\"MBXfOA-9LT438Gu3vIYFS3LNEeRQsxDVFhohlww2VjM\",\"airlineId\":\"1\",\"bookInfo\":null,\"childPassengerSummary\":{\"value\":\"Child 0\",\"total\":606600,\"service\":null,\"basic\":606600,\"tax\":null,\"iwjr\":null,\"pax\":0,\"passengerType\":\"Child\",\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"infantPassengerSummary\":{\"value\":\"Infant 0\",\"total\":606600,\"service\":null,\"basic\":606600,\"tax\":null,\"iwjr\":null,\"pax\":0,\"passengerType\":\"Infant\",\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"airlineName\":\"Airasia\",\"flightType\":\"direct\",\"etd\":\"15 08 2014 07:40:00\",\"eta\":\"15 08 2014 08:55:00\",\"via\":\"-\",\"flightDate\":\"15 08 2014 07:40:00\",\"comission\":null,\"departureAirportCode\":\"CGK\",\"ticketPrice\":606600,\"arrivalAirportCode\":\"SUB\",\"departureAirportFullName\":null,\"arrivalAirportFullName\":null,\"flightNumber\":\"QZ 7680\",\"etaConnecting\":\"15 08 2014 08:55:00\",\"isConnectingFlight\":false,\"aeroConnectingFlight\":null,\"aeroConnectingFlight2\":null,\"aeroFlightClasses\":[{\"value\":null,\"className\":\"Promo\",\"classId\":\"DQZ1\",\"classLabel\":null,\"price\":606600,\"avaliableSeat\":5,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"Reguler\",\"classId\":\"DQZ2\",\"classLabel\":null,\"price\":841600,\"avaliableSeat\":5,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null}],\"selectedClassId\":\"DQZ1\",\"selectedClass\":{\"value\":null,\"className\":\"Promo\",\"classId\":\"DQZ1\",\"classLabel\":null,\"price\":606600,\"avaliableSeat\":5,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"isDepartureFlight\":true,\"adultPassengerSummary\":{\"value\":\"Adult 1\",\"total\":606600,\"service\":null,\"basic\":606600,\"tax\":null,\"iwjr\":null,\"pax\":1,\"passengerType\":\"Adult\",\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"responseCode\":null,\"total\":null,\"currencyCode\":null,\"description\":null,\"referenceNumber\":null,\"accountType\":\"10\",\"merchantType\":\"6014\",\"transactionDate\":\"08 08 2014 17:59:05\",\"productName\":null,\"feeIndicator\":null,\"referenceName\":null,\"toAccountType\":null,\"billerName\":null,\"billerCode\":null,\"customerId\":null,\"cardNumber\":\"6278790011900001\",\"terminalId\":\"AERO\",\"amount\":null,\"fee\":null,\"cardData2\":null,\"bit48\":null,\"transactionType\":\"9m\",\"save\":null,\"inputType\":null,\"cardData1\":null,\"bit22\":null,\"traceNumber\":null,\"providerName\":null,\"transactionDateString\":\"08/08/2014 17:59 \",\"customerReference\":null,\"accountNumber\":\"1574087868\",\"productCode\":null,\"providerCode\":\"AERO\"}";
        AeroFlightView view = PojoJsonMapper.fromJson(json, AeroFlightView.class);
        System.out.println("view.getInfantPassengerSummary()=" + view.getInfantPassengerSummary());
        System.out.println("view.getInfantPassengerSummary().getPax()=" + view.getInfantPassengerSummary().getPax());
        */


/*
        String str = "{\"childPassengerSummary\":null,\"ticketPrice\":3053000,\"sessionId\":\"-DzbPknmdHptRO-7UstnB_qfbQNJuxLrxOwhypTQgL4\",\"infantPassengerSummary\":null,\"departureAirportCode\":\"CGK\",\"arrivalAirportCode\":\"DJJ\",\"flightNumber\":\"GA650\",\"selectedClassId\":\"DGA9\",\"aeroConnectingFlight\":null,\"aeroConnectingFlight2\":null,\"airlineId\":\"4\",\"bookInfo\":null,\"flightDate\":\"18 09 2014 21:00:00\",\"etd\":\"18 09 2014 21:00:00\",\"eta\":\"18 09 2014 07:05:00\",\"airlineName\":\"Garuda\",\"flightType\":\"direct\",\"via\":\"-\",\"comission\":null,\"departureAirportFullName\":\"JAKARTA\",\"arrivalAirportFullName\":\"JAYAPURA\",\"etaConnecting\":\"18 09 2014 07:20:00\",\"isConnectingFlight\":false,\"aeroFlightClasses\":[{\"value\":null,\"className\":\"V\",\"price\":3053000,\"classId\":\"DGA1\",\"classLabel\":null,\"avaliableSeat\":9,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"T\",\"price\":3574400,\"classId\":\"DGA2\",\"classLabel\":null,\"avaliableSeat\":9,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"Q\",\"price\":4040800,\"classId\":\"DGA3\",\"classLabel\":null,\"avaliableSeat\":9,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"N\",\"price\":4208000,\"classId\":\"DGA4\",\"classLabel\":null,\"avaliableSeat\":9,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"K\",\"price\":4445600,\"classId\":\"DGA5\",\"classLabel\":null,\"avaliableSeat\":9,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"M\",\"price\":5025300,\"classId\":\"DGA6\",\"classLabel\":null,\"avaliableSeat\":9,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"B\",\"price\":5952600,\"classId\":\"DGA7\",\"classLabel\":null,\"avaliableSeat\":9,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"Y\",\"price\":6223200,\"classId\":\"DGA8\",\"classLabel\":null,\"avaliableSeat\":9,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null}],\"selectedClass\":{\"value\":null,\"className\":\"V\",\"price\":3053000,\"classId\":\"DGA9\",\"classLabel\":null,\"avaliableSeat\":9,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"isDepartureFlight\":true,\"adultPassengerSummary\":null,\"responseCode\":null,\"total\":null,\"currencyCode\":null,\"description\":null,\"referenceNumber\":null,\"transactionType\":\"9m\",\"customerReference\":null,\"accountNumber\":null,\"productCode\":null,\"providerCode\":\"AERO\",\"accountType\":null,\"merchantType\":null,\"transactionDate\":null,\"productName\":null,\"feeIndicator\":null,\"referenceName\":null,\"toAccountType\":null,\"billerName\":null,\"billerCode\":null,\"customerId\":null,\"cardNumber\":null,\"terminalId\":null,\"amount\":null,\"fee\":null,\"cardData2\":null,\"bit48\":null,\"cardData1\":null,\"bit22\":null,\"providerName\":null,\"transactionDateString\":null,\"traceNumber\":null,\"save\":null,\"inputType\":null}\n";
        String json = "{\"departureAirportFullName\":\"JAKARTA\",\"arrivalAirportFullName\":\"JAYAPURA\",\"etaConnecting\":\"18 09 2014 07:20:00\",\"isConnectingFlight\":false,\"aeroFlightClasses\":[{\"value\":null,\"className\":\"V\",\"avaliableSeat\":9,\"price\":3053000,\"classId\":\"DGA1\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"T\",\"avaliableSeat\":9,\"price\":3574400,\"classId\":\"DGA2\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"Q\",\"avaliableSeat\":9,\"price\":4040800,\"classId\":\"DGA3\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"N\",\"avaliableSeat\":9,\"price\":4208000,\"classId\":\"DGA4\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"K\",\"avaliableSeat\":9,\"price\":4445600,\"classId\":\"DGA5\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"M\",\"avaliableSeat\":9,\"price\":5025300,\"classId\":\"DGA6\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"B\",\"avaliableSeat\":9,\"price\":5952600,\"classId\":\"DGA7\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},{\"value\":null,\"className\":\"Y\",\"avaliableSeat\":9,\"price\":6223200,\"classId\":\"DGA8\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null}],\"selectedClass\":{\"value\":null,\"className\":\"V\",\"avaliableSeat\":9,\"price\":3053000,\"classId\":\"DGA9\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"isDepartureFlight\":true,\"adultPassengerSummary\":null,\"sessionId\":\"-DzbPknmdHptRO-7UstnB_qfbQNJuxLrxOwhypTQgL4\",\"ticketPrice\":3053000,\"airlineId\":\"4\",\"bookInfo\":null,\"flightDate\":\"18 09 2014 21:00:00\",\"etd\":\"18 09 2014 21:00:00\",\"eta\":\"18 09 2014 07:05:00\",\"infantPassengerSummary\":null,\"departureAirportCode\":\"CGK\",\"arrivalAirportCode\":\"DJJ\",\"flightNumber\":\"GA650\",\"selectedClassId\":\"DGA9\",\"aeroConnectingFlight\":null,\"aeroConnectingFlight2\":null,\"childPassengerSummary\":null,\"airlineName\":\"Garuda\",\"flightType\":\"direct\",\"via\":\"-\",\"comission\":null,\"responseCode\":null,\"total\":null,\"currencyCode\":null,\"description\":null,\"referenceNumber\":null,\"save\":null,\"inputType\":null,\"billerName\":null,\"billerCode\":null,\"customerId\":null,\"cardNumber\":null,\"terminalId\":null,\"amount\":null,\"fee\":null,\"cardData2\":null,\"bit48\":null,\"cardData1\":null,\"bit22\":null,\"providerName\":null,\"transactionDateString\":null,\"referenceName\":null,\"toAccountType\":null,\"transactionType\":\"9m\",\"customerReference\":null,\"accountNumber\":null,\"productCode\":null,\"providerCode\":\"AERO\",\"accountType\":null,\"merchantType\":null,\"transactionDate\":null,\"productName\":null,\"feeIndicator\":null,\"traceNumber\":null}";
        String json2 = "{\"departureAirportFullName\":\"JAKARTA\",\"arrivalAirportFullName\":\"JAYAPURA\",\"etaConnecting\":\"18 09 2014 07:20:00\",\"isConnectingFlight\":false,\"aeroFlightClasses\":[],\"selectedClass\":{\"value\":null,\"className\":\"V\",\"avaliableSeat\":9,\"price\":3053000,\"classId\":\"DGA9\",\"classLabel\":null,\"id\":null,\"updated\":null,\"createdby\":null,\"created\":null,\"updatedby\":null},\"isDepartureFlight\":true,\"adultPassengerSummary\":null,\"sessionId\":\"-DzbPknmdHptRO-7UstnB_qfbQNJuxLrxOwhypTQgL4\",\"ticketPrice\":3053000,\"airlineId\":\"4\",\"bookInfo\":null,\"flightDate\":\"18 09 2014 21:00:00\",\"etd\":\"18 09 2014 21:00:00\",\"eta\":\"18 09 2014 07:05:00\",\"infantPassengerSummary\":null,\"departureAirportCode\":\"CGK\",\"arrivalAirportCode\":\"DJJ\",\"flightNumber\":\"GA650\",\"selectedClassId\":\"DGA9\",\"aeroConnectingFlight\":null,\"aeroConnectingFlight2\":null,\"childPassengerSummary\":null,\"airlineName\":\"Garuda\",\"flightType\":\"direct\",\"via\":\"-\",\"comission\":null,\"responseCode\":null,\"total\":null,\"currencyCode\":null,\"description\":null,\"referenceNumber\":null,\"save\":null,\"inputType\":null,\"billerName\":null,\"billerCode\":null,\"customerId\":null,\"cardNumber\":null,\"terminalId\":null,\"amount\":null,\"fee\":null,\"cardData2\":null,\"bit48\":null,\"cardData1\":null,\"bit22\":null,\"providerName\":null,\"transactionDateString\":null,\"referenceName\":null,\"toAccountType\":null,\"transactionType\":\"9m\",\"customerReference\":null,\"accountNumber\":null,\"productCode\":null,\"providerCode\":\"AERO\",\"accountType\":null,\"merchantType\":null,\"transactionDate\":null,\"productName\":null,\"feeIndicator\":null,\"traceNumber\":null}";

        AeroFlightView aeroFlightView = gson.fromJson(json2, AeroFlightView.class);

        System.out.println("json=" + json);
        System.out.println("aeroFlightView=" + aeroFlightView);*/


        Type type = new TypeToken<Map<String, Object>>() {}.getType();
        String jsonString = "{\"total\":null,\"agentMargin\":null,\"ticketComission\":0,\"totalInfant\":0,\"agentPrice\":0,\"save\":false,\"assurancePrice\":0,\"totalCustomerPrice\":0,\"assuranceComission\":0,\"totalAgentComission\":0,\"terminalId\":\"MBS\",\"agentMargin\":10000,\"passengers\":[{\"passengerDob\":\"06 12 2014 15:45:28\",\"passengerType\":\"Adult\",\"country\":\"INDONESIA\"}],\"accountNumber\":\"1527096622\",\"transactionDateString\":\"06\\/12\\/2014 15:45\",\"returnAirlineCode\":\"QG,GA,MZ,SJ,JT\",\"destinationAirportCode\":\"SUB\",\"customerId\":103442,\"merchantType\":\"6017\",\"ticketPrice\":0,\"totalAgentPrice\":0,\"departureAirportCode\":\"CGK\",\"departDate\":\"06 12 2014 15:45:28\",\"currencyCode\":\"360\",\"isDepartOnly\":true,\"providerCode\":\"VOLT\",\"transactionDate\":\"06 12 2014 15:45:28\",\"transactionType\":\"9m\",\"departureAirportName\":\"JAKARTA\",\"totalAdult\":1,\"toAccountType\":\"00\",\"destinationAirportName\":\"SURABAYA\",\"totalChildren\":0,\"accountType\":\"10\",\"departAirlineCode\":\"QG,GA,MZ,SJ,JT\",\"cardNumber\":\"6278790127040710\"}";

        Gson gson = new GsonBuilder().setDateFormat("dd MM yyyy HH:mm:ss").serializeNulls().create();
        AeroTicketingView aeroTicketingView = gson.fromJson(jsonString, AeroTicketingView.class);

        String json = new GsonBuilder().setPrettyPrinting().create().toJson(aeroTicketingView);
        System.out.println(json);

        String str = "{\"withInsurance\":false,\"departFlight\":{\"total\":415125,\"eta\":\"11 01 2015 08:50:00\",\"airlineName\":\"Airasia\",\"via\":\"-\",\"selectedClassId\":\"DQZ1\",\"etd\":\"11 01 2015 06:00:00\",\"isConnectingFlight\":false,\"referenceNumber\":null,\"billerCode\":null,\"billerName\":null,\"terminalId\":\"MBS\",\"amount\":null,\"transactionDateString\":\"11\\/01\\/2015 07:25 \",\"accountNumber\":null,\"traceNumber\":null,\"description\":null,\"merchantType\":\"6017\",\"ticketPrice\":406900,\"departureAirportCode\":\"CGK\",\"arrivalAirportCode\":\"DPS\",\"responseCode\":null,\"airlineId\":\"1\",\"transactionDate\":\"11 01 2015 07:25:07\",\"fee\":null,\"childPassengerSummary\":null,\"toAccountType\":\"00\",\"providerName\":null,\"sessionId\":\"f4gRq0Xas33fIPa9GMdnIazuMvAQI9zT7KLAx9uWCEI\",\"flightDate\":\"11 01 2015 06:00:00\",\"bit22\":null,\"aeroFlightClasses\":[],\"flightType\":\"direct\",\"isDepartureFlight\":true,\"cardData1\":null,\"cardData2\":null,\"bookInfo\":null,\"flightNumber\":\"QZ 7510\",\"adultPassengerSummary\":{\"total\":406900,\"tax\":32900,\"basic\":329000,\"updatedby\":null,\"pax\":1,\"passengerType\":\"Adult\",\"service\":40000,\"iwjr\":1},\"aeroConnectingFlight2\":null,\"aeroConnectingFlight\":null,\"customerId\":null,\"inputType\":null,\"comission\":8225,\"arrivalAirportFullName\":\"DENPASAR\",\"bit48\":null,\"currencyCode\":\"360\",\"etaConnecting\":\"11 01 2015 08:50:00\",\"productCode\":null,\"feeIndicator\":null,\"selectedClass\":{\"id\":null,\"price\":406900,\"updatedby\":null,\"created\":null,\"updated\":null,\"classId\":\"DQZ1\",\"value\":null,\"className\":\"Promo\",\"createdby\":null,\"classLabel\":null,\"avaliableSeat\":5},\"providerCode\":\"AERO\",\"customerReference\":null,\"referenceName\":null,\"transactionType\":\"9l\",\"infantPassengerSummary\":null,\"departureAirportFullName\":\"JAKARTA\",\"accountType\":\"10\",\"cardNumber\":\"6278790127040710\"}}";
        aeroTicketingView = gson.fromJson(str, AeroTicketingView.class);
        System.out.println("aeroTicketingView=" + aeroTicketingView);
        System.out.println("aeroTicketingView.getTransactionType()=" + aeroTicketingView.getTransactionType());



        str = "{\"transactionType\":\"9l\",\"withInsurance\":false,\"departFlight\":{\"total\":637900,\"eta\":\"15 01 2015 08:50:00\",\"airlineName\":\"Airasia\",\"via\":\"-\",\"selectedClassId\":\"DQZ1\",\"etd\":\"15 01 2015 06:00:00\",\"isConnectingFlight\":false,\"billerCode\":null,\"billerName\":null,\"amount\":null,\"terminalId\":\"MBS\",\"transactionDateString\":\"15\\/01\\/2015 03:31 \",\"accountNumber\":null,\"traceNumber\":null,\"description\":null,\"merchantType\":\"6017\",\"ticketPrice\":637900,\"departureAirportCode\":\"CGK\",\"arrivalAirportCode\":\"DPS\",\"responseCode\":null,\"airlineId\":\"1\",\"transactionDate\":\"15 01 2015 03:31:53\",\"fee\":null,\"childPassengerSummary\":null,\"toAccountType\":\"00\",\"providerName\":null,\"sessionId\":\"MuMyYgS7z_2a3bLcAHpvS1UimG2PonKDV-DAqIvLnkc\",\"flightDate\":\"15 01 2015 06:00:00\",\"bit22\":null,\"aeroFlightClasses\":[],\"flightType\":\"direct\",\"isDepartureFlight\":true,\"cardData1\":null,\"cardData2\":null,\"bookInfo\":null,\"flightNumber\":\"QZ 7510\",\"adultPassengerSummary\":{\"total\":637900,\"tax\":53900,\"basic\":539000,\"updatedby\":null,\"passengerType\":\"Adult\",\"pax\":1,\"service\":40000,\"iwjr\":1},\"aeroConnectingFlight2\":null,\"aeroConnectingFlight\":null,\"customerId\":null,\"inputType\":null,\"comission\":13475,\"arrivalAirportFullName\":\"DENPASAR\",\"bit48\":null,\"currencyCode\":\"360\",\"etaConnecting\":\"15 01 2015 08:50:00\",\"productCode\":null,\"withInsurance\":false,\"feeIndicator\":null,\"selectedClass\":{\"id\":null,\"price\":637900,\"updated\":null,\"updatedby\":null,\"created\":null,\"classId\":\"DQZ1\",\"value\":null,\"className\":\"Promo\",\"createdby\":null,\"classLabel\":null,\"avaliableSeat\":5},\"providerCode\":\"AERO\",\"customerReference\":null,\"referenceName\":null,\"transactionType\":\"9l\",\"infantPassengerSummary\":null,\"departureAirportFullName\":\"JAKARTA\",\"accountType\":\"10\",\"cardNumber\":\"6278790127040710\"}}";
        str = "{\"total\":637900,\"eta\":\"15 01 2015 08:50:00\",\"airlineName\":\"Airasia\",\"via\":\"-\",\"selectedClassId\":\"DQZ1\",\"etd\":\"15 01 2015 06:00:00\",\"isConnectingFlight\":false,\"billerCode\":null,\"billerName\":null,\"amount\":null,\"terminalId\":\"MBS\",\"transactionDateString\":\"15\\/01\\/2015 03:31 \",\"accountNumber\":null,\"traceNumber\":null,\"description\":null,\"merchantType\":\"6017\",\"ticketPrice\":637900,\"departureAirportCode\":\"CGK\",\"arrivalAirportCode\":\"DPS\",\"responseCode\":null,\"airlineId\":\"1\",\"transactionDate\":\"15 01 2015 03:31:53\",\"fee\":null,\"childPassengerSummary\":null,\"toAccountType\":\"00\",\"providerName\":null,\"sessionId\":\"MuMyYgS7z_2a3bLcAHpvS1UimG2PonKDV-DAqIvLnkc\",\"flightDate\":\"15 01 2015 06:00:00\",\"bit22\":null,\"aeroFlightClasses\":[],\"flightType\":\"direct\",\"isDepartureFlight\":true,\"cardData1\":null,\"cardData2\":null,\"bookInfo\":null,\"flightNumber\":\"QZ 7510\",\"adultPassengerSummary\":{\"total\":637900,\"tax\":53900,\"basic\":539000,\"updatedby\":null,\"passengerType\":\"Adult\",\"pax\":1,\"service\":40000,\"iwjr\":1},\"aeroConnectingFlight2\":null,\"aeroConnectingFlight\":null,\"customerId\":null,\"inputType\":null,\"comission\":13475,\"arrivalAirportFullName\":\"DENPASAR\",\"bit48\":null,\"currencyCode\":\"360\",\"etaConnecting\":\"15 01 2015 08:50:00\",\"productCode\":null,\"withInsurance\":false,\"feeIndicator\":null,\"selectedClass\":{\"id\":null,\"price\":637900,\"updated\":null,\"updatedby\":null,\"created\":null,\"classId\":\"DQZ1\",\"value\":null,\"className\":\"Promo\",\"createdby\":null,\"classLabel\":null,\"avaliableSeat\":5},\"providerCode\":\"AERO\",\"customerReference\":null,\"referenceName\":null,\"transactionType\":\"9l\",\"infantPassengerSummary\":null,\"departureAirportFullName\":\"JAKARTA\",\"accountType\":\"10\",\"cardNumber\":\"6278790127040710\"}";
        try {
            logger.info("str=" + str);
            AeroFlightView view = gson.fromJson(str, AeroFlightView.class);
            aeroTicketingView = new AeroTicketingView();
            aeroTicketingView.setTransactionType("9l");
            aeroTicketingView.setDepartureFlight(view);

            System.out.println("aeroTicketingView.getDepartureFlight()=" + aeroTicketingView.getDepartureFlight());


        } catch (BusinessException e) {
            e.printStackTrace();
            PacResponseError responseError = new PacResponseError();
            responseError.setCode(e.getErrorCode());
            responseError.setDescription(e.getFullMessage());
            String res = gson.toJson(responseError);
            logger.info("exception res=" + res);

        }
    }
}
