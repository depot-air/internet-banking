package com.dwidasa.engine.dao.impl;

import java.util.Date;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.dao.PeriodicProcessDao;
import com.dwidasa.engine.dao.mapper.PeriodicProcessMapper;
import com.dwidasa.engine.model.PeriodicProcess;


@Repository("periodicProcessDao")
public class PeriodicProcessDaoImpl extends GenericDaoImpl<PeriodicProcess, Long> implements PeriodicProcessDao {

	@Autowired
	public PeriodicProcessDaoImpl(DataSource dataSource, PeriodicProcessMapper periodicProcessMapper) {
		super("h_periodic_process", dataSource);
        this.defaultMapper = periodicProcessMapper;
        
        insertSql = new StringBuilder()
	        .append("insert into h_periodic_process ( ")
	        .append("   period_type, process_date, start_time, finish_time, status, ")
	        .append("   created, createdby, updated, updatedby ")
	        .append(") ")
	        .append("values ( ")
	        .append("   :periodType, :processDate, :startTime, :finishTime, :status, ")
	        .append("   :created, :createdby, :updated, :updatedby ")
	        .append(") ");
	
	    updateSql = new StringBuilder()
	        .append("update h_periodic_process ")
	        .append("set ")
	        .append("   period_type = :periodType, process_date = :processDate, ")
	        .append("   start_time = :startTime, finish_time = :finishTime, status = :status,")
	        .append("   created = :created, ")
	        .append("   createdby = :createdby, updated = :updated, updatedby = :updatedby ")
	        .append("where id = :id ");
	}

	public Date getLastProcessDate(String periodType) {
		String sqlSelect = "select max(process_date) from h_periodic_process where period_type=?";
		try {
			return getSimpleJdbcTemplate().queryForObject(sqlSelect, Date.class, new Object[] {periodType});
		} catch (EmptyResultDataAccessException e) {
            //-- actually do nothing
            e.printStackTrace();
            return null;
        }
	}

	
}
