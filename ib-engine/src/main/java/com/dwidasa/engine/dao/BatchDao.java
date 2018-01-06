package com.dwidasa.engine.dao;

import java.util.List;

import com.dwidasa.engine.model.Batch;

public interface BatchDao extends GenericDao<Batch, Long> {

	List<Batch> getAll(Long customerId);
}
