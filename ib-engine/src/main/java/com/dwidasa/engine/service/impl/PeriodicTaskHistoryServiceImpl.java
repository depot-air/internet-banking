package com.dwidasa.engine.service.impl;

import com.dwidasa.engine.dao.PeriodicTaskHistoryDao;
import com.dwidasa.engine.model.PeriodicTaskHistory;
import com.dwidasa.engine.service.PeriodicTaskHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 11/14/11
 * Time: 2:40 PM
 */
@Service("periodicTaskHistoryService")
public class PeriodicTaskHistoryServiceImpl extends GenericServiceImpl<PeriodicTaskHistory, Long>
        implements PeriodicTaskHistoryService {
    @Autowired
    public PeriodicTaskHistoryServiceImpl(PeriodicTaskHistoryDao periodicTaskHistoryDao) {
        super(periodicTaskHistoryDao);
    }
}
