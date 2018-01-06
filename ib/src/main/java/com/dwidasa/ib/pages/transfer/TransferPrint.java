package com.dwidasa.ib.pages.transfer;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.base.BasePrintPage;

public class TransferPrint extends BasePrintPage {
	private Logger logger = Logger.getLogger(TransferPrint.class);
	@Inject
    private Locale locale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(locale);

    @Property
    private DateFormat shortDate = new SimpleDateFormat("MMMM yyyy", locale);

    @Property
    private DateFormat mediumDate = new SimpleDateFormat(Constants.MEDIUM_FORMAT, locale);

    @InjectPage
    private TransferReceipt transferReceipt;

    @Property
    private TransferView transferView;
    
    @Property
    private String typeTransfer;
    
    @Property
    private String status;
    
    @Inject
    private Messages messages;

    void setupRender() {
    	transferView = transferReceipt.getTransferView();
    	logger.info("transferView=" + transferView);
    	if (transferView.getResponseCode() != null && transferView.getResponseCode().equals(Constants.SUCCESS_CODE)) {
        	status = messages.get("success");
        } else {
            status = messages.get("failed");
        }

        if (transferView.getTransferType() == Constants.TRANSFER_NOW) {
            typeTransfer = messages.get("transferImmediate");
        } else if (transferView.getTransferType() == Constants.TRANSFER_POSTDATE) {
            SimpleDateFormat sdate = new SimpleDateFormat(Constants.MEDIUM_FORMAT);
            String nowDate = sdate.format(transferView.getValueDate());
            nowDate = nowDate + " WIB";
            typeTransfer = messages.get("transferPostDate") + " " + nowDate;
            status = messages.get("pending");
        } else if (transferView.getTransferType() == Constants.TRANSFER_PERIODIC) {
            typeTransfer = messages.get("transferPeriod");
            status = messages.get("pending");
        }
        
    }
    
    @Inject
    private ThreadLocale threadLocale;
    
    @Property
    private DateFormat longDate = new SimpleDateFormat(com.dwidasa.ib.Constants.LONG_FORMAT, threadLocale.getLocale());
    
    @Property
    private DateFormat time = new SimpleDateFormat("hh:mm:ss", threadLocale.getLocale());
    
}
