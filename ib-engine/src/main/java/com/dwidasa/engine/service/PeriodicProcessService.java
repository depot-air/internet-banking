package com.dwidasa.engine.service;

import java.util.Date;

import com.dwidasa.engine.model.PeriodicProcess;

public interface PeriodicProcessService extends GenericService<PeriodicProcess, Long> {
	public Date getLastProcessDate(String periodType);
}
