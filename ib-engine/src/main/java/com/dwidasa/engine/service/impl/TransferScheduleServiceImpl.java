package com.dwidasa.engine.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.dao.TransferScheduleDao;
import com.dwidasa.engine.model.TransferSchedule;
import com.dwidasa.engine.service.TransferScheduleService;

@Service("transferScheduleService")
public class TransferScheduleServiceImpl extends GenericServiceImpl<TransferSchedule, Long> implements TransferScheduleService {
	private TransferScheduleDao transferScheduleDao;
	
    @Autowired
    public TransferScheduleServiceImpl(TransferScheduleDao transferScheduleDao) {
        super(transferScheduleDao);
        this.transferScheduleDao = transferScheduleDao;
    }


}
