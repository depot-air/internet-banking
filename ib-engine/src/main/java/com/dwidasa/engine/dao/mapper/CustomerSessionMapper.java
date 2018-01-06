package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.CustomerSession;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 9:57 AM
 */
@Component("customerSessionMapper")
public class CustomerSessionMapper extends ChainedRowMapper<CustomerSession>
        implements ParameterizedRowMapper<CustomerSession> {

    public CustomerSessionMapper() {
    }

    public CustomerSession chainRow(ResultSet rs, int index) throws SQLException {
        CustomerSession customerSession = new CustomerSession();

        customerSession.setId(rs.getLong(++index));
        customerSession.setCustomerId(rs.getLong(++index));
        customerSession.setDeviceId(rs.getString(++index));
        customerSession.setSessionId(rs.getString(++index));
        customerSession.setChallenge(rs.getString(++index));
        customerSession.setCreated(rs.getTimestamp(++index));
        customerSession.setCreatedby(rs.getLong(++index));
        customerSession.setUpdated(rs.getTimestamp(++index));
        customerSession.setUpdatedby(rs.getLong(++index));

        return customerSession;
    }
}
