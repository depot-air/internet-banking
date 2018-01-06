package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.CustomerDevice;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/4/11
 * Time: 12:08 PM
 */
@Component("customerDeviceMapper")
public class CustomerDeviceMapper extends ChainedRowMapper<CustomerDevice>
        implements ParameterizedRowMapper<CustomerDevice> {

    public CustomerDeviceMapper() {
    }

    public CustomerDevice chainRow(ResultSet rs, int index) throws SQLException {
        CustomerDevice customerDevice = new CustomerDevice();

        customerDevice.setId(rs.getLong(++index));
        customerDevice.setCustomerId(rs.getLong(++index));
        customerDevice.setDeviceId(rs.getString(++index));
        customerDevice.setTerminalId(rs.getString(++index));
        customerDevice.setIme(rs.getString(++index));
        customerDevice.setEncryptedActivatePin(rs.getString(++index));
        customerDevice.setExpiredDate(rs.getDate(++index));
        customerDevice.setStatus(rs.getInt(++index));
        customerDevice.setCreated(rs.getTimestamp(++index));
        customerDevice.setCreatedby(rs.getLong(++index));
        customerDevice.setUpdated(rs.getTimestamp(++index));
        customerDevice.setUpdatedby(rs.getLong(++index));
        customerDevice.setSoftToken(rs.getBoolean(++index));
        return customerDevice;
    }
}
