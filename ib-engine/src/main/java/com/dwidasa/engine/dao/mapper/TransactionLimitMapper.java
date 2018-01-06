package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.TransactionLimit;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/5/11
 * Time: 8:51 PM
 */
@Component("transactionLimitMapper")
public class TransactionLimitMapper extends ChainedRowMapper<TransactionLimit>
        implements ParameterizedRowMapper<TransactionLimit> {

    public TransactionLimitMapper() {
    }

    public TransactionLimit chainRow(ResultSet rs, int index) throws SQLException {
        TransactionLimit transactionLimit = new TransactionLimit();

        transactionLimit.setId(rs.getLong(++index));
        transactionLimit.setTransactionTypeId(rs.getLong(++index));
        transactionLimit.setProviderId(rs.getLong(++index));
        transactionLimit.setMinLimit(rs.getBigDecimal(++index));
        transactionLimit.setMaxLimit(rs.getBigDecimal(++index));
        transactionLimit.setCreated(rs.getTimestamp(++index));
        transactionLimit.setCreatedby(rs.getLong(++index));
        transactionLimit.setUpdated(rs.getTimestamp(++index));
        transactionLimit.setUpdatedby(rs.getLong(++index));

        return transactionLimit;
    }
}
