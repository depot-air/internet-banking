package com.dwidasa.ib.pages.transferBatch;

import java.util.Date;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.dwidasa.engine.model.Batch;
import com.dwidasa.engine.service.BatchService;
import com.dwidasa.engine.service.TransactionTypeService;
import com.dwidasa.ib.services.SessionManager;

public class TransferBatchDetail {
	@Inject
	private JavaScriptSupport javaScriptSupport;

    @Property
    private Batch batch;

    @Inject
    private BatchService batchService;

    @Inject
    private Messages messages;

    @Persist
    @Property(write = false)
    private Long id;

    @Inject
    private SessionManager sessionManager;
    
    @Inject
    private TransactionTypeService transactionTypeService;
    
    @Property
    private SelectModel transactionTypeModel;

	void onActivate(Long id) {
        this.id = id;
    }

    void onPrepare() {
        if (id != null) {
            batch = batchService.get(id);
        }
        else {
            batch = new Batch();
        }
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubtitle() {
        if (id != null) {
            return messages.get("subtitle-edit");
        }

        return messages.get("subtitle-add");
    }

    public String getAction() {
        if (id != null) {
            return messages.get("edit");
        }

        return messages.get("add");
    }
    
    
    @InjectPage
    private TransferBatchContentList transferBatchContentList;
    
    private boolean isBackButton;
	
	void onSelectedFromBack() {
		isBackButton = true;
	}
	
	void onSelectedFromAdd() {
		isBackButton = false;
	}    

    @DiscardAfter
    Object onSuccessFromForm() {
    	if (isBackButton) {
    		 return TransferBatchList.class;
    	} else {
	        if (batch.getId() == null) {
	            Long userId = sessionManager.getLoggedCustomerView().getId();
	            batch.setCustomerId(userId);
	            batch.setCreated(new Date());
	            batch.setCreatedby(userId);
	            batch.setUpdated(new Date());
	            batch.setUpdatedby(userId);
	        }
	
	        batchService.save(batch);
	        transferBatchContentList.setId(batch.getId());
	        return transferBatchContentList;
    	}
    }
}
