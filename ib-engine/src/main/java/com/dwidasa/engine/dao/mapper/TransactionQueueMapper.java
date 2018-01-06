package com.dwidasa.engine.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import com.dwidasa.engine.model.TransactionQueue;

@Component("transactionQueueMapper")
public class TransactionQueueMapper extends ChainedRowMapper<TransactionQueue> implements ParameterizedRowMapper<TransactionQueue> {
    public TransactionQueueMapper() {
    }

    public TransactionQueue chainRow(ResultSet rs, int index) throws SQLException {
        TransactionQueue queue = new TransactionQueue();
        queue.setId(rs.getLong(++index));
        queue.setCustomerId(rs.getLong(++index));
        queue.setAccountNumber(rs.getString(++index));
        queue.setBillerName(rs.getString(++index));
        queue.setCustomerReference(rs.getString(++index));
        queue.setAmount(rs.getBigDecimal(++index));
        queue.setFee(rs.getBigDecimal(++index));
        queue.setReferenceNumber(rs.getString(++index));        
        queue.setTransactionType(rs.getString(++index));
        queue.setExecutionType(rs.getString(++index));
        queue.setTransactionDate(rs.getTimestamp(++index));
        queue.setValueDate(rs.getDate(++index));
        queue.setPeriodType(rs.getInt(++index));
        queue.setPeriodValue(rs.getInt(++index));
        queue.setEndDate(rs.getDate(++index));
        queue.setStatus(rs.getString(++index));
        queue.setTransactionData(rs.getString(++index));
        queue.setDeliveryChannel(rs.getString(++index));
        queue.setDeliveryChannelId(rs.getString(++index));
        queue.setCreated(rs.getTimestamp(++index));
        queue.setCreatedby(rs.getLong(++index));
        queue.setUpdated(rs.getTimestamp(++index));
        queue.setUpdatedby(rs.getLong(++index));
        return queue;
    }
}
