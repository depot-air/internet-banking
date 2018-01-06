package com.dwidasa.ib.pages.transferBatch;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.model.TransferBatch;
import com.dwidasa.engine.service.TransferBatchService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.common.EvenOdd;
import com.dwidasa.ib.common.GeneralUtil;
import com.dwidasa.ib.services.SessionManager;

public class TransferBatchReportResult {
	@Property(write = false)
    @Persist
    private String accountNumber;

    @Property(write = false)
    @Persist
    private Date startDate;

    @Property(write = false)
    @Persist
    private Date endDate;

    @Inject
    private TransferBatchService transferBatchService;

    @Inject
    private Locale locale;

    @Property
    private DateFormat customDate = new SimpleDateFormat("dd-MMM-yy HH:mm:ss", locale);

    @Property
    private DateFormat shortDate = new SimpleDateFormat(Constants.SHORT_FORMAT, locale);

    @Property
    private EvenOdd evenOdd;

    @Property
    private List<TransferBatch> transferBatchList;

    @Property
    private TransferBatch row;

    @Inject
    private Messages messages;

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    Object onActivate() {
        if (accountNumber == null) {
            return TransferBatchReport.class;
        }
        return null;
    }
    
    @Inject
    private SessionManager sessionManager;

    void setupRender() {
        evenOdd = new EvenOdd();
        evenOdd.setEven(true);

        transferBatchList = transferBatchService.getTransferBatchList(sessionManager.getLoggedCustomerView().getId(), accountNumber, startDate, endDate);
    }

    @DiscardAfter
    Object onSuccessFromForm() {
        return TransferBatchReport.class;
    }
    
    public String getStrDate(Date date) {
		if (date == null) return " - ";
		return GeneralUtil.fmtDate.print(date.getTime());
	}
	
    public String getStrStatus(String status) {
		return messages.get("status" + status);
	}
	
	@Inject
	private ThreadLocale threadLocale;

	@SuppressWarnings("unused")
	@Property
	private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());
	
	public boolean isMultipleDays() {
		if (startDate == null || endDate == null) return false;
		if (endDate.after(startDate)) return true;
		return false;
	}
	
	@InjectPage
	private TransferBatchReceipt transferBatchReceipt;
	
	Object onShowDetail(Long id) {
        TransferBatch transferBatchView = transferBatchService.getWithContent(id);
        transferBatchReceipt.setTransferBatchView(transferBatchView);
        transferBatchReceipt.setFromHistory(true);
        return transferBatchReceipt;
    }
}


