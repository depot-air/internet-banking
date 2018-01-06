package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.CustomerAccount;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class for mapping resultset to bean. AFAIK, Spring provide generic implementation of
 * <code>ParameterizedRowMapper</code> interface named <code>ParameterizedBeanPropertyRowMapper</code>
 * <p/>
 * Javadoc of this API exposed that this implementation class designed for ease of used rather than high
 * performance. This is enough reason for us not to used it.
 *
 * @see org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper
 * @author rk
 */
@Component("customerAccountMapper")
public class CustomerAccountMapper extends ChainedRowMapper<CustomerAccount>
        implements ParameterizedRowMapper<CustomerAccount> {

    public CustomerAccountMapper() {
    }

    public CustomerAccount chainRow(ResultSet rs, int index) throws SQLException {
        CustomerAccount customerAccount = new CustomerAccount();

        customerAccount.setId(rs.getLong(++index));
        customerAccount.setAccountTypeId(rs.getLong(++index));
        customerAccount.setCurrencyId(rs.getLong(++index));
        customerAccount.setCustomerId(rs.getLong(++index));
        customerAccount.setProductId(rs.getLong(++index));
        customerAccount.setAccountNumber(rs.getString(++index));
        customerAccount.setCardNumber(rs.getString(++index));
        customerAccount.setIsDefault(rs.getString(++index));
        customerAccount.setStatus(rs.getInt(++index));
        customerAccount.setCreated(rs.getTimestamp(++index));
        customerAccount.setCreatedby(rs.getLong(++index));
        customerAccount.setUpdated(rs.getTimestamp(++index));
        customerAccount.setUpdatedby(rs.getLong(++index));

        return customerAccount;
    }
}
