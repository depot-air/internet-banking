package com.dwidasa.engine.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.dao.BatchDao;
import com.dwidasa.engine.model.Batch;
import com.dwidasa.engine.service.BatchService;

@Service("batchService")
public class BatchServiceImpl extends GenericServiceImpl<Batch, Long> implements BatchService {
	private BatchDao batchDao;
	
    @Autowired
    public BatchServiceImpl(BatchDao batchDao) {
        super(batchDao);
        this.batchDao = batchDao;
    }

	@Override
	public List<Batch> getAll(Long customerId) {		
		return batchDao.getAll(customerId);
	}
}
