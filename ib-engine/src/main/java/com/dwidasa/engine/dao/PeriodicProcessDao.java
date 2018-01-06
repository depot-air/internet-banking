package com.dwidasa.engine.dao;

import java.util.Date;

import com.dwidasa.engine.model.PeriodicProcess;

public interface PeriodicProcessDao extends GenericDao<PeriodicProcess, Long> {
	Date getLastProcessDate(String periodType);
}
