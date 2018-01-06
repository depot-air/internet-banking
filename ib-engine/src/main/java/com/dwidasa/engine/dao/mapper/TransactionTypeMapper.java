package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.TransactionType;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/24/11
 * Time: 10:32 AM
 */
@Component("transactionTypeMapper")
public class TransactionTypeMapper extends ChainedRowMapper<TransactionType>
        implements ParameterizedRowMapper<TransactionType> {

    public TransactionTypeMapper() {
    }

    public TransactionType chainRow(ResultSet rs, int index) throws SQLException {
        TransactionType transactionType = new TransactionType();

        transactionType.setId(rs.getLong(++index));
        transactionType.setTransactionType(rs.getString(++index));
        transactionType.setDescription(rs.getString(++index));
        transactionType.setCreated(rs.getTimestamp(++index));
        transactionType.setCreatedby(rs.getLong(++index));
        transactionType.setUpdated(rs.getTimestamp(++index));
        transactionType.setUpdatedby(rs.getLong(++index));
        transactionType.setFinancial(rs.getInt(++index));

        return transactionType;
    }
}
