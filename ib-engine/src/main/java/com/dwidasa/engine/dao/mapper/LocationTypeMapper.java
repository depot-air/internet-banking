package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.LocationType;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/5/11
 * Time: 7:42 PM
 */
@Component("locationTypeMapper")
public class LocationTypeMapper extends ChainedRowMapper<LocationType> implements ParameterizedRowMapper<LocationType> {
    public LocationTypeMapper() {
    }

    public LocationType chainRow(ResultSet rs, int index) throws SQLException {
        LocationType locationType = new LocationType();

        locationType.setId(rs.getLong(++index));
        locationType.setLocationType(rs.getString(++index));
        locationType.setDescription(rs.getString(++index));
        locationType.setCreated(rs.getTimestamp(++index));
        locationType.setCreatedby(rs.getLong(++index));
        locationType.setUpdated(rs.getTimestamp(++index));
        locationType.setUpdatedby(rs.getLong(++index));

        return locationType;
    }
}
