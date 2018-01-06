package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.CustomerData;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/5/11
 * Time: 2:46 PM
 */
@Component("customerDataMapper")
public class CustomerDataMapper extends ChainedRowMapper<CustomerData> implements ParameterizedRowMapper<CustomerData> {
    public CustomerDataMapper() {
    }

    public CustomerData chainRow(ResultSet rs, int index) throws SQLException {
        CustomerData customerData = new CustomerData();

        customerData.setId(rs.getLong(++index));
        customerData.setCustomerId(rs.getLong(++index));
        customerData.setCustomerFax(rs.getString(++index));
        customerData.setCustomerAddress(rs.getString(++index));
        customerData.setRegisterDate(rs.getTimestamp(++index));
        customerData.setPersonalIdentityNumber(rs.getString(++index));
        customerData.setDescription(rs.getString(++index));
        customerData.setLimitAmount(rs.getBigDecimal(++index));
        customerData.setCreated(rs.getTimestamp(++index));
        customerData.setCreatedby(rs.getLong(++index));
        customerData.setUpdated(rs.getTimestamp(++index));
        customerData.setUpdatedby(rs.getLong(++index));

        return customerData;
    }
}
