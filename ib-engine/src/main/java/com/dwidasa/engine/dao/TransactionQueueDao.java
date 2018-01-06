package com.dwidasa.engine.dao;

import java.util.Date;
import java.util.List;

import com.dwidasa.engine.model.TransactionQueue;
import com.dwidasa.engine.model.view.TransactionStatusView;

public interface TransactionQueueDao extends GenericDao<TransactionQueue, Long> {
	/**
	 * Get queued transactions which need to be execute on valueDate 
	 * @param valueDate
	 * @return list of transaction queue
	 */
	List<TransactionQueue> getQueuedTransaction(Date valueDate);

	/**
	 * Get queued transactions by TransactionStatusView
	 * @param transactionStatusView view parameter
	 * @return list of transaction queue
	 */
	List<TransactionQueue> getList(TransactionStatusView transactionStatusView);

	/**
	 * update status of queued transfer batch to CANCELED_STATUS
	 * updated record is a record where transactionType == transferBatch and transactionData == transferBatchId
	 * @param transferBatchId
	 */
	void cancelTransferBatch(Long transferBatchId);

	/**
	 * Update status of queued transaction
	 * @param id
	 * @param status
	 * @param transactionData 
	 */
	void updateStatus(Long id, String status, String transactionData);

	/**
	 * check if queued transaction exists
	 * @param customerId
	 * @return true if still exists
	 */
	boolean isQueuedTransactionExists(Long customerId);
	
}
