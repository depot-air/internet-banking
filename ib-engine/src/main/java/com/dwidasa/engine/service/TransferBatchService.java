package com.dwidasa.engine.service;

import java.util.Date;
import java.util.List;

import com.dwidasa.engine.model.TransferBatch;

public interface TransferBatchService extends GenericService<TransferBatch, Long> {

	void saveWithContent(TransferBatch transferBatchView);

	List<TransferBatch> getTransferBatchList(Long customerId, String accountNumber, Date startDate, Date endDate);

	TransferBatch getWithContent(Long id);

	TransferBatch getByCustomerIdAccountNumberTransferTypeTransactionDate(Long customerId, String accountNumber, Integer transferType, Date transactionDate);	
}

