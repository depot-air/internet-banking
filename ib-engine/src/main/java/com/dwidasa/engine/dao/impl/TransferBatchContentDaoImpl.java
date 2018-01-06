package com.dwidasa.engine.dao.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.TransferBatchContentDao;
import com.dwidasa.engine.dao.mapper.TransferBatchContentMapper;
import com.dwidasa.engine.model.TransferBatchContent;


@Repository("transferBatchContentDao")
public class TransferBatchContentDaoImpl extends GenericDaoImpl<TransferBatchContent, Long> implements TransferBatchContentDao {
	@Autowired
	public TransferBatchContentDaoImpl(DataSource dataSource, TransferBatchContentMapper transferBatchContentMapper) {
		super("t_transfer_batch", dataSource);
		defaultMapper = transferBatchContentMapper;
		 
		insertSql = new StringBuilder()
				.append("insert into t_transfer_batch_content ( ")
				.append("   t_transfer_batch_id, account_number, customer_name, amount, value_date, status, ")
				.append("   created, createdby, updated, updatedby ")
				.append(") ")
				.append("values ( ")
				.append("   :transferBatchId, :accountNumber, :customerName, :amount, :valueDate, :status, ") 
				.append("   :created, :createdby, :updated, :updatedby")
				.append(") ");

		updateSql = new StringBuilder()
				.append("update t_transfer_batch_content ")
				.append("set ")
				.append("   t_transfer_batch_id = :transferBatchId, account_number = :accountNumber, customer_name = :customerName, ")
				.append("   amount = :amount, value_date = :valueDate, status = :status,")
				.append("   created = :created, createdby = :createdby, updated = :updated, updatedby = :updatedby ")
				.append("where id = :id ");
	}

	@Override
	public List<TransferBatchContent> getAllByTransferBatchId(Long id) {
		String sqlSelect = "select * from t_transfer_batch_content where t_transfer_batch_id=?";
		return getSimpleJdbcTemplate().query(sqlSelect, defaultMapper, new Object[] {id});
	}

	@Override
	public List<TransferBatchContent> getPendingByTransferBatchId(Long id) {
		String sqlSelect = "select * from t_transfer_batch_content where status=? and t_transfer_batch_id=?";
		return getSimpleJdbcTemplate().query(sqlSelect, defaultMapper, new Object[] {Constants.PENDING_STATUS, id});
	}

	@Override
	public TransferBatchContent getByAllParams(Long transferBatchId, Long customerId, String customerReference, BigDecimal amount, Date valueDate, String status) {
		String sqlSelect = "select * from t_transfer_batch_content where t_transfer_batch_id=? and createdby=? and account_number=? and amount=? and value_date = ? and status=?";
		List<TransferBatchContent> batchContents = getSimpleJdbcTemplate().query(sqlSelect, defaultMapper, new Object[] {transferBatchId, customerId, customerReference, amount, valueDate, status});
		if (batchContents != null && batchContents.size() > 0) {
			return batchContents.get(0);
		}
		return null;
	}

	
}
