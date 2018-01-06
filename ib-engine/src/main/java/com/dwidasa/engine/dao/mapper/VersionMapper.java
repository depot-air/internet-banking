package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.Version;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/6/11
 * Time: 10:53 PM
 */
@Component("versionMapper")
public class VersionMapper extends ChainedRowMapper<Version> implements ParameterizedRowMapper<Version> {
    public VersionMapper() {
    }

    public Version chainRow(ResultSet rs, int index) throws SQLException {
        Version version = new Version();

        version.setId(rs.getLong(++index));
        version.setTableName(rs.getString(++index));
        version.setVersion(rs.getInt(++index));
        version.setCreated(rs.getTimestamp(++index));
        version.setCreatedby(rs.getLong(++index));
        version.setUpdated(rs.getTimestamp(++index));
        version.setUpdatedby(rs.getLong(++index));

        return version;
    }
}
