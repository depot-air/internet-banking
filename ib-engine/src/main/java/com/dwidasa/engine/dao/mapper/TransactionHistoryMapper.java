package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.TransactionHistory;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/20/11
 * Time: 4:43 PM
 */
@Component("transactionHistoryMapper")
public class TransactionHistoryMapper extends ChainedRowMapper<TransactionHistory>
        implements ParameterizedRowMapper<TransactionHistory> {

    public TransactionHistoryMapper() {
    }

    public TransactionHistory chainRow(ResultSet rs, int index) throws SQLException {
        TransactionHistory th = new TransactionHistory();

        th.setId(rs.getLong(++index));
        th.setAccountNumber(rs.getString(++index));
        th.setCurrency(rs.getString(++index));
        th.setTransactionIndicator(rs.getString(++index));
        th.setTransactionAmount(rs.getBigDecimal(++index));
        th.setReferenceNumber(rs.getString(++index));
        th.setEntryDate(rs.getTimestamp(++index));
        th.setPostingDate(rs.getTimestamp(++index));
        th.setProcessingDate(rs.getTimestamp(++index));
        th.setDescription(rs.getString(++index));
        th.setCreated(rs.getTimestamp(++index));
        th.setCreatedby(rs.getLong(++index));
        th.setUpdated(rs.getTimestamp(++index));
        th.setUpdatedby(rs.getLong(++index));

        return th;
    }
}
