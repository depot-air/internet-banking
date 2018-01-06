package com.dwidasa.ib.pages.transferBatch;

import java.text.NumberFormat;
import java.util.Date;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.Batch;
import com.dwidasa.engine.model.BatchContent;
import com.dwidasa.engine.model.TransferBatch;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.BatchContentService;
import com.dwidasa.engine.service.BatchService;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

public class TransferBatchContentDetailConfirm {
	@Persist
	@Property(write = false)
	private Long batchId;

	@Property
	private Batch batch;

	@Persist
	@Property(write = false)
	private BatchContent batchContent;

	@Inject
	private BatchService batchService;

	@Inject
	private BatchContentService batchContentService;

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
	
	public void setBatchContent(BatchContent batchContent) {
		this.batchContent = batchContent;
	}

	public void onActivate(Long batchId) {
		this.batchId = batchId;
	}

	Long onPassivate() {
		return batchId;
	}

	Object onActivate() {
		if (batchId == null) {
			return TransferBatch.class;
		}
		return null;
	}

	void setupRender() {
		batch = batchService.get(batchId);
		setTokenType();
	}

	@DiscardAfter
	Object onSuccessFromForm() {
		Long userId = sessionManager.getLoggedCustomerView().getId();
		batchContent.setCreated(new Date());
		batchContent.setCreatedby(userId);
		batchContent.setUpdated(new Date());
		batchContent.setUpdatedby(userId);
		batchContent.setBatchId(batchId);
		batchContentService.save(batchContent);
		batchContentList.setId(batchId);
		
		//reset token agar bisa dikirim lagi
    	sessionManager.setSmsTokenSent(false);

		return batchContentList;
	}

	@Property
    private int tokenType;
	
	@Property
    private TokenView tokenView;
	
	void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
            tokenView.setChallenge(batchContent.getAccountNumber());
        }
    } 

    @Inject
    private OtpManager otpManager;
    
    @InjectComponent
    private Form form;

	void onValidateFromForm() {
		//validate token
        try {
        	Boolean result = otpManager.validateToken(sessionManager.getLoggedCustomerView().getId(), this.getClass().getSimpleName(), tokenView);
        	System.out.println("resultnya : "+result);
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
        }
    }

	void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }

}
