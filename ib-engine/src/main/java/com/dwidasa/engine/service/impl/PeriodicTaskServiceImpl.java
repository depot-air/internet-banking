package com.dwidasa.engine.service.impl;

import com.dwidasa.engine.dao.PeriodicTaskDao;
import com.dwidasa.engine.model.PeriodicTask;
import com.dwidasa.engine.service.PeriodicTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 11/14/11
 * Time: 2:39 PM
 */
@Service("periodicTaskService")
public class PeriodicTaskServiceImpl extends GenericServiceImpl<PeriodicTask, Long> implements PeriodicTaskService {
    @Autowired
    public PeriodicTaskServiceImpl(PeriodicTaskDao periodicTaskDao) {
        super(periodicTaskDao);
    }
}
