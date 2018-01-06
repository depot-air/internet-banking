package com.dwidasa.engine.service;

import com.dwidasa.engine.model.FileBatch;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 8/1/11
 * Time: 9:46 AM
 */
public interface FileBatchService extends GenericService<FileBatch, Long> {
    /**
     * Process file batch => insert to inbox and notify
     * @param customerIds list of customer id
     * @param inbox object that contain the message string
     */
    public void processFileBatchInsertToInboxAndNotify(String fileName, Long userId);

	void processFileBatchInsertToInboxAndNotify(Long fileBatchId,
			String uploadDir, Long userId);
}
