package com.dwidasa.engine.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import com.dwidasa.engine.model.TransferBatchContent;

@Component("transferBatchContentMapper")
public class TransferBatchContentMapper extends
		ChainedRowMapper<TransferBatchContent> implements
		ParameterizedRowMapper<TransferBatchContent> {

	public TransferBatchContentMapper() {
	}

	public TransferBatchContent chainRow(ResultSet rs, int index)
			throws SQLException {
		TransferBatchContent transferBatchContent = new TransferBatchContent();
		
		transferBatchContent.setId(rs.getLong(++index));
		transferBatchContent.setTransferBatchId(rs.getLong(++index));
		transferBatchContent.setAccountNumber(rs.getString(++index));
		transferBatchContent.setCustomerName(rs.getString(++index));
		transferBatchContent.setAmount(rs.getBigDecimal(++index));
		transferBatchContent.setValueDate(rs.getDate(++index));
		transferBatchContent.setStatus(rs.getString(++index));
		transferBatchContent.setCreated(rs.getTimestamp(++index));
		transferBatchContent.setCreatedby(rs.getLong(++index));
		transferBatchContent.setUpdated(rs.getTimestamp(++index));
		transferBatchContent.setUpdatedby(rs.getLong(++index));

		return transferBatchContent;
	}
}
