package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.PeriodicTask;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 11/14/11
 * Time: 2:38 PM
 */
@Component("periodicTaskMapper")
public class PeriodicTaskMapper extends ChainedRowMapper<PeriodicTask>
        implements ParameterizedRowMapper<PeriodicTask> {

    public PeriodicTaskMapper() {
    }

    @Override
    public PeriodicTask chainRow(ResultSet rs, int index) throws SQLException {
        PeriodicTask t = new PeriodicTask();

        t.setId(rs.getLong(++index));
        t.setPeriodType(rs.getString(++index));
        t.setTaskName(rs.getString(++index));
        t.setClassName(rs.getString(++index));
        t.setExecutionOrder(rs.getInt(++index));
        t.setRunning(rs.getString(++index));
        t.setStatus(rs.getString(++index));
        t.setLastSuccessDate(rs.getTimestamp(++index));
        t.setTrace(rs.getString(++index));
        t.setCreated(rs.getTimestamp(++index));
        t.setCreatedby(rs.getLong(++index));
        t.setUpdated(rs.getTimestamp(++index));
        t.setUpdatedby(rs.getLong(++index));

        return t;
    }
}
