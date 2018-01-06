package com.dwidasa.engine.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.dao.PeriodicProcessDao;
import com.dwidasa.engine.model.PeriodicProcess;
import com.dwidasa.engine.service.PeriodicProcessService;

@Service("periodicProcessService")
public class PeriodicProcessServiceImpl extends	GenericServiceImpl<PeriodicProcess, Long> implements
		PeriodicProcessService {
	private PeriodicProcessDao periodicProcessDao;
	
	@Autowired
	public PeriodicProcessServiceImpl(PeriodicProcessDao periodicProcessDao) {
		super(periodicProcessDao);
		this.periodicProcessDao = periodicProcessDao;
	}

	public Date getLastProcessDate(String periodType) {
		return periodicProcessDao.getLastProcessDate(periodType);
	}
}