package com.dwidasa.ib.pages.transferBatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.dwidasa.engine.model.Batch;
import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.service.BatchService;
import com.dwidasa.engine.util.DateUtils;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.SessionManager;
import com.howardlewisship.tapx.datefield.components.DateField;

@Import(library = "context:bprks/js/transferBatch/TransferBatchReport.js")
public class TransferBatchReport {
	@Property
	@Persist
	private AccountView accountView;

	@Property
	private Long batchId;
	
	@Property
	private Date startDate;

	@Property
	private Date endDate;

	@Inject
	private BatchService batchService;

	@Property
	private SelectModel batchListModel;
	
	@Inject
	private SessionManager sessionManager;
	
	@Inject
	private GenericSelectModelFactory genericSelectModelFactory;
	
	@Inject
	private Messages messages;
	
	@Inject
    private Locale locale;
	
	@Property
    private DateFormat shortDate = new SimpleDateFormat(Constants.SHORT_FORMAT, locale);

	void setupRender() {
		List<Batch> batchList = batchService.getAll(sessionManager.getLoggedCustomerView().getId());
		batchListModel = genericSelectModelFactory.createPlusMinusOne(batchList, messages.get("selectAll"));
		periodValue = "today";
	}
	
	@Property
    private String periodValue;
	
	public String getShortPattern() {
        return Constants.SHORT_FORMAT;
    }

    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
    	return null;
    }

	void onPrepare() {
		endDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -31);
        startDate = cal.getTime();
		if (accountView == null) {
            accountView = new AccountView();
        }
	}
	
	@InjectComponent
	private Form form;
	
	@InjectComponent("startDate")
	private DateField start;
	
	void onValidateFromForm() {
        if (DateUtils.truncate(startDate).compareTo(DateUtils.truncate(endDate)) > 0) {
            form.recordError(start, messages.get("startDate-acrossField-message"));
        }
        else if (DateUtils.before(new Date(), 31).compareTo(DateUtils.truncate(startDate)) > 0) {
            form.recordError(start, messages.get("startDate-acrossField-message"));
        }
    }
	
	@Inject
	private JavaScriptSupport javaScriptSupport;
	
	@AfterRender
    public void afterRender() {
    	javaScriptSupport.addScript("new AutoChecker();");
    }
	
	@InjectPage
	private TransferBatchReportResult transferBatchReportResult;
	
	Object onSuccessFromForm() {
        if (periodValue.equals("today")) {
            startDate = new Date();
            endDate = startDate;
        }

        accountView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        transferBatchReportResult.setAccountNumber(accountView.getAccountNumber());
        transferBatchReportResult.setStartDate(startDate);
        transferBatchReportResult.setEndDate(endDate);

        return transferBatchReportResult;
    }
}
