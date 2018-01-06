package com.dwidasa.engine.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import com.dwidasa.engine.model.TransferBatch;

@Component("transferBatchMapper")
public class TransferBatchMapper extends ChainedRowMapper<TransferBatch>
		implements ParameterizedRowMapper<TransferBatch> {

	public TransferBatchMapper() {
	}

	public TransferBatch chainRow(ResultSet rs, int index)
			throws SQLException {
		TransferBatch transferBatch = new TransferBatch();
		transferBatch.setId(rs.getLong(++index));
		transferBatch.setCustomerId(rs.getLong(++index));
		transferBatch.setAccountNumber(rs.getString(++index));
		transferBatch.setBatchName(rs.getString(++index));
		transferBatch.setBatchDescription(rs.getString(++index));
		transferBatch.setTransferType(rs.getInt(++index));
		transferBatch.setTransactionDate(rs.getTimestamp(++index));
		transferBatch.setReferenceNumber(rs.getString(++index));
		transferBatch.setStatus(rs.getString(++index));
		transferBatch.setTotalAmount(rs.getBigDecimal(++index));
		transferBatch.setCreated(rs.getTimestamp(++index));
		transferBatch.setCreatedby(rs.getLong(++index));
		transferBatch.setUpdated(rs.getTimestamp(++index));
		transferBatch.setUpdatedby(rs.getLong(++index));
		return transferBatch;
	}
}
