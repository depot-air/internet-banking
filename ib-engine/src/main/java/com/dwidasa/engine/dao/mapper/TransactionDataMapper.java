package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.TransactionData;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 8/10/11
 * Time: 3:51 PM
 */
@Component("transactionDataMapper")
public class TransactionDataMapper extends ChainedRowMapper<TransactionData>
        implements ParameterizedRowMapper<TransactionData> {

    public TransactionDataMapper() {
    }

    public TransactionData chainRow(ResultSet rs, int index) throws SQLException {
        TransactionData td = new TransactionData();

        td.setId(rs.getLong(++index));
        td.setTransactionId(rs.getLong(++index));
        td.setClassName(rs.getString(++index));
        td.setTransactionData(rs.getString(++index));
        td.setCreated(rs.getTimestamp(++index));
        td.setCreatedby(rs.getLong(++index));
        td.setUpdated(rs.getTimestamp(++index));
        td.setUpdatedby(rs.getLong(++index));

        return td;
    }
}
