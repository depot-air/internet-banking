package com.dwidasa.engine.dao.impl;

import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.PeriodicTaskDao;
import com.dwidasa.engine.dao.PeriodicTaskHistoryDao;
import com.dwidasa.engine.dao.mapper.PeriodicTaskMapper;
import com.dwidasa.engine.model.PeriodicTask;
import com.dwidasa.engine.model.PeriodicTaskHistory;
import com.dwidasa.engine.service.task.Executable;
import com.dwidasa.engine.util.EngineUtils;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 11/14/11
 * Time: 2:37 PM
 */
@Repository("periodicTaskDao")
public class PeriodicTaskDaoImpl extends GenericDaoImpl<PeriodicTask, Long> implements PeriodicTaskDao {
    @Autowired
    private PeriodicTaskHistoryDao periodicTaskHistoryDao;

    @Autowired
    public PeriodicTaskDaoImpl(DataSource dataSource, PeriodicTaskMapper periodicTaskMapper) {
        super("m_periodic_task", dataSource);
        this.defaultMapper = periodicTaskMapper;

        insertSql = new StringBuilder()
            .append("insert into m_periodic_task ( ")
            .append("   period_type, task_name, class_name, execution_order, running, status, ")
            .append("   last_success_date, trace, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :periodType, :taskName, :className, :executionOrder, :running, :status, ")
            .append("   :lastSuccessDate, :trace, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_periodic_task ")
            .append("set ")
            .append("   period_type = :periodType, task_name = :taskName, class_name = :className, ")
            .append("   execution_order = :executionOrder, running = :running, status = :status, ")
            .append("   last_success_date = :lastSuccessDate, trace = :trace, created = :created, ")
            .append("   createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public List<PeriodicTask> getAll(String periodType) {
        StringBuilder sql = new StringBuilder()
                .append("select mpt.* ")
                .append("from m_periodic_task mpt ")
                .append("where mpt.period_type = ? ")
                .append("order by mpt.execution_order ");

        return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, periodType);
    }
        

	@Override
	public boolean executeSingleTask(Date processingDate, Long userId, PeriodicTaskHistory task) {
		task.setStatus(Constants.PERIODIC_STATUS.PROGRESS);
		task.setStartTime(new Date());
		periodicTaskHistoryDao.save(task);
		Executable executor = null;

        try {
            executor = (Executable) Class.forName(task.getClassName()).newInstance();
            executor.execute(processingDate, userId);
            Date currentDate = new Date();
	        task.setUpdated(currentDate);
	        task.setStatus(Constants.PERIODIC_STATUS.SUCCESS); 
	        task.setFinishTime(currentDate);
	        periodicTaskHistoryDao.save(task);
	        return true;
        } catch (Exception e) {
        	e.printStackTrace();
        	if (executor != null) {
        		executor.cleanup(processingDate);
        	}
        	Date currentDate = new Date();
        	StringBuffer trace = new StringBuffer(e.getClass().getSimpleName()).append(" ").append(EngineUtils.getRootCause(e)).append("\n");
        	for (StackTraceElement element : e.getStackTrace() ){
        		trace.append(element).append("\n");
        		if (trace.length() > 4000) break;
        	}        	
        	if (trace.length() > 4000) {
        		trace.delete(4000, trace.length());
        	}
	        task.setTrace(trace.toString());
	        task.setUpdated(currentDate);
	        task.setStatus(Constants.PERIODIC_STATUS.FAIL); 
	        task.setFinishTime(currentDate);
	        periodicTaskHistoryDao.save(task);
            return false;
        } 
	}
}
