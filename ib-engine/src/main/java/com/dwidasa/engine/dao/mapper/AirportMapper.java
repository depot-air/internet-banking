package com.dwidasa.engine.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import com.dwidasa.engine.model.Airport;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 6:22 PM
 */
@Component("airportMapper")
public class AirportMapper extends ChainedRowMapper<Airport> implements ParameterizedRowMapper<Airport> {
    public AirportMapper() {
    }

    public Airport chainRow(ResultSet rs, int index) throws SQLException {
        Airport airport = new Airport();

        airport.setId(rs.getLong(++index));
        airport.setAirportCode(rs.getString(++index));
        airport.setAirportName(rs.getString(++index));
        airport.setAirportCountry(rs.getString(++index));
        airport.setAirportCity(rs.getString(++index));
        airport.setAirportFullname(rs.getString(++index));
        airport.setCreated(rs.getTimestamp(++index));
        airport.setCreatedby(rs.getLong(++index));
        airport.setUpdated(rs.getTimestamp(++index));
        airport.setUpdatedby(rs.getLong(++index));
        return airport;
    }
}
