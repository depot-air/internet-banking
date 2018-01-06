package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.InboxCustomer;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/5/11
 * Time: 7:17 PM
 */
@Component("inboxCustomerMapper")
public class InboxCustomerMapper extends ChainedRowMapper<InboxCustomer>
        implements ParameterizedRowMapper<InboxCustomer> {

    public InboxCustomerMapper() {
    }

    public InboxCustomer chainRow(ResultSet rs, int index) throws SQLException {
        InboxCustomer inboxCustomer = new InboxCustomer();

        inboxCustomer.setId(rs.getLong(++index));
        inboxCustomer.setInboxId(rs.getLong(++index));
        inboxCustomer.setCustomerId(rs.getLong(++index));
        inboxCustomer.setStatus(rs.getInt(++index));
        inboxCustomer.setCreated(rs.getTimestamp(++index));
        inboxCustomer.setCreatedby(rs.getLong(++index));
        inboxCustomer.setUpdated(rs.getTimestamp(++index));
        inboxCustomer.setUpdatedby(rs.getLong(++index));

        return inboxCustomer;
    }
}
