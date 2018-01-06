package com.dwidasa.ib.pages.transferBatch;

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

import com.dwidasa.engine.model.TransferBatch;
import com.dwidasa.ib.Constants;

public class TransferBatchPrint {

	private Logger logger = Logger.getLogger(TransferBatchPrint.class);
	@Inject
    private Locale locale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(locale);

    @Property
    private DateFormat shortDate = new SimpleDateFormat("MMMM yyyy", locale);

    @Property
    private DateFormat mediumDate = new SimpleDateFormat(Constants.MEDIUM_FORMAT, locale);

    @InjectPage
    private TransferBatchReceipt transferBatchReceipt;

    @Property
    private TransferBatch transferBatchView;
    
    @Property
    private String typeTransfer;
    
    @Property
    private String status;
    
    @Inject
    private Messages messages;

    void setupRender() {
    	transferBatchView = transferBatchReceipt.getTransferBatchView();
    }

	public String getStrTransferType(Integer transferType) {
		if (transferType == null) return "-";
		if (transferType.intValue() == Constants.TRANSFER_POSTDATE) {
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.MEDIUM_FORMAT, threadLocale.getLocale());
			return String.format(messages.get("transferType"+transferType), sdf.format(transferBatchView.getValueDate()));
		} else if (transferType.intValue() == Constants.TRANSFER_NOW) {
			return messages.get("transferType"+transferType);
		} else if (transferType.intValue() == Constants.TRANSFER_PERIODIC) {
			return messages.get("transferType"+transferType);
		}
		return "-";
	}
	
	public String getStrStatus(String status) {
		if (status.equals(com.dwidasa.engine.Constants.SUCCEED_STATUS)) {
        	status = messages.get("success");
        } else if (status.equals(com.dwidasa.engine.Constants.PENDING_STATUS)) {
        	status = messages.get("pending");
        } else {
            status = messages.get("failed");
        }
		return status;
	}

    @Inject
    private ThreadLocale threadLocale;
    
    @Property
    private DateFormat longDate = new SimpleDateFormat(com.dwidasa.ib.Constants.LONG_FORMAT, threadLocale.getLocale());
    
    @Property
    private DateFormat time = new SimpleDateFormat("hh:mm:ss", threadLocale.getLocale());
    
}
