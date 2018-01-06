package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.AppVersion;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: ryoputranto
 * Date: 1/30/12
 * Time: 9:38 AM
 */
@Component("appVersionMapper")
public class AppVersionMapper extends ChainedRowMapper<AppVersion> implements ParameterizedRowMapper<AppVersion> {
    public AppVersionMapper() {
    }

    public AppVersion chainRow(ResultSet rs, int index) throws SQLException {
        AppVersion appVersion = new AppVersion();

        appVersion.setId(rs.getLong(++index));
        appVersion.setDeviceType(rs.getString(++index));
        appVersion.setVersionId(rs.getLong(++index));
        appVersion.setVersion(rs.getString(++index));
        appVersion.setMandatory(rs.getInt(++index));
        appVersion.setChanges(rs.getString(++index));
        appVersion.setUrl(rs.getString(++index));
        appVersion.setCreated(rs.getTimestamp(++index));
        appVersion.setCreatedby(rs.getLong(++index));
        appVersion.setUpdated(rs.getTimestamp(++index));
        appVersion.setUpdatedby(rs.getLong(++index));

        return appVersion;
    }

}
