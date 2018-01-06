package com.dwidasa.engine.service;

import java.util.List;

import com.dwidasa.engine.model.Batch;

public interface BatchService extends GenericService<Batch, Long> {
	/**
	 * Get all batch by customer id
	 * @param customerId customer id
	 * @return list of Batch belongs to current customer
	 */
	List<Batch> getAll(Long customerId);

}
