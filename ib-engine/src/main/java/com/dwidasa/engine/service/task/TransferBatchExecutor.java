package com.dwidasa.engine.service.task;

import java.util.List;
import java.util.concurrent.Callable;

import com.dwidasa.engine.model.TransferBatch;
import com.dwidasa.engine.model.TransferBatchContent;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.CustomerService;
import com.dwidasa.engine.service.TransferBatchContentService;
import com.dwidasa.engine.service.TransferBatchService;
import com.dwidasa.engine.service.facade.TransferService;
//import com.dwidasa.engine.dao.PortfolioAccountDao;
//import com.dwidasa.engine.model.view.AccountInfo;

public class TransferBatchExecutor implements Callable<Void>{
	private Long transferBatchId;
	private String deliveryChannelId;
	
	private TransferBatchService transferBatchService;
	private TransferBatchContentService transferBatchContentService;
    private TransferService transferService;
//	private PortfolioAccountDao portfolioAccountDao;
	private CacheManager cacheManager;
	private CustomerService customerService;
	
	public TransferBatchExecutor(Long transferBatchId, String deliveryChannelId,
			TransferBatchService transferBatchService, TransferBatchContentService transferBatchContentService, 
			TransferService transferService, CacheManager cacheManager, CustomerService customerService) {
		this.transferBatchId = transferBatchId;
		this.deliveryChannelId = deliveryChannelId;
		this.transferBatchService = transferBatchService;
		this.transferBatchContentService = transferBatchContentService;
		this.transferService = transferService;
//		this.portfolioAccountDao = portfolioAccountDao;
		this.cacheManager = cacheManager;
		this.customerService = customerService;
	}
	
	@Override
	public Void call() throws Exception {
		TransferBatch transferBatch = transferBatchService.getWithContent(transferBatchId);
//		AccountInfo accountInfo = portfolioAccountDao.getAccountInfo(transferBatch.getAccountNumber(), transferBatch.getCustomerId());
		List<TransferBatchContent> contentList = transferBatch.getTransferBatchContentList();
/*		String cardNumber = customerService.getRegistrationCardNumber(transferBatch.getCustomerId());
		boolean allSuccess = true;
		boolean allFail = true;
		boolean adaPending = false;
		for (TransferBatchContent content : contentList) {
			TransferView transferView = new TransferView();
//			transferView.setBatchProcess(true);
			transferView.setCurrencyCode(Constants.CURRENCY_CODE);
	        transferView.setTransactionDate(new Date());
	        transferView.setCustomerId(transferBatch.getCustomerId());
//	        transferView.setDeliveryChannel(deliveryChannel);
//	        transferView.setDeliveryChannelId(deliveryChannelId);
	        transferView.setCardNumber(cardNumber);
	        transferView.setAccountType(accountInfo.getAccountTypeForIso());
	        transferView.setBillerCode(cacheManager.getBillerCodeList(Constants.TRANSFER_CODE).get(0));
	        transferView.setMerchantType(com.dwidasa.engine.sessionManager.getMerchantType()_IB);
	        transferView.setToAccountType("00");
	        
	        transferView.setTransferType(com.dwidasa.engine.Constants.TRANSFER_NOW);
	        transferView.setDescription(transferBatch.getBatchDescription());
	        transferView.setAmount(content.getAmount());
	        transferView.setCustomerReference(content.getAccountNumber());
	        transferView.setValueDate(new Date());
	        transferView.setTerminalId(Constants.TERMINAL_ID);
	        transferView.setBillerName(Constants.BANK_NAME);
	        transferView.setAccountNumber(transferBatch.getAccountNumber());
	        transferView.setTransactionType(com.dwidasa.engine.Constants.TRANSFER_CODE);
	        transferView.setReceiverName(content.getCustomerName());
			
	        transferView = (TransferView) transferService.execute(transferView);
	        if (transferView.getResponseCode() == null) {
                transferBatchContentService.updateStatus(content.getId(), Constants.FAILED_STATUS);
                allSuccess = false;
            } else if (transferView.getResponseCode().equals(Constants.SUCCESS_CODE)) {
            	transferBatchContentService.updateStatus(content.getId(), Constants.SUCCEED_STATUS);
            	allFail = false;
            } else if (transferView.getResponseCode().equals(Constants.TIMEOUT_CODE)) {
            	transferBatchContentService.updateStatus(content.getId(), Constants.PENDING_STATUS);
            	allSuccess = false;
            	allFail = false;
            	adaPending = true;
            } else {
            	transferBatchContentService.updateStatus(content.getId(), Constants.FAILED_STATUS);
            	allSuccess = false;
            }
		}
		if (allSuccess) {
			transferBatchService.updateStatus(transferBatch.getId(), Constants.SUCCEED_STATUS);
		} else if (allFail) {
			transferBatchService.updateStatus(transferBatch.getId(), Constants.FAILED_STATUS);
		} else if (adaPending) {
			transferBatchService.updateStatus(transferBatch.getId(), Constants.PENDING_STATUS);
		} else {
			transferBatchService.updateStatus(transferBatch.getId(), Constants.PARTIAL_STATUS);
		}
*/		
		return null;
	}

}

