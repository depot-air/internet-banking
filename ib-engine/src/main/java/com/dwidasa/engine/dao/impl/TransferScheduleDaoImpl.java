package com.dwidasa.engine.dao.impl;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.dao.TransferScheduleDao;
import com.dwidasa.engine.dao.mapper.TransferScheduleMapper;
import com.dwidasa.engine.model.TransferSchedule;

@Repository("transferScheduleDao")
public class TransferScheduleDaoImpl extends GenericDaoImpl<TransferSchedule, Long> implements TransferScheduleDao {
	@Autowired
	public TransferScheduleDaoImpl(DataSource dataSource, TransferScheduleMapper transferScheduleMapper) {
		super("m_transfer_schedule", dataSource);
		defaultMapper = transferScheduleMapper;
		
		insertSql = new StringBuilder()

		.append("insert into m_transfer_schedule ( ")
		.append("   m_batch_id, account_from, account_to, amount, news, batch_type, transfer_date, transfer_end, flag_sent, created, createdby, updated, updatedby ")
		.append(") ")
		.append("values ( ")
		.append("   :batchId, :accountFrom, :accountTo, :amount, :news, :batchType, :transferDate, :transferEnd, :flagSent, :created, :createdby, :updated, :updatedby ")
		.append(") ");
		
		updateSql = new StringBuilder()
		.append("update m_transfer_schedule ")
		.append("set ")
		.append("   m_batch_id = :batchId, account_from = :accountFrom, account_to=:accountTo,  ")
		.append("   amount = :amount, news = :news, batch_type=:batchType,  ")
		.append("   transfer_date = :transferDate, transfer_end = :transferEnd, flag_sent=:flagSent,  ")
		.append("   created = :created, createdby = :createdby, ")
		.append("   updated = :updated, updatedby = :updatedby ")
		.append("where id = :id ");
	}



}
