package com.dwidasa.engine.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.dao.TransferBatchContentDao;
import com.dwidasa.engine.dao.TransferBatchDao;
import com.dwidasa.engine.model.TransferBatch;
import com.dwidasa.engine.model.TransferBatchContent;
import com.dwidasa.engine.service.TransferBatchService;

@Service("transferBatchService")
public class TransferBatchServiceImpl extends GenericServiceImpl<TransferBatch, Long> implements TransferBatchService {

	private final TransferBatchDao transferBatchDao;

	@Autowired
	public TransferBatchServiceImpl(TransferBatchDao transactionBatchDao) {
		super(transactionBatchDao);
		this.transferBatchDao = transactionBatchDao;
	}
	
	@Autowired
	private TransferBatchContentDao transferBatchContentDao;

	@Override
	public void saveWithContent(TransferBatch transferBatch) {
		transferBatchDao.save(transferBatch);
		
		if (transferBatch.getTransferBatchContentList() != null) {
			for (TransferBatchContent content : transferBatch.getTransferBatchContentList()) {
				content.setTransferBatchId(transferBatch.getId());
				transferBatchContentDao.save(content);
			}
		}
	}

	@Override
	public List<TransferBatch> getTransferBatchList(Long customerId, String accountNumber, Date startDate, Date endDate) { 
		return transferBatchDao.getTransferBatchList(customerId, accountNumber, startDate, endDate);
	}

	@Override
	public TransferBatch getWithContent(Long id) {		
		TransferBatch result = transferBatchDao.get(id);
		if (result != null) {
			List<TransferBatchContent> content = transferBatchContentDao.getAllByTransferBatchId(id);
			result.setTransferBatchContentList(content);
		}
		return result;
	}

	@Override
	public TransferBatch getByCustomerIdAccountNumberTransferTypeTransactionDate(Long customerId, String accountNumber, Integer transferType, Date transactionDate) {
		return transferBatchDao.getByCustomerIdAccountNumberTransferTypeTransactionDate(customerId, accountNumber, transferType, transactionDate);
	}

}
