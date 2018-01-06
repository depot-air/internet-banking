package com.dwidasa.engine.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.dao.TransferBatchDao;
import com.dwidasa.engine.dao.mapper.TransferBatchMapper;
import com.dwidasa.engine.model.TransferBatch;

@Repository("transerBatchDao")
public class TransferBatchDaoImpl extends GenericDaoImpl<TransferBatch, Long> implements TransferBatchDao {
	@Autowired
	public TransferBatchDaoImpl(DataSource dataSource, TransferBatchMapper transferBatchMapper) {
		super("t_transfer_batch", dataSource);
		defaultMapper = transferBatchMapper;
		
		insertSql = new StringBuilder()
				.append("insert into t_transfer_batch ( ")
				.append("   m_customer_id, account_number, batch_name, batch_description, transfer_type, ")
				.append("   transaction_date, reference_number, status, total_amount,")
				.append("   created, createdby, updated, updatedby ")
				.append(") ")
				.append("values ( ")
				.append("   :customerId, :accountNumber, :batchName, :batchDescription, :transferType, ")
				.append("   :transactionDate, :referenceNumber, :status, :totalAmount,")
				.append("   :created, :createdby, :updated, :updatedby")
				.append(") ");
		
		updateSql = new StringBuilder()
				.append("update t_transfer_batch ")
				.append("set ")
				.append("   m_customer_id = :customerId, account_number = :accountNumber, ")
				.append("   batch_name = :batchName, batch_description = :batchDescription, transfer_type = :transferType, ")
				.append("   transaction_date = :transactionDate, reference_number = :referenceNumber, status = :status, total_amount = :totalAmount,")
				.append("   created = :created, createdby = :createdby, updated = :updated, updatedby = :updatedby ")
				.append("where id = :id ");
	}

	@Override
	public List<TransferBatch> getTransferBatchList(Long customerId,
			String accountNumber, Date startDate, Date endDate) {
		String sqlSelect = "select * from t_transfer_batch where m_customer_id=? and account_number=?" +
			" and value_date >= ? and value_date < ?";
		return getSimpleJdbcTemplate().query(sqlSelect, defaultMapper, new Object[] {customerId, accountNumber, startDate, endDate});
	}

	@Override
	public TransferBatch getByCustomerIdAccountNumberTransferTypeTransactionDate(Long customerId, String accountNumber, Integer transferType, Date transactionDate) {
		String sqlSelect = "select * from t_transfer_batch where m_customer_id=? and account_number=?" +
				" and transfer_type = ? and  to_char(transaction_date, 'YYYY-MM-DD') = ? ";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	String transDate = sdf.format(transactionDate);
		List<TransferBatch> transferBatches = getSimpleJdbcTemplate().query(sqlSelect, defaultMapper, new Object[] {customerId, accountNumber, transferType, transDate});
		if (transferBatches != null && transferBatches.size() > 0) {
			return transferBatches.get(0);
		}
		return null;
	}	
}
