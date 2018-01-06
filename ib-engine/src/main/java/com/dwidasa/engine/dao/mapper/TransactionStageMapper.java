package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.TransactionStage;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/26/11
 * Time: 2:33 PM
 */
@Component("transactionStageMapper")
public class TransactionStageMapper extends ChainedRowMapper<TransactionStage>
        implements ParameterizedRowMapper<TransactionStage> {

    public TransactionStageMapper() {
    }

    @Override
    public TransactionStage chainRow(ResultSet rs, int index) throws SQLException {
        TransactionStage ts = new TransactionStage();

        ts.setId(rs.getLong(++index));
        ts.setTransactionId(rs.getLong(++index));
        ts.setStatus(rs.getString(++index));
        ts.setCreated(rs.getTimestamp(++index));
        ts.setCreatedby(rs.getLong(++index));
        ts.setUpdated(rs.getTimestamp(++index));
        ts.setUpdatedby(rs.getLong(++index));

        return ts;
    }
}
