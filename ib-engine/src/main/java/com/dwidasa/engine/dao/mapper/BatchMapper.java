package com.dwidasa.engine.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import com.dwidasa.engine.model.Batch;

@Component("batchMapper")
public class BatchMapper extends ChainedRowMapper<Batch> implements ParameterizedRowMapper<Batch> {
    public BatchMapper() {
    }

    public Batch chainRow(ResultSet rs, int index) throws SQLException {
        Batch batch = new Batch();

        batch.setId(rs.getLong(++index));
        batch.setBatchName(rs.getString(++index));
        batch.setDescription(rs.getString(++index));
        batch.setCustomerId(rs.getLong(++index));
        
        batch.setCreated(rs.getTimestamp(++index));
        batch.setCreatedby(rs.getLong(++index));
        batch.setUpdated(rs.getTimestamp(++index));
        batch.setUpdatedby(rs.getLong(++index));

        return batch;
    }
}
