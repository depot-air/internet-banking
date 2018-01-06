package com.dwidasa.engine.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import com.dwidasa.engine.model.TrainStation;

@Component("trainStationMapper")
public class TrainStationMapper extends ChainedRowMapper<TrainStation> implements ParameterizedRowMapper<TrainStation> {
    public TrainStationMapper() {
    }

    public TrainStation chainRow(ResultSet rs, int index) throws SQLException {
    	TrainStation row = new TrainStation();

        row.setId(rs.getLong(++index));
        row.setCityName(rs.getString(++index));
        row.setStationCode(rs.getString(++index));
        row.setStationName(rs.getString(++index));
        row.setCreated(rs.getTimestamp(++index));
        row.setCreatedby(rs.getLong(++index));
        row.setUpdated(rs.getTimestamp(++index));
        row.setUpdatedby(rs.getLong(++index));
        return row;
    }
}

