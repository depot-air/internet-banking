package com.dwidasa.ib.pages.transferBatch;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.springframework.beans.factory.annotation.Autowired;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.ExtendedProperty;
import com.dwidasa.engine.model.Batch;
import com.dwidasa.engine.model.BatchContent;
import com.dwidasa.engine.model.TransferBatch;
import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.service.BatchContentService;
import com.dwidasa.engine.service.BatchService;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.engine.service.facade.TransferService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.common.DayOfWeek;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.SessionManager;

@Import(library = "context:bprks/js/transferBatch/TransferBatchInput_123.js")
public class TransferBatchInput {
	@Property
	@Persist
	private TransferBatch transferBatchView;

	@Property
	private SelectModel batchListModel;

	@Inject
	private BatchService batchService;

	@Inject
	private GenericSelectModelFactory genericSelectModelFactory;

	@Inject
	private SessionManager sessionManager;

    @Inject
    private CacheManager cacheManager;

    @Autowired
    private ExtendedProperty extendedProperty;
    
	void setupRender() {
		transferBatchView = new TransferBatch();
		transferBatchView.setTransferType(1);
		List<Batch> batchList = batchService.getAll(sessionManager.getLoggedCustomerView().getId());
		batchListModel = genericSelectModelFactory.create(batchList);
	}

	public Object onActivate() {
		if (sessionManager.isNotActivatedYet()) {
			return EulaWelcome.class;
		}
		return null;
	}

	public String getDateFieldFormat() {
		return Constants.SHORT_FORMAT;
	}

	@Inject
	private JavaScriptSupport javaScriptSupport;

	@AfterRender
	public void afterRender() {
		javaScriptSupport.addScript("new AutoChecker();");
		javaScriptSupport.addScript(InitializationPriority.LATE, "$('accountNumber').focus()");
	}

	@InjectPage
	private TransferBatchConfirm transferBatchConfirm;

	@InjectComponent
	private Form form;

	@Inject
	private Messages messages;

	@Inject
	private BatchContentService batchContentService;

    @Inject
    private AccountService accountService;

    @Inject
    private TransferService transferService;

    @Property
    private String periodicTransferGroup;

    @Property
    private int transferType1;

    @Property
    private int transferType3;

    @Property
    private DayOfWeek day;
        
    private List<TransferView> transferViews;

	void onValidateFromForm() {
		/*
		if (transferBatchView.getTransferType().intValue() == Constants.TRANSFER_POSTDATE) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 1);
			if ((transferBatchView.getValueDate() == null) || (transferBatchView.getValueDate().before(DateUtils.truncate(cal.getTime())))) {
				form.recordError(messages.get("invalidDate"));
				return;
			}
		} else if (transferBatchView.getTransferType().intValue() == Constants.TRANSFER_NOW) {
			transferBatchView.setValueDate(new Date());
		}
		*/
		Batch batch = batchService.get(transferBatchView.getBatchId());
		if (batch == null) {
			form.recordError(messages.get("invalidBatch"));
			return;
		}
		transferBatchView.setBatchName(batch.getBatchName());
		transferBatchView.setBatchDescription(batch.getDescription());
		List<BatchContent> batchContentList = batchContentService.getAll(batch.getId());
		if (batchContentList.size() == 0) {
			form.recordError(messages.get("emptyBatch"));
			return;
		}

		if (transferBatchView.getTransferType().intValue() == Constants.TRANSFER_NOW) {
			transferBatchView.setValueDate(new Date());
		} else if (transferBatchView.getTransferType().intValue() == Constants.TRANSFER_POSTDATE) {
            if (transferBatchView.getValueDate() == null) {
                form.recordError(messages.get("transferDate-requiredIf-message"));
            }
        } else if (transferBatchView.getTransferType().intValue() == Constants.TRANSFER_PERIODIC) {
            if (periodicTransferGroup.equals("every")) {
                if (transferType1 == 0) {
                    form.recordError(messages.get("everyField-regexp-message"));
                }
            }
            else if (periodicTransferGroup.equals("everyDate")) {
                if (transferType3 < 1 || transferType3 > 31) {
                    form.recordError(messages.get("everyDateField-regexp-message"));
                }
            }

            if (transferBatchView.getEndDate() == null) {
                form.recordError(messages.get("endDate-requiredIf-message"));
            }
        }

    	//cek informasi saldo
    	AccountView accountView = new AccountView();
        accountView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        accountView.setCurrencyCode(Constants.CURRENCY_CODE);
        accountView.setMerchantType(sessionManager.getDefaultMerchantType());
        accountView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        accountView.setAccountNumber(transferBatchView.getAccountNumber());
        accountView = accountService.getAccountBalance(accountView);

        BigDecimal totalAmount = BigDecimal.ZERO;
        for(int i = 0; i < batchContentList.size(); i++) {
        	BatchContent content = batchContentList.get(i);
        	totalAmount = totalAmount.add(content.getAmount());
        }
        if (totalAmount.compareTo(accountView.getAvailableBalance()) > 0) {
            form.recordError(messages.get("inputAmount-notEnough-message"));
        }
        
        if (!form.getHasErrors()) {
        	transferViews = new ArrayList<TransferView>();
        	for(int i = 0; i < batchContentList.size(); i++) {
        		TransferView transferView = setTransferViewData(batchContentList.get(i));
	            try {
	                transferView.setTransactionType(com.dwidasa.engine.Constants.TRANSFER_INQ_CODE);
	                transferView = (TransferView) transferService.inquiry(transferView);
	                transferView.setTransactionType(com.dwidasa.engine.Constants.TRANSFER_CODE);
	                transferService.confirm(transferView);
	                transferViews.add(transferView);
	            } catch (BusinessException e) {
	                transferView.setTransactionType(com.dwidasa.engine.Constants.TRANSFER_CODE);
	                form.recordError(e.getFullMessage());
	                e.printStackTrace();
	            }
        	}
        }
	}
	private TransferView setTransferViewData(BatchContent batchContent) {
		TransferView transferView= new TransferView();
		transferView.setCardNumber(sessionManager.getLoggedCustomerView().getCardNumber());
        transferView.setCurrencyCode(Constants.CURRENCY_CODE);
        transferView.setTransactionDate(new Date());
        transferView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        transferView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());
        transferView.setBillerCode(cacheManager.getBillers(com.dwidasa.engine.Constants.TRANSFER_CODE).get(0).getBillerCode());
        transferView.setBillerName(cacheManager.getBillers(com.dwidasa.engine.Constants.TRANSFER_CODE).get(0).getBillerName());
        transferView.setMerchantType(sessionManager.getDefaultMerchantType());
        transferView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        transferView.setToAccountType("00");
        transferView.setInputType("M");        
        transferView.setAccountNumber(transferBatchView.getAccountNumber());
        transferView.setEndDate(transferBatchView.getEndDate());

        if (transferBatchView.getTransferType().intValue() == Constants.TRANSFER_NOW) {
			transferView.setTransferType(Constants.TRANSFER_NOW);
			transferView.setValueDate(transferBatchView.getValueDate());
		} else if (transferBatchView.getTransferType().intValue() == Constants.TRANSFER_POSTDATE) {
			transferView.setTransferType(Constants.TRANSFER_POSTDATE);
			transferView.setValueDate(transferBatchView.getValueDate());
		} else {
			transferView.setTransferType(Constants.TRANSFER_PERIODIC);
            if (periodicTransferGroup.equals("every")) {
                transferView.setPeriodType(1);
                transferView.setPeriodValue(transferType1);
            } else if (periodicTransferGroup.equals("everyDay")) {
                transferView.setPeriodType(2);
                transferView.setPeriodValue(Integer.valueOf(day.toString()));
            } else if (periodicTransferGroup.equals("everyDate")) {
                transferView.setPeriodType(3);
                transferView.setPeriodValue(transferType3);
            }
		}

        transferView.setAmount(batchContent.getAmount());
        transferView.setCustomerReference(batchContent.getAccountNumber());
        transferView.setReceiverName(batchContent.getCustomerName());
        
        return transferView;
    }

	Object onSuccessFromForm() {
		transferBatchConfirm.setTransferBatchView(transferBatchView);
		
		transferBatchConfirm.setTransferViews(transferViews);
		return transferBatchConfirm;
	}

}
