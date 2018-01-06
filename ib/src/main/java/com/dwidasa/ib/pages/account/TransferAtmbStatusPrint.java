package com.dwidasa.ib.pages.account;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.base.BasePrintPage;

public class TransferAtmbStatusPrint extends BasePrintPage {
	@Inject
    private Locale locale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(locale);

    @Property
    private DateFormat shortDate = new SimpleDateFormat("MMMM yyyy", locale);

    @Property
    private DateFormat mediumDate = new SimpleDateFormat(Constants.MEDIUM_FORMAT, locale);

    @InjectPage
    private TransferATMBStatusReceipt transferATMBStatusReceipt;

    @Property
    private TransferView transferView;
        
    @Property
    private String status;
    
    @Inject
    private Messages messages;

    void setupRender() {
    	transferView = transferATMBStatusReceipt.getTransferInputsView();
    	
    }
    
    @Inject
    private ThreadLocale threadLocale;
    
    @Property
    private DateFormat longDate = new SimpleDateFormat(com.dwidasa.ib.Constants.LONG_FORMAT, threadLocale.getLocale());
    
}
