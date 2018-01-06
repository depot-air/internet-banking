package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.Balance;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/3/11
 * Time: 3:21 PM
 */
@Component("balanceMapper")
public class BalanceMapper extends ChainedRowMapper<Balance> implements ParameterizedRowMapper<Balance> {
    public BalanceMapper() {
    }

    public Balance chainRow(ResultSet rs, int index) throws SQLException {
        Balance balance = new Balance();

        balance.setId(rs.getLong(++index));
        balance.setAccountNumber(rs.getString(++index));
        balance.setDate(rs.getTimestamp(++index));
        balance.setCurrencyCode(rs.getString(++index));
        balance.setAmount(rs.getBigDecimal(++index));
        balance.setCreated(rs.getTimestamp(++index));
        balance.setCreatedby(rs.getLong(++index));
        balance.setUpdated(rs.getTimestamp(++index));
        balance.setUpdatedby(rs.getLong(++index));

        return balance;
    }
}
