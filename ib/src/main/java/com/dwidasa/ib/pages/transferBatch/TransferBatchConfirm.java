package com.dwidasa.ib.pages.transferBatch;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.dwidasa.engine.model.TransferBatch;
import com.dwidasa.engine.model.TransferBatchContent;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.TransferBatchContentService;
import com.dwidasa.engine.service.TransferBatchService;
import com.dwidasa.engine.service.facade.TransferService;
import com.dwidasa.engine.util.DateUtils;
import com.dwidasa.engine.util.ReferenceGenerator;
import com.dwidasa.engine.util.ScheduleHelper;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

public class TransferBatchConfirm {
	@Persist
	@Property(write = false)
	private TransferBatch transferBatchView;

	public void setTransferBatchView(TransferBatch transferBatchView) {
		this.transferBatchView = transferBatchView;
	}
	@Persist
	@Property(write = false)
	private List<TransferView> transferViews;

	public void setTransferViews(List<TransferView> transferViews) {
		this.transferViews = transferViews;
	}
	@Property
	private int tokenType;

	@Property
	private TokenView tokenView;

	@Inject
	private ThreadLocale threadLocale;

	@Property
	private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Inject
    private TransferService transferService;

    @Inject
    private CacheManager cacheManager;
    
	void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }
	
	Object onActivate() {
		if (transferBatchView == null) {
			return TransferBatchInput.class;
		}
		return null;
	}
	
	@Persist
	@Property
	private Batch batch;
	
	void setupRender() {
		setTokenType();
	}
	
	@Inject
	private Messages messages;
	
	public String getStrTransferType(Integer transferType) {
		if (transferType == null) return "-";
		if (transferType.intValue() == Constants.TRANSFER_NOW) {
			return messages.get("transferType"+transferType);
		} else if (transferType.intValue() == Constants.TRANSFER_POSTDATE) {
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.MEDIUM_FORMAT, threadLocale.getLocale());
			return String.format(messages.get("transferType"+transferType), sdf.format(transferBatchView.getValueDate()));
		} else if (transferType.intValue() == Constants.TRANSFER_PERIODIC) {
			String transferPeriodic = "";
			transferPeriodic = messages.get("transferType" + transferType);
			
			return transferPeriodic;
		}
		return "-";
	}
	
	@Property
	private TransferBatchContent row;

	public int getPageSize() {
		 return Constants.PAGE_SIZE;
	}
	
    @Inject
    private SessionManager sessionManager;
    
    @InjectComponent
    private Form form;

	@Inject
	private TransferBatchService transferBatchService;
	
	@Inject
	private TransferBatchContentService transferBatchContentService;

    @Inject
    private OtpManager otpManager;
    
    void onValidateFromForm() {
    	if (otpManager.validateToken(sessionManager.getLoggedCustomerView().getId(), this.getClass().getSimpleName(), tokenView)) {
    		
    		List<TransferBatchContent> transferBatchContentList = new ArrayList<TransferBatchContent>();
    		BigDecimal totalAmount = BigDecimal.ZERO;
    		for(int i = 0; i < transferViews.size(); i++) {
    			TransferView transferView = transferViews.get(i);
            	totalAmount = totalAmount.add(transferView.getAmount());
            	
            	TransferBatchContent transferBatchContent = new TransferBatchContent();
    			transferBatchContent.setAccountNumber(transferView.getCustomerReference());
    			transferBatchContent.setCustomerName(transferView.getReceiverName());
    			transferBatchContent.setAmount(transferView.getAmount());
            	transferBatchContent.setStatus(com.dwidasa.engine.Constants.PENDING_STATUS);
            	transferBatchContentList.add(transferBatchContent);
    		}
    		transferBatchView.setTotalAmount(totalAmount);
	    	transferBatchView.setTransferBatchContentList(transferBatchContentList);
	    	transferBatchView.setStatus(com.dwidasa.engine.Constants.PENDING_STATUS);
	    	transferBatchView.setCustomerId(sessionManager.getLoggedCustomerPojo().getId());
	    	transferBatchView.setTransactionDate(new Date());
	    	TransferView firstTransferView = transferViews.get(0);
	    	transferBatchView.setTransferType(firstTransferView.getTransferType());
	    	List<Date> dates = null;
			if (firstTransferView.getTransferType() == 1) {
				transferBatchView.setValueDate(transferBatchView.getTransactionDate());
			} else if (firstTransferView.getTransferType() == 2) {
				transferBatchView.setValueDate(firstTransferView.getValueDate());
			} else if (firstTransferView.getTransferType() == 3) {
		        if (firstTransferView.getPeriodType() == 1) {
		            dates = ScheduleHelper.differenceOption(firstTransferView.getPeriodValue(), firstTransferView.getEndDate());
		        }
		        else if (firstTransferView.getPeriodType() == 2) {
		            dates = ScheduleHelper.dayOption(firstTransferView.getPeriodValue(), firstTransferView.getEndDate());
		        }
		        else if (firstTransferView.getPeriodType() == 3) {
		            dates = ScheduleHelper.dateOption(firstTransferView.getPeriodValue(), firstTransferView.getEndDate());
		        }

		        if (dates != null && dates.size() > 0) {
		        	transferBatchView.setValueDate(dates.get(0));
		        }
			}
			transferBatchView.setReferenceNumber(ReferenceGenerator.generate());
			transferBatchView.setCreated(new Date());
			transferBatchView.setCreatedby(sessionManager.getLoggedCustomerPojo().getId());
			transferBatchView.setUpdated(new Date());
			transferBatchView.setUpdatedby(sessionManager.getLoggedCustomerPojo().getId());
	    	transferBatchView = (TransferBatch) transferBatchService.save(transferBatchView);
	    	
	    	boolean allIsSuccess = true;
	    	for(int i = 0; i < transferViews.size(); i++) {
	    		TransferView transferView = transferViews.get(i);
	    		TransferBatchContent transferBatchContent = transferBatchContentList.get(i);
	    		transferBatchContent.setTransferBatchId(transferBatchView.getId());
	    		transferBatchContent.setValueDate(transferBatchView.getValueDate());
	    		transferBatchContent.setCreated(new Date());
	    		transferBatchContent.setCreatedby(sessionManager.getLoggedCustomerPojo().getId());
	    		transferBatchContent.setUpdated(new Date());
	    		transferBatchContent.setUpdatedby(sessionManager.getLoggedCustomerPojo().getId());
	            try {	            	
	            	transferView = (TransferView) transferService.execute(transferView);
	            	if (transferView.getResponseCode().equals(Constants.SUCCESS_CODE)) {
	            		if (firstTransferView.getTransferType() == 1) {
	            			transferBatchContent.setStatus(com.dwidasa.engine.Constants.SUCCEED_STATUS);
	            		} else if (firstTransferView.getTransferType() == 2) {
	        				if (DateUtils.truncate(firstTransferView.getValueDate()).equals(DateUtils.truncate(new Date()))) {
	        					transferBatchContent.setStatus(com.dwidasa.engine.Constants.SUCCEED_STATUS);
	        		        }				
	        			} else if (firstTransferView.getTransferType() == 3) {
	        		        if (dates != null && dates.size() > 0) {
	        		        	if (DateUtils.truncate(dates.get(0)).equals(DateUtils.truncate(new Date()))) {
	        		        		transferBatchContent.setStatus(com.dwidasa.engine.Constants.SUCCEED_STATUS);
	        			        }
	        		        }
	        			}
	            		transferBatchContentService.save(transferBatchContent);
	            	}
	            } catch (BusinessException e) {
	            	form.recordError(e.getFullMessage());
	                e.printStackTrace();
	                
            		transferBatchContent.setStatus(com.dwidasa.engine.Constants.FAILED_STATUS);
            		transferBatchContentService.save(transferBatchContent);
            		allIsSuccess = false;
	            }
	    	}
	    	if (firstTransferView.getTransferType() == 1) {
	    		transferBatchView.setStatus(allIsSuccess ? com.dwidasa.engine.Constants.SUCCEED_STATUS : com.dwidasa.engine.Constants.FAILED_STATUS);
			} else if (firstTransferView.getTransferType() == 2) {
				if (DateUtils.truncate(firstTransferView.getValueDate()).equals(DateUtils.truncate(new Date()))) {
					transferBatchView.setStatus(allIsSuccess ? com.dwidasa.engine.Constants.SUCCEED_STATUS : com.dwidasa.engine.Constants.FAILED_STATUS);
		        } else {
		        	transferBatchView.setStatus(allIsSuccess ? com.dwidasa.engine.Constants.PENDING_STATUS : com.dwidasa.engine.Constants.FAILED_STATUS);
		        }				
			} else if (firstTransferView.getTransferType() == 3) {
		        if (dates != null && dates.size() > 0) {
		        	if (DateUtils.truncate(dates.get(0)).equals(DateUtils.truncate(new Date()))) {
						transferBatchView.setStatus(allIsSuccess ? com.dwidasa.engine.Constants.SUCCEED_STATUS : com.dwidasa.engine.Constants.FAILED_STATUS);
			        } else {
			        	transferBatchView.setStatus(allIsSuccess ? com.dwidasa.engine.Constants.PENDING_STATUS : com.dwidasa.engine.Constants.FAILED_STATUS);
			        }
		        }
			}
	    	transferBatchService.save(transferBatchView);
    	}
    }

    @InjectPage
    private TransferBatchReceipt transferBatchReceipt;

    @DiscardAfter
    Object onSuccessFromForm() {
    	TransferView firstTransferView = transferViews.get(0);
    	if (firstTransferView.getTransferType().equals(Constants.TRANSFER_POSTDATE) || firstTransferView.getTransferType().equals(Constants.TRANSFER_PERIODIC)) {
    		if (firstTransferView.getResponseCode() != null && firstTransferView.getResponseCode().equals(Constants.SUCCESS_CODE)) {
    			transferBatchView.setStatus(com.dwidasa.engine.Constants.PENDING_STATUS);
    		} else {
        		transferBatchView.setStatus(com.dwidasa.engine.Constants.FAILED_STATUS);
        	}
    	} else {
    		if (firstTransferView.getResponseCode() != null && firstTransferView.getResponseCode().equals(Constants.SUCCESS_CODE)) {
    			transferBatchView.setStatus(com.dwidasa.engine.Constants.SUCCEED_STATUS);    	
        	} else {
        		transferBatchView.setStatus(com.dwidasa.engine.Constants.FAILED_STATUS);
        	}
    	}
    		
    	transferBatchView.setTransactionDate(firstTransferView.getTransactionDate());
    	
    	//simpan di t_transaction_batch dan content
    	
//    	transferBatchView.setTotalAmount(totalAmount);
//    	transferBatchService.saveWithContent(transferBatchView);
    	
        transferBatchReceipt.setTransferBatchView(transferBatchView);
        return transferBatchReceipt;
    }

    void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }

}
