package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.Ftp;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 11/14/11
 * Time: 11:21 AM
 */
@Component("ftpMapper")
public class FtpMapper extends ChainedRowMapper<Ftp> implements ParameterizedRowMapper<Ftp> {
    public FtpMapper() {
    }

    @Override
    public Ftp chainRow(ResultSet rs, int index) throws SQLException {
        Ftp f = new Ftp();

        f.setId(rs.getLong(++index));
        f.setServerAddress(rs.getString(++index));
        f.setServerPort(rs.getInt(++index));
        f.setUsername(rs.getString(++index));
        f.setPassword(rs.getString(++index));
        f.setTransferMode(rs.getString(++index));
        f.setLocalFolder(rs.getString(++index));
        f.setRemoteFolder(rs.getString(++index));
        f.setDescription(rs.getString(++index));
        f.setCreated(rs.getTimestamp(++index));
        f.setCreatedby(rs.getLong(++index));
        f.setUpdated(rs.getTimestamp(++index));
        f.setUpdatedby(rs.getLong(++index));

        return f;
    }
}
