package com.dwidasa.engine.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.dwidasa.engine.model.TransferBatchContent;

public interface TransferBatchContentService extends GenericService<TransferBatchContent, Long> {

	List<TransferBatchContent> getPendingByTransferBatchId(Long id);

	TransferBatchContent getByAllParams(Long transferBatchId, Long customerId, String customerReference, BigDecimal amount, Date valueDate, String status);
}

