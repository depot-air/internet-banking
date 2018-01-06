package com.dwidasa.ib.pages.purchase;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.AeroPassenger;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.TransactionData;
import com.dwidasa.engine.model.view.AeroTicketingView;
import com.dwidasa.engine.model.view.PlnPurchaseView;
import com.dwidasa.engine.service.TransactionDataService;
import com.dwidasa.engine.service.TransactionService;
import com.dwidasa.engine.service.facade.KioskReprintService;
import com.dwidasa.engine.service.facade.PurchaseService;
import com.dwidasa.engine.service.transform.TransformerFactory;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.base.BasePrintPage;
import org.apache.log4j.Logger;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 12/10/11
 * Time: 19:38
 */
public class AeroTicketingPrint extends BasePrintPage {
	private Logger logger = Logger.getLogger(AeroTicketingPrint.class);
    @Inject private Locale locale;

    @Property private NumberFormat formatter = NumberFormat.getInstance(locale);
    @Property private DateFormat mediumDate = new SimpleDateFormat(Constants.MEDIUM_FORMAT, locale);
    @Property private DateFormat hhmm = new SimpleDateFormat(Constants.DATEFORMAT.HH_MM, locale);

    @InjectPage
    private AeroTicketingFlightResult aeroTicketingFlightResult;

    @InjectComponent
    private Form form;
    
    @Property private AeroTicketingView aeroTicketingView;

    @SessionAttribute(value = "reprintTransactionId")
    private Long transactionId;

    @ActivationRequestParameter
    private String reprint;

    @Inject private TransactionService transactionService;
    @Inject private TransactionDataService transactionDataService;
    @Inject private PurchaseService purchaseService;
    @Inject private KioskReprintService kioskReprintService;

    @Persist @Property private AeroPassenger aeroPassenger1;
    @Persist @Property private AeroPassenger aeroPassenger2;
    @Persist @Property private AeroPassenger aeroPassenger3;
    @Persist @Property private AeroPassenger aeroPassenger4;
    @Persist @Property private AeroPassenger aeroPassenger5;
    @Persist @Property private AeroPassenger aeroPassenger6;
    @Persist @Property private AeroPassenger aeroPassenger7;

    private String getToken(String token) {
    	String vToken = token.substring(0, 4) + "  " + token.substring(4, 8) + "  " +  token.substring(8, 12) + "  " + token.substring(12, 16);
        if(token.length() >= 20)
            vToken = vToken + "  " + token.substring(16, 20);
        return vToken;
    }
    void setupRender() {
        aeroTicketingView = aeroTicketingFlightResult.getAeroTicketingView();
        setPassenger();
    }


    private void setPassenger() {
   	    for (int i = 0; i < aeroTicketingView.getPassengers().size(); i++) {
   	    	AeroPassenger aeroPassenger = aeroTicketingView.getPassengers().get(i);
   			if (i == 0) {
   	    		aeroPassenger1 = aeroPassenger;
   	    	} else if (i == 1) {
   	    		aeroPassenger2 = aeroPassenger;
   	    	} else if (i == 2) {
   	    		aeroPassenger3 = aeroPassenger;
   	    	} else if (i == 3) {
   	    		aeroPassenger4 = aeroPassenger;
   	    	} else if (i == 4) {
   	    		aeroPassenger5 = aeroPassenger;
   	    	} else if (i == 5) {
   	    		aeroPassenger6 = aeroPassenger;
   	    	} else if (i == 6) {
   	    		aeroPassenger7 = aeroPassenger;
   	    	}
   	    }
   	}

    public boolean isTwoPassengers() {
    	return (aeroPassenger2 != null);
    }
    public boolean isThreePassengers() {
    	return (aeroPassenger3 != null);
    }
    public boolean isFourPassengers() {
    	return (aeroPassenger4 != null);
    }
    public boolean isFivePassengers() {
    	return (aeroPassenger5 != null);
    }
    public boolean isSixPassengers() {
    	return (aeroPassenger6 != null);
    }
    public boolean isSevenPassengers() {
    	return (aeroPassenger7 != null);
    }

    public boolean isPassanger2Adult() { return aeroPassenger2.getPassengerType().equals(AeroPassenger.PASSENGER_TYPE.ADULT); }
    public boolean isPassanger3Adult() { return aeroPassenger3.getPassengerType().equals(AeroPassenger.PASSENGER_TYPE.ADULT); }
    public boolean isPassanger4Adult() { return aeroPassenger4.getPassengerType().equals(AeroPassenger.PASSENGER_TYPE.ADULT); }
    public boolean isPassanger5Adult() { return aeroPassenger5.getPassengerType().equals(AeroPassenger.PASSENGER_TYPE.ADULT); }
    public boolean isPassanger6Adult() { return aeroPassenger6.getPassengerType().equals(AeroPassenger.PASSENGER_TYPE.ADULT); }
    public boolean isPassanger7Adult() { return aeroPassenger7.getPassengerType().equals(AeroPassenger.PASSENGER_TYPE.ADULT); }

    @Property
    private String _footer;

    public boolean isSuccess() {
    	return aeroTicketingView != null;
    }
    
    public String getcurrencyFormat(BigDecimal n) {
        return NumberFormat.getCurrencyInstance().format(n).replace("$", "");
    }

    public boolean isDepartConnecting() {
    	return aeroTicketingView.getDepartureFlight().getAeroConnectingFlight() != null;
    }
    public boolean isDepartConnecting2() {
        return aeroTicketingView.getDepartureFlight().getAeroConnectingFlight2() != null;
    }

    public boolean isDepartReturn() {
    	return aeroTicketingView.getIsDepartOnly() == false;
    }
    public boolean isReturnConnecting() {
    	return aeroTicketingView.getReturnFlight().getAeroConnectingFlight() != null;
    }
    public boolean isReturnConnecting2() {
        return aeroTicketingView.getReturnFlight().getAeroConnectingFlight2() != null;
    }


}
