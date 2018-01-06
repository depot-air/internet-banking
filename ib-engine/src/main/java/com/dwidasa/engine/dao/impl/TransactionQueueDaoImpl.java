package com.dwidasa.engine.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.TransactionQueueDao;
import com.dwidasa.engine.dao.mapper.TransactionQueueMapper;
import com.dwidasa.engine.model.TransactionQueue;
import com.dwidasa.engine.model.view.TransactionStatusView;
import com.dwidasa.engine.util.DateUtils;

@Repository("transactionQueueDao")
public class TransactionQueueDaoImpl extends GenericDaoImpl<TransactionQueue, Long> implements TransactionQueueDao {
    @Autowired
    public TransactionQueueDaoImpl(DataSource dataSource, TransactionQueueMapper transactionQueueMapper) {
        super("t_transaction_queue", dataSource);
        defaultMapper = transactionQueueMapper;
        
        insertSql = new StringBuilder()
            .append("insert into t_transaction_queue ( ")
            .append("   m_customer_id, account_number, biller_name, customer_reference, amount, fee, ")
            .append("   reference_number, transaction_type, execution_type, transaction_date, value_date, ")
            .append("   period_type, period_value, end_date, status, transaction_data,")
            .append("   delivery_channel, delivery_channel_id,")
            .append("   created, createdby, updated, updatedby")
            .append(") ")
            .append("values ( ")
            .append("    ")
            .append("   :customerId, :accountNumber, :billerName, :customerReference, :amount, :fee, ")
            .append("   :referenceNumber, :transactionType, :executionType, :transactionDate, :valueDate,")
            .append("   :periodType, :periodValue, :endDate, :status, :transactionData, ")
            .append("   :deliveryChannel, :deliveryChannelId,")
            .append("   :created, :createdby, :updated, :updatedby")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update t_transaction_queue ")
            .append("set ")
            .append("   m_customer_id = :customerId, account_number = :accountNumber, biller_name = :billerName, customer_reference = :customerReference, amount = :amount, fee = :fee,")
            .append("	reference_number = :referenceNumber, transaction_type = :transactionType, execution_type = :executionType, transaction_date = :transactionDate, value_date = :valueDate, ")
            .append("   period_type = :periodType, period_value = :periodValue, end_date = :endDate, status = :status, transaction_data = :transactionData, ")
            .append("   delivery_channel = :deliveryChannel, delivery_channel_id = :deliveryChannelId, ")
        	.append("   created = :created, createdby = :createdby, ")
            .append("   updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

	@Override
	public List<TransactionQueue> getQueuedTransaction(Date valueDate) {
		String sqlSelect = "select * from t_transaction_queue where status=? and value_date=? order by id ";
		return getJdbcTemplate().query(sqlSelect, new Object[] {Constants.QUEUED_STATUS, valueDate}, defaultMapper);
	}

	@Override
	public List<TransactionQueue> getList(TransactionStatusView transactionStatusView) {
		StringBuilder sbSelect = new StringBuilder("select * from t_transaction_queue");
		List<Object> paramList = new ArrayList<Object>();
		if ("valueDate".equals(transactionStatusView.getDateType())) {
			sbSelect.append(" where value_date >= ? and value_date < ?");
		} else { 
			sbSelect.append(" where transaction_date >= ? and transaction_date < ?");
		}
		paramList.add(DateUtils.generateStart(transactionStatusView.getStartDate()));
		paramList.add(DateUtils.generateEnd(transactionStatusView.getEndDate()));
		
		if (transactionStatusView.getStatus() != null && transactionStatusView.getStatus().trim().length() > 0) {
			sbSelect.append(" and status=?");
			paramList.add(transactionStatusView.getStatus());
		}
		
		if (transactionStatusView.getTransactionType() != null && transactionStatusView.getTransactionType().trim().length() > 0) {
			sbSelect.append(" and transaction_type=?");
			paramList.add(transactionStatusView.getTransactionType());
		}
		
		sbSelect.append(" and account_number=? and m_customer_id=? order by ");
		if ("valueDate".equals(transactionStatusView.getDateType())) {
			sbSelect.append(" value_date, id");
		} else {
			sbSelect.append(" transaction_date, id");
		}
		paramList.add(transactionStatusView.getAccountNumber());
		paramList.add(transactionStatusView.getCustomerId());
		return getJdbcTemplate().query(sbSelect.toString(), paramList.toArray(), defaultMapper);
	}

	@Override
	public void cancelTransferBatch(Long transferBatchId) {
		String sqlSelect = "update t_transaction_queue set status=? where transaction_type='TB' and transaction_data=?";
		getJdbcTemplate().update(sqlSelect, new Object[] {Constants.CANCELED_STATUS, transferBatchId.toString()});
	}

	@Override
	public void updateStatus(Long id, String status, String transactionData) {
		String sqlUpdate = "update t_transaction_queue set status=?, transaction_data=? where id=?";
		logger.info("sqlUpdate=" + sqlUpdate + " id=" + id + " status=" + status );
		int result = getJdbcTemplate().update(sqlUpdate, new Object[] {status, transactionData, id});
		logger.info("result=" + result);
		
	}

	@Override
	public boolean isQueuedTransactionExists(Long customerId) {
		DateTime currentDate = new DateTime().withTime(0, 0, 0, 0);
		String sqlSelect = "select exists(select 1 from t_transaction_queue where m_customer_id=? and status='QUEUED' and value_date>=?)";
		return getJdbcTemplate().queryForObject(sqlSelect, new Object[] {customerId, currentDate.toDate()}, Boolean.class);
	}
}