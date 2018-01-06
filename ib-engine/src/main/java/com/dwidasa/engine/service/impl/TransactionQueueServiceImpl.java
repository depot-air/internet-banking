package com.dwidasa.engine.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.dao.TransactionQueueDao;
import com.dwidasa.engine.model.TransactionQueue;
import com.dwidasa.engine.model.view.TransactionStatusView;
import com.dwidasa.engine.service.TransactionQueueService;

@Service("transactionQueueService")
public class TransactionQueueServiceImpl extends GenericServiceImpl<TransactionQueue, Long> implements TransactionQueueService {
	private TransactionQueueDao dao;
	
    @Autowired
    public TransactionQueueServiceImpl(TransactionQueueDao transactionQueueDao) {
        super(transactionQueueDao);
        this.dao = transactionQueueDao;
    }

    @Override
	public List<TransactionQueue> getQueuedTransaction(Date valueDate) {
		return dao.getQueuedTransaction(valueDate);
	}

	@Override
	public List<TransactionQueue> getList(
			TransactionStatusView transactionStatusView) {
		return dao.getList(transactionStatusView);
	}

	@Override
	public void cancelTransferBatch(Long transferBatchId) {
		dao.cancelTransferBatch(transferBatchId);
	}

	@Override
	public void updateStatus(Long id, String status, String transactionData) {
		dao.updateStatus(id, status, transactionData);
	}

	@Override
	public boolean isQueuedTransactionExists(Long customerId) {		
		return dao.isQueuedTransactionExists(customerId);
	}
}
