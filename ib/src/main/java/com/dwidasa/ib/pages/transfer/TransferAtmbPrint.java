package com.dwidasa.ib.pages.transfer;

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

public class TransferAtmbPrint extends BasePrintPage {
	@Inject
    private Locale locale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(locale);

    @Property
    private DateFormat shortDate = new SimpleDateFormat("MMMM yyyy", locale);

    @Property
    private DateFormat mediumDate = new SimpleDateFormat(Constants.MEDIUM_FORMAT, locale);
    
    @InjectPage
    private TransferAtmbReceipt transferAtmbReceipt;

    @Property
    private TransferView transferView;
        
    @Property
    private String status;
    
    @Inject
    private Messages messages;
    
    @Property
    private String note1;

    @Property
    private String note2;

    void setupRender() {
    	transferView = transferAtmbReceipt.getTransferReceiptView();
    	
    	if(transferView.getTransactionStatus().toUpperCase().equals(com.dwidasa.engine.Constants.SUCCEED_STATUS))
        {
            note1 = messages.get("transferTransSuccess1");
            note2 = messages.get("transferTransSuccess2");
        }
        else
        if(transferView.getTransactionStatus().toUpperCase().equals(com.dwidasa.engine.Constants.PENDING_STATUS))
        {
            note1 = messages.get("transferTransPending1");
            note2 = messages.get("transferTransPending2");
        }
    	
    }
    
    @Inject
    private ThreadLocale threadLocale;
    
    @Property
    private DateFormat longDate = new SimpleDateFormat(com.dwidasa.ib.Constants.LONG_FORMAT, threadLocale.getLocale());

    public boolean isAlto() {    	
     	return transferView.getTransactionType().equals(com.dwidasa.engine.Constants.ALTO.TT_POSTING);
     }
}
