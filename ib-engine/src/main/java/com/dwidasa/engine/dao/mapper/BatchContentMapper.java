package com.dwidasa.engine.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import com.dwidasa.engine.model.BatchContent;

@Component("batchContentMapper")
public class BatchContentMapper extends ChainedRowMapper<BatchContent> implements ParameterizedRowMapper<BatchContent> {
    public BatchContentMapper() {
    }

    public BatchContent chainRow(ResultSet rs, int index) throws SQLException {
        BatchContent batchContent = new BatchContent();

        batchContent.setId(rs.getLong(++index));
        batchContent.setBatchId(rs.getLong(++index));
        batchContent.setAccountNumber(rs.getString(++index));
        batchContent.setCustomerName(rs.getString(++index));
        batchContent.setAmount(rs.getBigDecimal(++index));
        batchContent.setDescription(rs.getString(++index));
        
        batchContent.setCreated(rs.getTimestamp(++index));
        batchContent.setCreatedby(rs.getLong(++index));
        batchContent.setUpdated(rs.getTimestamp(++index));
        batchContent.setUpdatedby(rs.getLong(++index));
        return batchContent;
    }
}
