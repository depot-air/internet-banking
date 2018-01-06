package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.CustomerRegister;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/5/11
 * Time: 3:01 PM
 */
@Component("customerRegisterMapper")
public class CustomerRegisterMapper extends ChainedRowMapper<CustomerRegister>
        implements ParameterizedRowMapper<CustomerRegister> {

    public CustomerRegisterMapper() {
    }

    public CustomerRegister chainRow(ResultSet rs, int index) throws SQLException {
        CustomerRegister customerRegister = new CustomerRegister();

        customerRegister.setId(rs.getLong(++index));
        customerRegister.setCustomerId(rs.getLong(++index));
        customerRegister.setTransactionType(rs.getString(++index));
        customerRegister.setCustomerReference(rs.getString(++index));
        customerRegister.setData1(rs.getString(++index));
        customerRegister.setData2(rs.getString(++index));
        customerRegister.setData3(rs.getString(++index));
        customerRegister.setData4(rs.getString(++index));
        customerRegister.setData5(rs.getString(++index));
        customerRegister.setCreated(rs.getTimestamp(++index));
        customerRegister.setCreatedby(rs.getLong(++index));
        customerRegister.setUpdated(rs.getTimestamp(++index));
        customerRegister.setUpdatedby(rs.getLong(++index));

        return customerRegister;
    }
}
