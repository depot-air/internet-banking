package com.dwidasa.engine.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.dao.TransferBatchContentDao;
import com.dwidasa.engine.model.TransferBatchContent;
import com.dwidasa.engine.service.TransferBatchContentService;

@Service("transferBatchContentService")
public class TransferBatchContentServiceImpl extends
		GenericServiceImpl<TransferBatchContent, Long> implements TransferBatchContentService {

	@SuppressWarnings("unused")
	private final TransferBatchContentDao transferBatchContentDao;

	@Autowired
	public TransferBatchContentServiceImpl(TransferBatchContentDao transferBatchContentDao) {
		super(transferBatchContentDao);
		this.transferBatchContentDao = transferBatchContentDao;
	}

	@Override
	public List<TransferBatchContent> getPendingByTransferBatchId(Long id) {
		return transferBatchContentDao.getPendingByTransferBatchId(id);
	}

	@Override
	public TransferBatchContent getByAllParams(Long transferBatchId, Long customerId, String customerReference, BigDecimal amount, Date valueDate, String status) {
		return transferBatchContentDao.getByAllParams(transferBatchId, customerId, customerReference, amount, valueDate, status);
	}
}
