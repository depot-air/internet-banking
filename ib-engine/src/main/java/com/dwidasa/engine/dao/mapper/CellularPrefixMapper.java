package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.CellularPrefix;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/14/11
 * Time: 9:27 PM
 */
@Component("cellularPrefixMapper")
public class CellularPrefixMapper extends ChainedRowMapper<CellularPrefix>
        implements ParameterizedRowMapper<CellularPrefix> {

    public CellularPrefixMapper() {
    }

    public CellularPrefix chainRow(ResultSet rs, int index) throws SQLException {
        CellularPrefix cp = new CellularPrefix();

        cp.setId(rs.getLong(++index));
        cp.setBillerProductId(rs.getLong(++index));
        cp.setPrefix(rs.getString(++index));
        cp.setStatus(rs.getInt(++index));
        cp.setCreated(rs.getTimestamp(++index));
        cp.setCreatedby(rs.getLong(++index));
        cp.setUpdated(rs.getTimestamp(++index));
        cp.setUpdatedby(rs.getLong(++index));

        return cp;
    }
}
