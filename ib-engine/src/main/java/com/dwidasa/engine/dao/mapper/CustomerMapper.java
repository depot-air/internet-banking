package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.Customer;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/20/11
 * Time: 6:04 PM
 */
@Component("customerMapper")
public class CustomerMapper extends ChainedRowMapper<Customer> implements ParameterizedRowMapper<Customer> {
    public CustomerMapper() {
    }

    public Customer chainRow(ResultSet rs, int index) throws SQLException {
        Customer customer = new Customer();

        customer.setId(rs.getLong(++index));
        customer.setCustomerName(rs.getString(++index));
        customer.setCustomerUsername(rs.getString(++index));
        customer.setEncryptedCustomerPin(rs.getString(++index));
        customer.setCustomerPhone(rs.getString(++index));
        customer.setCustomerEmail(rs.getString(++index));
        customer.setCifNumber(rs.getString(++index));
        customer.setFailedAuthAttempts(rs.getInt(++index));
        customer.setFirstLogin(rs.getString(++index));
        customer.setTokenActivated(rs.getString(++index));
        customer.setLastLogin(rs.getTimestamp(++index));
        customer.setLastTokenId(rs.getString(++index));
        customer.setMobileWebTokenId(rs.getString(++index));
        customer.setStatus(rs.getInt(++index));
        customer.setCreated(rs.getTimestamp(++index));
        customer.setCreatedby(rs.getLong(++index));
        customer.setUpdated(rs.getTimestamp(++index));
        customer.setUpdatedby(rs.getLong(++index));

        return customer;
    }
}
