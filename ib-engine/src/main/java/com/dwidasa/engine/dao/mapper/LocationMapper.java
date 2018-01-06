package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.Location;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/5/11
 * Time: 7:30 PM
 */
@Component("locationMapper")
public class LocationMapper extends ChainedRowMapper<Location> implements ParameterizedRowMapper<Location>  {
    public LocationMapper() {
    }

    public Location chainRow(ResultSet rs, int index) throws SQLException {
        Location location = new Location();

        location.setId(rs.getLong(++index));
        location.setLocationTypeId(rs.getLong(++index));
        location.setStreet(rs.getString(++index));
        location.setCity(rs.getString(++index));
        location.setProvince(rs.getString(++index));
        location.setLongitude(rs.getDouble(++index));
        location.setLatitude(rs.getDouble(++index));
        location.setContactPerson(rs.getString(++index));
        location.setContactNumber(rs.getString(++index));
        location.setDescription(rs.getString(++index));
        location.setCreated(rs.getTimestamp(++index));
        location.setCreatedby(rs.getLong(++index));
        location.setUpdated(rs.getTimestamp(++index));
        location.setUpdatedby(rs.getLong(++index));

        return location;
    }
}
