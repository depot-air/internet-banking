package com.dwidasa.engine.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.dao.BatchContentDao;
import com.dwidasa.engine.model.BatchContent;
import com.dwidasa.engine.service.BatchContentService;

@Service("batchContentService")
public class BatchContentServiceImpl extends GenericServiceImpl<BatchContent, Long> implements BatchContentService {
	private BatchContentDao batchContentDao;
	
    @Autowired
    public BatchContentServiceImpl(BatchContentDao batchContentDao) {
        super(batchContentDao);
        this.batchContentDao = batchContentDao;
    }

	@Override
	public List<BatchContent> getAll(Long batchId) {
		return batchContentDao.getAll(batchId);
	}

    @Override
    public void removeAll(Long batchId) {
        batchContentDao.removeAll(batchId);
    }
}

