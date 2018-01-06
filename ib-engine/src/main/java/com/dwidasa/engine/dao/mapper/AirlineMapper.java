package com.dwidasa.engine.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import com.dwidasa.engine.model.Airline;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 6:22 PM
 */
@Component("airlineMapper")
public class AirlineMapper extends ChainedRowMapper<Airline> implements ParameterizedRowMapper<Airline> {
    public AirlineMapper() {
    }

    public Airline chainRow(ResultSet rs, int index) throws SQLException {
        Airline airline = new Airline();

        airline.setId(rs.getLong(++index));
        airline.setAirlineId(rs.getString(++index));
        airline.setAirlineCode(rs.getString(++index));
        airline.setAirlineName(rs.getString(++index));
        airline.setAirlineType(rs.getString(++index));
        airline.setCreated(rs.getTimestamp(++index));
        airline.setCreatedby(rs.getLong(++index));
        airline.setUpdated(rs.getTimestamp(++index));
        airline.setUpdatedby(rs.getLong(++index));
        airline.setImage(rs.getString(++index));
        return airline;
    }
}
