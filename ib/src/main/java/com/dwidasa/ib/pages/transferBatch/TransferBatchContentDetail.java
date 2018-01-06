package com.dwidasa.ib.pages.transferBatch;

import java.text.NumberFormat;
import java.util.List;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.dwidasa.engine.model.Batch;
import com.dwidasa.engine.model.BatchContent;
import com.dwidasa.engine.model.CustomerRegister;
import com.dwidasa.engine.model.TransferBatch;
import com.dwidasa.engine.service.BatchService;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.TransferService;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.SessionManager;

@Import(library = "context:bprks/js/transferBatch/TransferBatchContentDetail.js")
public class TransferBatchContentDetail {
	@Persist
	@Property(write = false)
	private Long batchId;

	@Property
	private Batch batch;

	@Persist
	@Property
	private BatchContent batchContent;

	@Inject
	private BatchService batchService;

    @InjectComponent
    private Form form;
    
	@Inject
	private Messages messages;

	@InjectPage
	private TransferBatchContentList batchContentList;

	@Inject
	private ThreadLocale threadLocale;

	@Property
	private NumberFormat formatter = NumberFormat.getInstance(threadLocale
			.getLocale());

	@Inject
	private SessionManager sessionManager;

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public void onActivate(Long batchId) {
		this.batchId = batchId;
	}

	Long onPassivate() {
		return batchId;
	}

	Object onActivate() {
		if (batchId == null) {
			return TransferBatchContentList.class;
		}
		return null;
	}

	void setupRender() {
		batch = batchService.get(batchId);
		batchContent = new BatchContent();
		buildTransferListModel();
		if (transferListModel.getOptions().size() > 0) {
			transferValue = "fromAccountList";
		} else {
			transferValue = "inputManual";
		}
	}
	
	private boolean isBackButton;
	
	void onSelectedFromBack() {
		isBackButton = true;
	}
	
	void onSelectedFromAdd() {
		isBackButton = false;
	}
	
	@InjectPage
	private TransferBatchContentDetailConfirm confirmPage;

    void onValidateFromForm() {
		if (batchContent.getAmount() == null) {
			form.recordError(messages.get("amount-bigdecimal-message"));
//		} else if (batchContent.getAmount()) {
//			form.recordError(messages.get("amount-bigdecimal-message"));
		} else if (batchContent.getAmount().doubleValue() <= 0) {
            form.recordError(messages.get("amount-cannotZero-message"));
        } else if (batchContent.getAmount().doubleValue() < 10000D) {
            form.recordError(messages.get("amount-minimum-message"));
        }  
    }
	@DiscardAfter
	Object onSuccessFromForm() {
		if (isBackButton) {
			batchContentList.setId(batchId);
			return batchContentList;			
		} else {
			String accNo = customerReference2.substring(0, 10);
			String customerName = customerReference2.substring(13).trim();
			batchContent.setAccountNumber(accNo);
			batchContent.setCustomerName(customerName);
			
			confirmPage.setBatchId(batchId);
			confirmPage.setBatchContent(batchContent);
			return confirmPage;
		}
	}

	@Property
	private String transferValue;

	@Property
	private String customerReference2, customerReference3;

	@Property
	private SelectModel transferListModel;

	@Inject
	private TransferService transferService;

	@Inject
	private CacheManager cacheManager;

	@Inject
	private GenericSelectModelFactory genericSelectModelFactory;

	private void buildTransferListModel() {
		List<CustomerRegister> customerRegisters = transferService
				.getRegisters(sessionManager.getLoggedCustomerView().getId(), com.dwidasa.engine.Constants.TRANSFER_CODE,
						cacheManager.getBillers(com.dwidasa.engine.Constants.TRANSFER_CODE).get(0).getBillerCode());
		transferListModel = genericSelectModelFactory.create(customerRegisters);
	}

	@Inject
	private JavaScriptSupport javaScriptSupport;

	@AfterRender
	public void afterRender() {
		javaScriptSupport.addScript("new AutoChecker();");
	}
	
	

}
