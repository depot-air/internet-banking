package com.dwidasa.engine.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.dwidasa.engine.model.TransferBatchContent;

public interface TransferBatchContentDao extends GenericDao<TransferBatchContent, Long> {

	List<TransferBatchContent> getAllByTransferBatchId(Long id);

	List<TransferBatchContent> getPendingByTransferBatchId(Long id);
	
	TransferBatchContent getByAllParams(Long transferBatchId, Long customerId, String customerReference, BigDecimal amount, Date valueDate, String status);
}
