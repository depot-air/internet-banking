package com.dwidasa.engine.service.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.CustomerService;
import com.dwidasa.engine.service.TransferBatchContentService;
import com.dwidasa.engine.service.TransferBatchService;
import com.dwidasa.engine.service.facade.TransferService;

@Service("executorPool")
public class ExecutorPool {
	@Autowired
	private TransferBatchService transferBatchService;
	
	@Autowired
	private TransferBatchContentService transferBatchContentService;
	
	@Autowired
    private TransferService transferService;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	private CustomerService customerService;
	
	public void createTransferBatchExecutor(Long transferBatchId, String deliveryChannelId) {
		TransferBatchExecutor batchExecutor = new TransferBatchExecutor(transferBatchId, deliveryChannelId,
				transferBatchService, transferBatchContentService, transferService, cacheManager, customerService);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(batchExecutor);
		executor.shutdown();
	}
	
}
