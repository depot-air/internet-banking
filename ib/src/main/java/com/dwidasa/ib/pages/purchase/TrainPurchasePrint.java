package com.dwidasa.ib.pages.purchase;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.joda.time.DateTime;

import com.dwidasa.engine.model.train.TrainPassenger;
import com.dwidasa.engine.model.view.TrainPurchaseView;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.base.BasePrintPage;

public class TrainPurchasePrint extends BasePrintPage {
	@Inject
    private Locale locale;
	
	@Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(locale);

    @Property
    private DateFormat mediumDate = new SimpleDateFormat(Constants.MEDIUM_FORMAT, locale);
    
    @Property
    private DateFormat longDate = new SimpleDateFormat(Constants.LONG_FORMAT, threadLocale.getLocale());
	
	@Property
    private DateFormat shortDate = new SimpleDateFormat(Constants.KAI_SHORT_DATE, threadLocale.getLocale());
	
	@Property
    private DateFormat shortTime = new SimpleDateFormat(Constants.KAI_TIME, threadLocale.getLocale());
	
	@Property
	private int idx;

    @InjectPage
    private TrainPurchaseReceipt trainPurchaseReceipt;

    @Property
    private TrainPurchaseView view;
    
    @Property
    private boolean fromHistory;
    
    @Property
    private TrainPassenger passenger;
    
    @Property
    private String status;
    
    @Inject
    private Messages messages;
    
    void setupRender() {
    	view = trainPurchaseReceipt.getView();
    	fromHistory = trainPurchaseReceipt.isFromHistory();
    	if (view.getResponseCode() != null && view.getResponseCode().equals(Constants.SUCCESS_CODE)) {
        	status = messages.get("success");
        } else {
            status = messages.get("failed");
        }
    }
    
    public boolean isAdult() {
		if (passenger == null) return false;
		return "A".equals(passenger.getType());
	}
    
    public boolean isHasSeat() {
		if (passenger == null) return false;
		return "A".equals(passenger.getType()) || "C".equals(passenger.getType());
	}

    public String getTitle() {
    	if (fromHistory) {
    		return "Reprint Bukti Pembelian Tiket Kereta Api";
    	} else {
    		return "Bukti Pembelian Tiket Kereta Api";
    	}
    }
    
    public String getStrDepartureDate() {
    	if (view == null || view.getDepartureDate() == null) return "";
    	DateFormat fmt = new SimpleDateFormat(", dd MMM yyyy, HH:mm");
    	DateTime jodaDate = new DateTime(view.getDepartureDate());
    	String hari = messages.get("day" + jodaDate.getDayOfWeek());
    	return hari + fmt.format(view.getDepartureDate()) + " WIB";
    }
    
    public String getStrArrivalDate() {
    	if (view == null || view.getArrivalDate() == null) return "";
    	DateFormat fmt = new SimpleDateFormat(", dd MMM yyyy, HH:mm");
    	DateTime jodaDate = new DateTime(view.getArrivalDate());
    	String hari = messages.get("day" + jodaDate.getDayOfWeek());
    	return hari + fmt.format(view.getArrivalDate()) + " WIB";
    }
    
    
}

