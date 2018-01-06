package com.dwidasa.engine.service.impl.facade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.BillerProductDao;
import com.dwidasa.engine.dao.ProviderProductDao;
import com.dwidasa.engine.dao.TransactionStageDao;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.BatchContent;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.TransactionData;
import com.dwidasa.engine.model.TransactionQueue;
import com.dwidasa.engine.model.TransactionStage;
import com.dwidasa.engine.model.TransferBatch;
import com.dwidasa.engine.model.TransferBatchContent;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.TransferBatchView;
import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.service.BatchContentService;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.engine.service.TransactionQueueService;
import com.dwidasa.engine.service.TransactionService;
import com.dwidasa.engine.service.TransferBatchContentService;
import com.dwidasa.engine.service.TransferBatchService;
import com.dwidasa.engine.service.facade.TransferService;
import com.dwidasa.engine.service.link.CommLink;
import com.dwidasa.engine.service.link.MxCommLink;
import com.dwidasa.engine.service.transform.TransformerFactory;
import com.dwidasa.engine.service.view.BaseViewService;
import com.dwidasa.engine.service.view.TransferViewService;
import com.dwidasa.engine.util.DateUtils;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.engine.util.MoneyUtils;
import com.dwidasa.engine.util.ReferenceGenerator;
import com.dwidasa.engine.util.ScheduleHelper;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/13/11
 * Time: 9:54 AM
 */
@Service("transferService")
public class TransferServiceImpl extends BaseTransactionServiceImpl implements TransferService {
    private static Logger logger = Logger.getLogger( TransferServiceImpl.class );
    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private TransactionStageDao transactionStageDao;

    @Autowired
    private TransactionQueueService transactionQueueService;

    @Autowired
    private BatchContentService batchContentService;

    @Autowired
    private TransferBatchService transferBatchService;

    @Autowired
    private TransferBatchContentService transferBatchContentService;

    @Autowired
    private TransactionService transactionService;
    
    @SuppressWarnings("unused")
	@Autowired
    private ProviderProductDao providerProductDao;

    @SuppressWarnings("unused")
	@Autowired
    private BillerProductDao billerProductDao;

    public TransferServiceImpl() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseView execute(BaseView view) {
        TransferView tv = (TransferView) view;

        if (tv.getTransferType() == 1) {
            return executeImmediate(tv);
        }
        else if (tv.getTransferType() == 2) {
            //-- post dated but used NOW as value date, therefore categorized as in time transfer
            if (DateUtils.truncate(tv.getValueDate()).equals(DateUtils.truncate(new Date()))) {
                tv.setTransferType(1);
                super.execute(view);
            }

            return executePostDate(tv);
        }
        else if (tv.getTransferType() == 3) {
            return executePeriodic(tv);
        }

        throw new BusinessException("IB-1017");
    }

    public BaseView executeBatch(BaseView view) {
    	TransferBatchView tbv = (TransferBatchView) view;
    	List<TransferView> transferViews = tbv.getTransferViews();
    	for (int i = 0; i < transferViews.size(); i++) {
			TransferView tv = transferViews.get(i);
	        if (tv.getTransferType() == 1) {
	            tv = (TransferView) executeImmediate(tv);
	        }
	        else if (tv.getTransferType() == 2) {
	            //-- post dated but used NOW as value date, therefore categorized as in time transfer
	            if (DateUtils.truncate(tv.getValueDate()).equals(DateUtils.truncate(new Date()))) {
	                tv.setTransferType(1);
	                tv = (TransferView) super.execute(view);
	            }
	            tv = (TransferView) executePostDate(tv);
	        }
	        else if (tbv.getTransferType() == 3) {
	        	tv = (TransferView) executePeriodic(tv);
	        }
    	}
        throw new BusinessException("IB-1017");
    }
    /**
     * Create transaction stage object.
     * @param transactionId transaction id
     * @param customerId customer id
     * @param status Constants.PENDING or Constants.DELIVERED
     * @return transaction stage object
     */
    private TransactionStage createStage(Long transactionId, Long customerId, String status) {
        TransactionStage ts = new TransactionStage();

        ts.setTransactionId(transactionId);
        ts.setStatus(status);
        ts.setCreated(new Date());
        ts.setCreatedby(customerId);
        ts.setUpdated(new Date());
        ts.setUpdatedby(customerId);

        return ts;
    }

    /**
     * Modified version of super.execute to handle deferred transfer via treasury.
     * @param tv transfer view object
     * @param deferred true means messaging to external system will be deferred to another process
     * @return base view object
     */
    private TransferView executeStage(TransferView tv, Boolean deferred) {
        String intermediaryAcc = cacheManager.getParameter("TREASURY_INTERMEDIARY_ACCOUNT").getParameterValue();

        BaseViewService viewService = getServiceObject(tv);
        viewService.validate(tv);

        Transaction transaction = TransformerFactory.getTransformer(tv)
                .transformTo(tv, new Transaction());
        viewService.composeTransaction(tv, transaction);
        transaction.setExecutionType(Constants.NOW_ET);

        if (!deferred) {
            String oAccTo = transaction.getToAccountNumber();
            transaction.setToAccountNumber(intermediaryAcc);
            CommLink link = new MxCommLink(transaction);
            link.sendMessage();
            if (transaction.getResponseCode().equals(Constants.SUCCESS_CODE)) {
                viewService.decomposeTransaction(tv, transaction);
            }
            else {
                loggingService.logFailedTransaction(transaction, tv);
                throw new BusinessException("IB-1009", transaction.getResponseCode());
            }
            tv.setPending(false);
            EngineUtils.setTransactionStatus(transaction);
            transaction.setToAccountNumber(oAccTo);
        }
        else {
            transaction.setReferenceNumber(ReferenceGenerator.generate());
            transaction.setStatus(Constants.QUEUED_STATUS);
            tv.setResponseCode(Constants.SUCCESS_CODE);
            tv.setReferenceNumber(transaction.getReferenceNumber());
            tv.setPending(true);
        }

        transactionDao.save(transaction);
        transactionDataDao.save(EngineUtils.createTransactionData(tv, transaction.getId()));

        TransactionStage stage;
        if (deferred) {
            stage = createStage(transaction.getId(), tv.getCustomerId(), Constants.PENDING_STATUS);
        }
        else {
            stage = createStage(transaction.getId(), tv.getCustomerId(), Constants.DELIVERED_STATUS);
        }

        transactionStageDao.save(stage);
        return tv;
    }

    /**
     * Execute immediate transaction and checking for other bank transfer through treasury
     * that have different mechanism.
     * @param tv transfer view
     * @return base view object
     */
    private BaseView executeImmediate(TransferView tv) {
//        if (tv.getTransactionType().equals(Constants.TRANSFER_TREASURY_CODE)) {
//            Date start = DateUtils.today(cacheManager.getParameter("TREASURY_START_TIME").getParameterValue(),
//                    "HH:mm:ss");
//            Date end =  DateUtils.today(cacheManager.getParameter("TREASURY_END_TIME").getParameterValue(),
//                    "HH:mm:ss");
//
//            if (DateUtils.inRange(tv.getTransactionDate(), start, end)) {
//                tv = executeStage(tv, false);
//            }
//            else {
//                tv = executeStage(tv, true);
//            }
//
//            return tv;
//        }

        return super.execute(tv);
    }

    /**
     * Execute post dated transaction.
     * @param tv transfer view
     * @return transfer view object
     */
    private BaseView executePostDate(TransferView tv) {
    	BaseViewService viewService = getServiceObject(tv);
        viewService.validate(tv);

        String referenceNumber = ReferenceGenerator.generate(); 
        tv.setReferenceNumber(referenceNumber);
        tv.setStatus(Constants.QUEUED_STATUS);
        TransactionQueue queue = new TransactionQueue();
		queue.setCustomerId(tv.getCustomerId());
		queue.setAccountNumber(tv.getAccountNumber());
		queue.setBillerName(tv.getBillerName());
		queue.setCustomerReference(tv.getCustomerReference());
		queue.setAmount(tv.getAmount());
		queue.setFee(tv.getFee());
		queue.setReferenceNumber(referenceNumber);
		queue.setTransactionType(tv.getTransactionType());
		queue.setExecutionType(com.dwidasa.engine.Constants.POSTDATED_ET);
		queue.setTransactionDate(tv.getTransactionDate());
		queue.setValueDate(tv.getValueDate());
		queue.setStatus(Constants.QUEUED_STATUS);
		queue.setTransactionData(PojoJsonMapper.toJson(tv));
//		queue.setDeliveryChannel(tv.getDeliveryChannel());
//		queue.setDeliveryChannelId(tv.getDeliveryChannelId());
		queue.setCreated(new Date());
		queue.setCreatedby(tv.getCustomerId());
		queue.setUpdated(new Date());
		queue.setUpdatedby(tv.getCustomerId());
		transactionQueueService.save(queue);
		tv.setResponseCode(Constants.SUCCESS_CODE);	//success save to queued
        return tv;
    }

    /**
     * Generate transactions data with specified period.
     * @param tv transfer view
     * @return transfer view object
     */
    private BaseView executePeriodic(TransferView tv) {
        BaseViewService viewService = getServiceObject(tv);
        viewService.validate(tv);
                
        List<Date> dates = null;
        if (tv.getPeriodType() == 1) {
            dates = ScheduleHelper.differenceOption(tv.getPeriodValue(), tv.getEndDate());
        }
        else if (tv.getPeriodType() == 2) {
            dates = ScheduleHelper.dayOption(tv.getPeriodValue(), tv.getEndDate());
        }
        else if (tv.getPeriodType() == 3) {
            dates = ScheduleHelper.dateOption(tv.getPeriodValue(), tv.getEndDate());
        }

        if (dates != null && dates.size() > 0) {
            Transaction transaction = TransformerFactory.getTransformer(tv)
                    .transformTo(tv, new Transaction());
            viewService.composeTransaction(tv, transaction);
            transaction.setExecutionType(Constants.PERIODIC_ET);
            String referenceNumber = ReferenceGenerator.generate(); 
            tv.setReferenceNumber(referenceNumber);
            tv.setStatus(Constants.QUEUED_STATUS);

            for (Date d : dates) {
            	/*
                transaction.setValueDate(d);
                transactionDao.save(transaction);
                transaction.setId(null);
                */
                
                TransactionQueue queue = new TransactionQueue();
        		queue.setCustomerId(tv.getCustomerId());
        		queue.setAccountNumber(tv.getAccountNumber());
        		queue.setBillerName(tv.getBillerName());
        		queue.setCustomerReference(tv.getCustomerReference());
        		queue.setAmount(tv.getAmount());
        		queue.setFee(tv.getFee());
        		queue.setReferenceNumber(referenceNumber);
        		queue.setTransactionType(tv.getTransactionType());
        		queue.setExecutionType(com.dwidasa.engine.Constants.PERIODIC_ET);
        		queue.setTransactionDate(d);
        		queue.setValueDate(d);
        		queue.setStatus(Constants.QUEUED_STATUS);
        		queue.setTransactionData(PojoJsonMapper.toJson(tv));
//        		queue.setDeliveryChannel(tv.getDeliveryChannel());
//        		queue.setDeliveryChannelId(tv.getDeliveryChannelId());
        		queue.setCreated(new Date());
        		queue.setCreatedby(tv.getCustomerId());
        		queue.setUpdated(new Date());
        		queue.setUpdatedby(tv.getCustomerId());
        		transactionQueueService.save(queue);                
            }
        }
        tv.setResponseCode(Constants.SUCCESS_CODE);
        return tv;
    }

    /**
     * {@inheritDoc}
     */
    public List<Transaction> getAll(String status, Date valueDate) {
        return transactionDao.getAll(status, valueDate);
    }

    /**
     * {@inheritDoc}
     */
    public TransferView execute(Transaction transaction) {
        TransferViewService viewService = (TransferViewService) ServiceLocator.getService("transferViewService");
        TransactionData td = transactionDataDao.getByTransactionFk(transaction.getId());
        TransferView view = (TransferView) EngineUtils.deserialize(td);

        CommLink link = new MxCommLink(transaction);
        link.sendMessage();
        if (transaction.getResponseCode().equals(Constants.SUCCESS_CODE)) {
            viewService.decomposeTransaction(view, transaction);
        }

        EngineUtils.setTransactionStatus(transaction);
        transactionDao.save(transaction);

        td.setTransactionData(PojoJsonMapper.toJson(view));
        td.setUpdated(new Date());
        transactionDataDao.save(td);

        if (view.getProviderCode().equals(Constants.TREASURY_PROVIDER_CODE)) {

            TransactionStage ts = transactionStageDao.getByTransactionFk(transaction.getId());
            ts.setStatus(Constants.DELIVERED_STATUS);
            ts.setUpdated(new Date());

            transactionStageDao.save(ts);
        }

        return view;
    }

    @Override
    public TransferView inquiryATMB(TransferView view) {
        BaseViewService viewService = getServiceObject(view);
        viewService.preProcess(view);
        
        Transaction transaction = TransformerFactory.getTransformer(view).transformTo(view, new Transaction());

        viewService.composeInquiry(view, transaction);

        CommLink link = new MxCommLink(transaction);
        link.sendMessage();

        if (transaction.getResponseCode().equals(Constants.SUCCESS_CODE)) {
            viewService.decomposeInquiry(view, transaction);
        }
        else {
            transaction.setResponseCode(getMappedResponseCode(transaction.getResponseCode(), Constants.ATMB.TT_INQUIRY));
            throw new BusinessException("IB-1009", transaction.getResponseCode());
        }

        EngineUtils.setTransactionStatus(transaction);
        return view;
    }

	private void checkTransferAmount(Transaction transaction) {
//		if (transaction.getTransactionAmount().compareTo(Constants.TRANSFER_AMOUNT.MIN_PER_TRX) < 0) {
//			throw new BusinessException("IB-1009", "AP", MoneyUtils.getMoney(Constants.TRANSFER_AMOUNT.MIN_PER_TRX));
//		} else if (transaction.getTransactionAmount().compareTo(Constants.TRANSFER_AMOUNT.MAX_PER_TRX) > 0) {
//			throw new BusinessException("IB-1009", "AQ", MoneyUtils.getMoney(Constants.TRANSFER_AMOUNT.MAX_PER_TRX));
//		} 
		BigDecimal totalInOneDay = transactionService.getTotalTransferInOneDay(transaction.getFromAccountNumber(), transaction.getToAccountNumber(), transaction.getTransactionDate());
		BigDecimal totalAll = totalInOneDay.add(transaction.getTransactionAmount());
		if (totalAll.compareTo(Constants.TRANSFER_AMOUNT.MAX_IN_ONE_DAY) == 1) {
			throw new BusinessException("IB-1009", "AR", MoneyUtils.getMoney(Constants.TRANSFER_AMOUNT.MAX_IN_ONE_DAY), transaction.getToAccountNumber(), MoneyUtils.getMoney(totalInOneDay));
		}
	}

    @Override
    public TransferView executeATMB(TransferView view) {
        BaseViewService viewService = getServiceObject(view);
        viewService.validate(view);

        Transaction transaction = TransformerFactory.getTransformer(view).transformTo(view, new Transaction());

        //cek maximal transfer tiap hari
        checkTransferAmount(transaction);
        
        viewService.composeTransaction(view, transaction);
        transaction.setExecutionType(Constants.NOW_ET);

        CommLink link = new MxCommLink(transaction);
        link.sendMessage();
        //transaction.setStatus("SUCCEED");   //karena untuk menyimpan custRefAtmb

        if (transaction.getResponseCode().equals(Constants.SUCCESS_CODE))
        {
            transaction.setStatus(Constants.SUCCEED_STATUS);
            viewService.decomposeTransaction(view, transaction);
        }
        else {
            view.setBit42(transaction.getBit42());

            loggingService.logFailedTransaction(transaction, view);

            transaction.setResponseCode(getMappedResponseCode(transaction.getResponseCode(), Constants.ATMB.TT_POSTING));
            
            if (transaction.getResponseCode().equals("AH"))
            {
            	transaction.setStatus(Constants.PENDING_STATUS);
                String bit48 = transaction.getFreeData1();
                view.setSenderName(bit48.substring(132, 162).trim());
                view.setNews(bit48.substring(162, 192).trim());
                view.setResponseCode(transaction.getResponseCode());
                //throw new BusinessException("IB-1009", new Object[] { transaction.getResponseCode(), transaction.getReferenceNumber(), view.getSenderName(), new Date()}); //Sisi Mobile Diaktifkan
            }
            else {
                throw new BusinessException("IB-1009", transaction.getResponseCode());
            }
        }

        EngineUtils.setTransactionStatus(transaction);
        transactionDao.save(transaction);   //disimpan di IsoHelper
        view.setTransactionStatus(transaction.getStatus());
        view.setBit42(transaction.getBit42());
       
        TransactionData transactionData = EngineUtils.createTransactionData(view, transaction.getId());
        transactionDataDao.save(transactionData);
        
        mailer.sendTransactionMessage(view.getClass().getSimpleName()+".vm", view);
        view.setTransactionDate(transaction.getTransactionDate());
        view.setValueDate(transaction.getValueDate());
        return view;
    }

	@Override
	public TransferView inquiryALTO(TransferView view) {
		return inquiryATMB(view);
	}

	@Override
	public TransferView executeALTO(TransferView view) {
		return executeATMB(view);
	}

    private String getMappedResponseCode(String responseCode, String transactionType) {
//    	//tidak dipakai, karena menggunakan IGATE bukan MX
//        if (transactionType.equals(Constants.ATMB.TT_INQUIRY)) {
//            if (responseCode.equals("08") || responseCode.equals("12")) return "AG";
//        } else if (transactionType.equals(Constants.ATMB.TT_POSTING)) {
//            if (responseCode.equals("08") || responseCode.equals("94")) return "AH";
//        }
//        if (responseCode.equals("20")) return "AA";
//        else if (responseCode.equals("23")) return "AB";
//        else if (responseCode.equals("76")) return "AC";
//        else if (responseCode.equals("01")) return "AD";
//        else if (responseCode.equals("02")) return "AE";
//        else if (responseCode.equals("92")) return "AF";
//        else if (responseCode.equals("04")) return "AI";
//        else if (responseCode.equals("75")) return "AJ";
//        else if (responseCode.equals("41")) return "AK";
        return responseCode;
    }

	@Override
	public BaseView executeNow(TransferView tv) {
		return super.execute(tv);
	}

	@Override
	public TransferBatchView inquiryBatch(TransferBatchView view) {
		List<BatchContent> batchContentList = batchContentService.getAll(view.getBatchId());

		BigDecimal totalAmount = BigDecimal.ZERO;
        for(int i = 0; i < batchContentList.size(); i++) {
        	BatchContent content = batchContentList.get(i);
        	totalAmount = totalAmount.add(content.getAmount());
        }
        view.setAmount(totalAmount);
        List<TransferView>	transferViews = new ArrayList<TransferView>();
    	for(int i = 0; i < batchContentList.size(); i++) {
    		TransferView transferView = setTransferViewData(batchContentList.get(i), view);
            transferView.setTransactionType(com.dwidasa.engine.Constants.TRANSFER_INQ_CODE);
            inquiry(transferView);
            transferView.setTransactionType(com.dwidasa.engine.Constants.TRANSFER_CODE);
            confirm(transferView);
            transferViews.add(transferView);
    	}
		view.setTransferViews(transferViews);
		return view;
	}

	@Override
	public TransferBatchView executeBatch(TransferBatchView view) {
		List<TransferView> transferViews = view.getTransferViews();
		List<TransferBatchContent> transferBatchContentList = new ArrayList<TransferBatchContent>();
		for(int i = 0; i < transferViews.size(); i++) {
			TransferView transferView = transferViews.get(i);
        	TransferBatchContent transferBatchContent = new TransferBatchContent();
			transferBatchContent.setAccountNumber(transferView.getCustomerReference());
			transferBatchContent.setCustomerName(transferView.getReceiverName());
			transferBatchContent.setAmount(transferView.getAmount());
        	transferBatchContent.setStatus(com.dwidasa.engine.Constants.PENDING_STATUS);
        	transferBatchContentList.add(transferBatchContent);
		}
		TransferBatch transferBatch = new TransferBatch();
		transferBatch.setTotalAmount(view.getAmount());
    	transferBatch.setTransferBatchContentList(transferBatchContentList);
    	transferBatch.setStatus(com.dwidasa.engine.Constants.PENDING_STATUS);
    	transferBatch.setCustomerId(view.getCustomerId());
    	transferBatch.setTransactionDate(new Date());
    	TransferView firstTransferView = transferViews.get(0);
    	transferBatch.setTransferType(firstTransferView.getTransferType());
    	List<Date> dates = null;
		if (firstTransferView.getTransferType() == 1) {
			transferBatch.setValueDate(transferBatch.getTransactionDate());
		} else if (firstTransferView.getTransferType() == 2) {
			transferBatch.setValueDate(firstTransferView.getValueDate());
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
	        	transferBatch.setValueDate(dates.get(0));
	        }
		}
		transferBatch.setReferenceNumber(ReferenceGenerator.generate());
		transferBatch.setCreated(new Date());
		transferBatch.setCreatedby(view.getCustomerId());
		transferBatch.setUpdated(new Date());
		transferBatch.setUpdatedby(view.getCustomerId());
    	transferBatch = (TransferBatch) transferBatchService.save(transferBatch);
    	
    	boolean allIsSuccess = true;
    	for(int i = 0; i < transferViews.size(); i++) {
    		TransferView transferView = transferViews.get(i);
    		TransferBatchContent transferBatchContent = transferBatchContentList.get(i);
    		transferBatchContent.setTransferBatchId(transferBatch.getId());
    		transferBatchContent.setValueDate(transferBatch.getValueDate());
    		transferBatchContent.setCreated(new Date());
    		transferBatchContent.setCreatedby(view.getCustomerId());
    		transferBatchContent.setUpdated(new Date());
    		transferBatchContent.setUpdatedby(view.getCustomerId());
    		
            try {	 
            	execute(transferView);
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
            		saveTransferBatch(transferBatch, firstTransferView, dates, allIsSuccess);
            	}
            } catch (BusinessException e) {
        		transferBatchContent.setStatus(com.dwidasa.engine.Constants.FAILED_STATUS);
        		transferBatchContentService.save(transferBatchContent);
        		allIsSuccess = false;
        		
        		saveTransferBatch(transferBatch, firstTransferView, dates, allIsSuccess);
        		
        		throw e;
            }
    	}
    	return view;
	}

	private void saveTransferBatch(TransferBatch transferBatch, TransferView firstTransferView, List<Date> dates, boolean allIsSuccess) {
		if (firstTransferView.getTransferType() == 1) {
    		transferBatch.setStatus(allIsSuccess ? com.dwidasa.engine.Constants.SUCCEED_STATUS : com.dwidasa.engine.Constants.FAILED_STATUS);
		} else if (firstTransferView.getTransferType() == 2) {
			if (DateUtils.truncate(firstTransferView.getValueDate()).equals(DateUtils.truncate(new Date()))) {
				transferBatch.setStatus(allIsSuccess ? com.dwidasa.engine.Constants.SUCCEED_STATUS : com.dwidasa.engine.Constants.FAILED_STATUS);
	        } else {
	        	transferBatch.setStatus(allIsSuccess ? com.dwidasa.engine.Constants.PENDING_STATUS : com.dwidasa.engine.Constants.FAILED_STATUS);
	        }				
		} else if (firstTransferView.getTransferType() == 3) {
	        if (dates != null && dates.size() > 0) {
	        	if (DateUtils.truncate(dates.get(0)).equals(DateUtils.truncate(new Date()))) {
					transferBatch.setStatus(allIsSuccess ? com.dwidasa.engine.Constants.SUCCEED_STATUS : com.dwidasa.engine.Constants.FAILED_STATUS);
		        } else {
		        	transferBatch.setStatus(allIsSuccess ? com.dwidasa.engine.Constants.PENDING_STATUS : com.dwidasa.engine.Constants.FAILED_STATUS);
		        }
	        }
		}
    	transferBatchService.save(transferBatch);
	}
	private TransferView setTransferViewData(BatchContent batchContent, TransferBatchView transferBatchView ) {
		TransferView transferView= new TransferView();
		transferView.setCardNumber(transferBatchView.getCardNumber());
        transferView.setCurrencyCode(Constants.CURRENCY_CODE);
        transferView.setTransactionDate(new Date());
        transferView.setCustomerId(transferBatchView.getCustomerId());
        transferView.setAccountType(transferBatchView.getAccountType());
        transferView.setBillerCode(cacheManager.getBillers(com.dwidasa.engine.Constants.TRANSFER_CODE).get(0).getBillerCode());
        transferView.setBillerName(cacheManager.getBillers(com.dwidasa.engine.Constants.TRANSFER_CODE).get(0).getBillerName());
        transferView.setMerchantType(transferBatchView.getMerchantType());
        transferView.setTerminalId(transferBatchView.getTerminalId());
        transferView.setToAccountType("00");
        transferView.setInputType("M");        
        transferView.setAccountNumber(transferBatchView.getAccountNumber());
        transferView.setEndDate(transferBatchView.getEndDate());
        transferView.setTransferType(transferBatchView.getTransferType());
        transferView.setPeriodType(transferBatchView.getPeriodType());
        transferView.setPeriodValue(transferBatchView.getPeriodValue());
        
        transferView.setAmount(batchContent.getAmount());
        transferView.setCustomerReference(batchContent.getAccountNumber());
        transferView.setReceiverName(batchContent.getCustomerName());
        
        return transferView;
    }

}
