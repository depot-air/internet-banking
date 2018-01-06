package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.AccountType;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/21/11
 * Time: 3:13 PM
 */
@Component("accountTypeMapper")
public class AccountTypeMapper extends ChainedRowMapper<AccountType> implements ParameterizedRowMapper<AccountType> {
    public AccountTypeMapper() {
    }

    public AccountType chainRow(ResultSet rs, int index) throws SQLException {
        AccountType accountType = new AccountType();

        accountType.setId(rs.getLong(++index));
        accountType.setAccountType(rs.getString(++index));
        accountType.setAccountName(rs.getString(++index));
        accountType.setCreated(rs.getTimestamp(++index));
        accountType.setCreatedby(rs.getLong(++index));
        accountType.setUpdated(rs.getTimestamp(++index));
        accountType.setUpdatedby(rs.getLong(++index));

        return accountType;
    }
}
