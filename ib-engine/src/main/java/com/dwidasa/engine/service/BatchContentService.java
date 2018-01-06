package com.dwidasa.engine.service;

import java.util.List;

import com.dwidasa.engine.model.BatchContent;

public interface BatchContentService extends GenericService<BatchContent, Long> {

	/**
	 * Get all batch content where m_batch_id equals to batchId
	 * @param batchId batch id
	 * @return list of Batch Content
	 */
	List<BatchContent> getAll(Long batchId);

    /**
     * Remove all batch content where m_batch_id equals to batchId
     * @param batchId batch id
     */
    void removeAll(Long batchId);
}
