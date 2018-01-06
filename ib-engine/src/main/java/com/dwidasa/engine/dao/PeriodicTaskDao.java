package com.dwidasa.engine.dao;

import java.util.Date;
import java.util.List;

import com.dwidasa.engine.model.PeriodicTask;
import com.dwidasa.engine.model.PeriodicTaskHistory;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 11/14/11
 * Time: 2:36 PM
 */
public interface PeriodicTaskDao extends GenericDao<PeriodicTask, Long> {
    /**
     * Get all task by period type order by execution order ascending.
     * @param periodType period type
     * @return list of periodic task
     */
    public List<PeriodicTask> getAll(String periodType);    
    
    /**
     * execute single task
     * @param processingDate processing date
     * @param userId user id
     * @param task task to execute
     */
    public boolean executeSingleTask(Date processingDate, Long userId, PeriodicTaskHistory task);
    
    
}
