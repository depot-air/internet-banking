package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.dao.PeriodicTaskHistoryDao;
import com.dwidasa.engine.dao.mapper.PeriodicTaskHistoryMapper;
import com.dwidasa.engine.model.PeriodicTaskHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 11/14/11
 * Time: 2:37 PM
 */
@Repository("periodicTaskHistoryDao")
public class PeriodicTaskHistoryDaoImpl extends GenericDaoImpl<PeriodicTaskHistory, Long>
        implements PeriodicTaskHistoryDao {
    @Autowired
    public PeriodicTaskHistoryDaoImpl(DataSource dataSource, PeriodicTaskHistoryMapper periodicTaskHistoryMapper) {
        super("h_periodic_task", dataSource);
        defaultMapper = periodicTaskHistoryMapper;

        insertSql = new StringBuilder()
            .append("insert into h_periodic_task ( ")
            .append("   h_periodic_process_id, period_type, task_name, class_name, execution_order, status, ")
            .append("   start_time, finish_time, trace, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :periodicProcessId, :periodType, :taskName, :className, :executionOrder, :status, ")
            .append("   :startTime, :finishTime, :trace, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update h_periodic_task ")
            .append("set ")
            .append("   h_periodic_process_id=:periodicProcessId, period_type = :periodType, task_name = :taskName, class_name = :className, ")
            .append("   execution_order = :executionOrder, status = :status, ")
            .append("   start_time = :startTime, finish_time = :finishTime, trace = :trace, created = :created, ")
            .append("   createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }
}
