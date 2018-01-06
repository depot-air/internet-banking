package com.dwidasa.ib.pages.transferBatch;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.model.TransferBatch;
import com.dwidasa.ib.Constants;


public class TransferBatchReceipt {
	@Persist
	private TransferBatch transferBatchView;
	
	public void setTransferBatchView(TransferBatch transferBatchView) {
		this.transferBatchView = transferBatchView;
	}

    public TransferBatch getTransferBatchView() {
    	return transferBatchView;
    }

	Object onActivate() {
		if (transferBatchView == null) {
			return TransferBatchInput.class;
		}
		return null;
	}
	
	@Inject
	private Messages messages;
	
	@Inject
	private ThreadLocale threadLocale;
	
	@Property
	private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

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

	@Property
    private DateFormat longDate = new SimpleDateFormat(Constants.LONG_FORMAT, threadLocale.getLocale());
	
	/*
	@Property
	private TransferBatchContent row;
	*/
	public int getPageSize() {
		return Constants.PAGE_SIZE;
	}
	
	@Persist
    private boolean fromHistory;
	
    public void setFromHistory(boolean fromHistory) {
    	this.fromHistory = true;
    }
    
    public boolean isFromHistory() {
    	return fromHistory;
    }
    
    @DiscardAfter
	Object onSuccessFromForm() {
    	return TransferBatchReportResult.class;
	}
	
}
