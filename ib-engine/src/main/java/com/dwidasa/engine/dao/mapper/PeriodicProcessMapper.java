package com.dwidasa.engine.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import com.dwidasa.engine.model.PeriodicProcess;

@Component("periodicProcessMapper")
public class PeriodicProcessMapper extends ChainedRowMapper<PeriodicProcess>
		implements ParameterizedRowMapper<PeriodicProcess> {

	public PeriodicProcessMapper() {
	}

	@Override
	public PeriodicProcess chainRow(ResultSet rs, int index) throws SQLException {
		PeriodicProcess t = new PeriodicProcess();
		t.setId(rs.getLong(++index));
		t.setPeriodType(rs.getString(++index));
		t.setProcessDate(rs.getDate(++index));
		t.setStartTime(rs.getTimestamp(++index));
		t.setFinishTime(rs.getTimestamp(++index));
		t.setStatus(rs.getString(++index));
		t.setCreated(rs.getTimestamp(++index));
		t.setCreatedby(rs.getLong(++index));
		t.setUpdated(rs.getTimestamp(++index));
		t.setUpdatedby(rs.getLong(++index));
		return t;
	}
}
