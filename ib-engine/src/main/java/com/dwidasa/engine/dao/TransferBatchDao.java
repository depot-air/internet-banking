package com.dwidasa.engine.dao;

import java.util.Date;
import java.util.List;

import com.dwidasa.engine.model.TransferBatch;

public interface TransferBatchDao extends GenericDao<TransferBatch, Long> {
	List<TransferBatch> getTransferBatchList(Long customerId, String accountNumber, Date startDate, Date endDate);
	TransferBatch getByCustomerIdAccountNumberTransferTypeTransactionDate(Long customerId, String accountNumber, Integer transferType, Date transactionDate);
}
