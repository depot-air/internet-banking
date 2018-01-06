package com.dwidasa.engine.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import com.dwidasa.engine.model.AccountException;

@Component("accountExceptionMapper")
public class AccountExceptionMapper extends ChainedRowMapper<AccountException> implements ParameterizedRowMapper<AccountException> {
    public AccountExceptionMapper() {
    }

    public AccountException chainRow(ResultSet rs, int index) throws SQLException {
    	AccountException accountException = new AccountException();

    	accountException.setId(rs.getLong(++index));
    	accountException.setAccountNumber(rs.getString(++index));      
        accountException.setCreated(rs.getTimestamp(++index));
        accountException.setCreatedby(rs.getLong(++index));
        accountException.setUpdated(rs.getTimestamp(++index));
        accountException.setUpdatedby(rs.getLong(++index));
        
        return accountException;
    }

}
