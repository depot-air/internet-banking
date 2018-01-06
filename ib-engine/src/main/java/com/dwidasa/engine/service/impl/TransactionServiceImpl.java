package com.dwidasa.engine.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.TransactionDao;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.TransactionQueue;
import com.dwidasa.engine.model.TransferBatch;
import com.dwidasa.engine.model.TransferBatchContent;
import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.service.TransactionQueueService;
import com.dwidasa.engine.service.TransactionService;
import com.dwidasa.engine.service.TransferBatchContentService;
import com.dwidasa.engine.service.TransferBatchService;
import com.dwidasa.engine.service.facade.TransferService;
import com.dwidasa.engine.service.task.ExecutorPool;
import com.dwidasa.engine.util.DateUtils;
import com.dwidasa.engine.util.ReferenceGenerator;
import com.dwidasa.engine.util.ScheduleHelper;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/23/11
 * Time: 8:36 PM
 */
@Service("transactionService")
public class TransactionServiceImpl extends GenericServiceImpl<Transaction, Long> implements TransactionService {
    
	private static Logger logger = Logger.getLogger( TransactionServiceImpl.class );
	private TransactionDao transactionDao;

	@Autowired
	private TransactionQueueService queueService;

	@Autowired
	private TransferBatchService batchService;

	@Autowired
	private TransferBatchContentService batchContentService;
	
	@Autowired
	private TransferService transferService;

	@Autowired
	private ExecutorPool executorPool;
	
    @Autowired
    public TransactionServiceImpl(TransactionDao transactionDao) {
        super(transactionDao);
        this.transactionDao = transactionDao;
    }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public InputStream getCsvStream(Date startDate, Date endDate,
			String transactionType) {
		//get row count
		List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();
		if (transactionType != null && transactionType.trim().length() > 0) {
			restrictions.add("t.transaction_type=?");
			values.add(transactionType);
		}
		restrictions.add("t.value_date >= ?");
		values.add(DateUtils.generateStart(startDate));
		restrictions.add("t.value_date < ?");
		values.add(DateUtils.generateEnd(endDate));
		int rowCount = transactionDao.getRowCount(restrictions, values.toArray());		
				
		//query one thousand record at a time (don't query all record)
//		final int QUERY_LIMIT = 1000;
		//max. byte array size (5 MB)
		final int SIZE_LIMIT = 5000000; 
		
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream() ;
		PrintWriter pw = new PrintWriter(byteArray);
		int i = 0;
		DateFormat iso = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		//write header
		StringBuffer sb = new StringBuffer();
		sb.append("\"Value Date\",");
		sb.append("\"From Account Number\",");
		sb.append("\"Transaction Type\",");
		sb.append("\"Customer Reference\",");
		sb.append("\"Transaction Amount\",");
		sb.append("\"Fee\",");
		sb.append("\"Status\",");
		sb.append("\"Resp. Code\",");
		sb.append("\"Bit 48\",");
		sb.append("\"Bit 62\",");
		pw.println(sb.toString());
		while (i < rowCount) {
			List<Transaction> transactionList = transactionDao.getCurrentPageRows(i, 1000, restrictions, null, values.toArray());
			if (transactionList == null) break;
			for (Transaction t : transactionList) {
				sb.delete(0, sb.length());
				sb.append("\"").append(iso.format(t.getValueDate())).append("\"").append(",");
				sb.append("=\"").append(t.getFromAccountNumber()).append("\"").append(",");
				sb.append("\"").append(t.getTransactionTypeModel().getDescription()).append("\"").append(",");
				sb.append("=\"").append(t.getCustomerReference()).append("\"").append(",");
				sb.append("\"").append(t.getTransactionAmount()).append("\"").append(",");
				sb.append("\"").append(t.getFee()).append("\"").append(",");
				sb.append("\"").append(t.getStatus()).append("\"").append(",");
				sb.append("=\"").append(t.getResponseCode()).append("\"").append(",");
				if (isAllNumber(t.getFreeData1())) {
					sb.append("=\"").append(t.getFreeData1()).append("\"").append(",");
				} else {
					sb.append("\"").append(t.getFreeData1()).append("\"").append(",");
				}
				if (isAllNumber(t.getFreeData3())) {
					sb.append("=\"").append(t.getFreeData3()).append("\"").append(",");
				} else {
					sb.append("\"").append(t.getFreeData3()).append("\"").append(",");
				}
				pw.println(sb.toString());
			}
			i += transactionList.size();
			if (byteArray.size() > SIZE_LIMIT)  break;
		} 
		
		pw.flush();
		pw.close();
		return new ByteArrayInputStream(byteArray.toByteArray());
	}
	
	private boolean isAllNumber(String str) {
		if (str == null) return false;
		str = str.trim();
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) < '0' || str.charAt(i) >'9') {
				return false;
			}
		}
		return true;
	}

    public Transaction getByTransType_AccountNo_CustRef_Amount(String transactionType, String accountNumber, String custReference, BigDecimal amount) {
        return transactionDao.getByTransType_AccountNo_CustRef_Amount(transactionType, accountNumber, custReference, amount);
    }

	@Override
	public void executeTransactionQueue(Date processingDate) {
		logger.info("executeTransactionQueue processingDate=" + processingDate);
		List<TransactionQueue> queueList = queueService.getQueuedTransaction(processingDate);
		logger.info("executeTransactionQueue queueList=" + queueList);

        for (int i = 0; i < queueList.size(); i++) {
        	TransactionQueue queue = queueList.get(i);
        	if (queue.getTransactionType().equals(Constants.TRANSFER_CODE)) {
        		//queue.getTransactionType().equals(Constants.TRANSFER_OTHER_SKN_CODE) || queue.getTransactionType().equals(Constants.TRANSFER_OTHER_RTGS_CODE)) {
        		TransferView tv = PojoJsonMapper.fromJson(queue.getTransactionData(), TransferView.class);
        		logger.info("executeTransactionQueue before execute tv=" + tv);
        		Date currentDate = new Date();
//	        		tv.setBatchProcess(true);
        		tv.setTransactionDate(currentDate);
        		tv.setValueDate(currentDate);
        		tv.setTransactionQueueId(queue.getId());    
        		try {
        			transferService.executeNow(tv);
            		logger.info("executeTransactionQueue after execute tv=" + tv);
            		
            		//after execute, set transaction date on transferview back to input date
            		tv.setTransactionDate(queue.getTransactionDate());
            		
            		logger.info("executeTransactionQueue queue.getExecutionType()=" + queue.getExecutionType());
            		if (Constants.PERIODIC_ET.equals(queue.getExecutionType())) {
            			Date date = ScheduleHelper.getNextDate(queue.getPeriodType(), queue.getPeriodValue(), queue.getEndDate());
            			if (date != null) {
            				scheduleNextTransfer(tv, date);
            			}
            		}

            		//set reference number back to original input reference number
            		tv.setReferenceNumber(queue.getReferenceNumber());
                    tv.setResponseCode(null);
            		tv.setStatus(Constants.EXECUTED_STATUS);
            		//update status menjadi sukses, sebelumnya pending
            		tv.setStatus(Constants.SUCCEED_STATUS);
            		String jsonResult = PojoJsonMapper.toJson(tv);
            		logger.info("executeTransactionQueue jsonResult reffNo=" + tv.getReferenceNumber());     		
            		        		
            		queueService.updateStatus(queue.getId(), Constants.EXECUTED_STATUS, jsonResult);
            		logger.info("after update status success"); 
            		
            		//for add update to t_transfer_batch_content
        			int transferType = (queue.getExecutionType().equals(Constants.NOW_ET)) ? 1 : (queue.getExecutionType().equals(Constants.POSTDATED_ET)) ? 2 : 3;
        			TransferBatch batch = batchService.getByCustomerIdAccountNumberTransferTypeTransactionDate(queue.getCustomerId(), queue.getAccountNumber(), transferType, queue.getTransactionDate());
        			if (batch != null) {
        				TransferBatchContent batchContent = batchContentService.getByAllParams(batch.getId(), batch.getCreatedby(), tv.getCustomerReference(), tv.getAmount(), queue.getValueDate(), Constants.PENDING_STATUS);
                		if (batchContent != null) {
                			batchContent.setStatus(Constants.SUCCEED_STATUS);
                			batchContentService.save(batchContent);
                		}
                		//check if there is no batchContent is pending, then transferBatch is success
                		List<TransferBatchContent> batchContents = batchContentService.getPendingByTransferBatchId(batch.getId());
                		if (batchContents == null || batchContents.size() == 0) {
                			batch.setStatus(Constants.SUCCEED_STATUS);
                			batchService.save(batch);
                		}
        			}
        		} catch (BusinessException e) {
        			logger.info("Ada error tidak mendapat RC00 ketika melakukan transfer dari " + tv.getAccountNumber() + " ke rek" + tv.getCustomerId() + ", pesan error=" + e.getFullMessage());
        			//for add update to t_transfer_batch_content
        			int transferType = (queue.getExecutionType().equals(Constants.NOW_ET)) ? 1 : (queue.getExecutionType().equals(Constants.POSTDATED_ET)) ? 2 : 3;
        			TransferBatch batch = batchService.getByCustomerIdAccountNumberTransferTypeTransactionDate(queue.getCustomerId(), queue.getAccountNumber(), transferType, queue.getTransactionDate());
        			if (batch != null) {
        				TransferBatchContent batchContent = batchContentService.getByAllParams(batch.getId(), batch.getCreatedby(), tv.getCustomerReference(), tv.getAmount(), queue.getValueDate(), Constants.PENDING_STATUS);
                		if (batchContent != null) {
                			batchContent.setStatus(Constants.FAILED_STATUS);
                			batchContentService.save(batchContent);
                		}
                		//transferBatch is failed
                		batch.setStatus(Constants.FAILED_STATUS);
                		batchService.save(batch);
        			}
        		}
        	}		
        }
	}
	
	private void scheduleNextTransfer(TransferView tv, Date date) {
    	String referenceNumber = ReferenceGenerator.generate();
        tv.setReferenceNumber(referenceNumber);
        tv.setStatus(Constants.QUEUED_STATUS);
        tv.setResponseCode(null);
        tv.setValueDate(date);
    	TransactionQueue queue = new TransactionQueue();
		queue.setCustomerId(tv.getCustomerId());
		queue.setAccountNumber(tv.getAccountNumber());
		queue.setBillerName(tv.getBillerName());
		queue.setCustomerReference(tv.getCustomerReference());
		queue.setAmount(tv.getAmount());
		queue.setFee(tv.getFee());
		queue.setReferenceNumber(referenceNumber);
		queue.setTransactionType(tv.getTransactionType());
		queue.setExecutionType(Constants.PERIODIC_ET);
		queue.setPeriodType(tv.getPeriodType());
		queue.setPeriodValue(tv.getPeriodValue());
		queue.setEndDate(tv.getEndDate());
		queue.setTransactionDate(tv.getTransactionDate());
		queue.setValueDate(date);
		queue.setStatus(Constants.QUEUED_STATUS);
		queue.setTransactionData(PojoJsonMapper.toJson(tv));
//		queue.setDeliveryChannel(tv.getDeliveryChannel());
//		queue.setDeliveryChannelId(tv.getDeliveryChannelId());
		queue.setCreated(new Date());
		queue.setCreatedby(tv.getCustomerId());
		queue.setUpdated(new Date());
		queue.setUpdatedby(tv.getCustomerId());
		queueService.save(queue);
	}

	@Override
	public BigDecimal getTotalTransferInOneDay(String fromAccount, String toAccount, Date transferDate) {
		List<Transaction> transactions = transactionDao.getTransferInOnDay(fromAccount, toAccount, transferDate);
		BigDecimal totAmount = BigDecimal.ZERO;
		for (Transaction transaction : transactions) {
			totAmount.add(transaction.getTransactionAmount());
		}		
		return totAmount;
	}
}
