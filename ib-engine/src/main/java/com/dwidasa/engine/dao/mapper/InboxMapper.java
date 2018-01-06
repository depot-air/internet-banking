package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.Inbox;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/5/11
 * Time: 3:29 PM
 */
@Component("inboxMapper")
public class InboxMapper extends ChainedRowMapper<Inbox>
        implements ParameterizedRowMapper<Inbox> {

    public InboxMapper() {
    }

    public Inbox chainRow(ResultSet rs, int index) throws SQLException {
        Inbox inbox = new Inbox();

        inbox.setId(rs.getLong(++index));
        inbox.setTitle(rs.getString(++index));
        inbox.setContent(rs.getString(++index));
        inbox.setStartDate(rs.getTimestamp(++index));
        inbox.setEndDate(rs.getTimestamp(++index));
        inbox.setCreated(rs.getTimestamp(++index));
        inbox.setCreatedby(rs.getLong(++index));
        inbox.setUpdated(rs.getTimestamp(++index));
        inbox.setUpdatedby(rs.getLong(++index));
        inbox.setForAll(rs.getBoolean(++index));

        return inbox;
    }
}
