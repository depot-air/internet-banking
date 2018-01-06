package com.dwidasa.engine.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import com.dwidasa.engine.model.City;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 6:22 PM
 */
@Component("cityMapper")
public class CityMapper extends ChainedRowMapper<City> implements ParameterizedRowMapper<City> {
    public CityMapper() {
    }

    public City chainRow(ResultSet rs, int index) throws SQLException {
        City city = new City();

        city.setId(rs.getLong(++index));
        city.setCityCode(rs.getString(++index));
        city.setCityName(rs.getString(++index));
        city.setCityFullname(rs.getString(++index));
        city.setCreated(rs.getTimestamp(++index));
        city.setCreatedby(rs.getLong(++index));
        city.setUpdated(rs.getTimestamp(++index));
        city.setUpdatedby(rs.getLong(++index));
        return city;
    }
}
