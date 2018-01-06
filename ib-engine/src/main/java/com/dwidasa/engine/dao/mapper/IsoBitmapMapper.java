package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.IsoBitmap;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/30/11
 * Time: 10:35 AM
 */
@Component("isoBitmapMapper")
public class IsoBitmapMapper extends ChainedRowMapper<IsoBitmap> implements ParameterizedRowMapper<IsoBitmap> {
    public IsoBitmapMapper() {
    }

    public IsoBitmap chainRow(ResultSet rs, int index) throws SQLException {
        IsoBitmap isoBitmap = new IsoBitmap();

        isoBitmap.setId(rs.getLong(++index));
        isoBitmap.setTransactionTypeId(rs.getLong(++index));
        isoBitmap.setMti(rs.getString(++index));
        isoBitmap.setBitmap(rs.getString(++index));
        isoBitmap.setCustom(rs.getString(++index));
        isoBitmap.setCreated(rs.getTimestamp(++index));
        isoBitmap.setCreatedby(rs.getLong(++index));
        isoBitmap.setUpdated(rs.getTimestamp(++index));
        isoBitmap.setUpdatedby(rs.getLong(++index));

        return isoBitmap;
    }
}
