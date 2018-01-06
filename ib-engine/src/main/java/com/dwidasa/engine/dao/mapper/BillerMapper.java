package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.Biller;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 6:02 PM
 */
@Component("billerMapper")
public class BillerMapper extends ChainedRowMapper<Biller> implements ParameterizedRowMapper<Biller> {
    public BillerMapper() {
    }

    public Biller chainRow(ResultSet rs, int index) throws SQLException {
        Biller biller = new Biller();

        biller.setId(rs.getLong(++index));
        biller.setTransactionTypeId(rs.getLong(++index));
        biller.setBillerCode(rs.getString(++index));
        biller.setBillerName(rs.getString(++index));
        biller.setCreated(rs.getTimestamp(++index));
        biller.setCreatedby(rs.getLong(++index));
        biller.setUpdated(rs.getTimestamp(++index));
        biller.setUpdatedby(rs.getLong(++index));
        biller.setIsActive(rs.getBoolean(++index));
        return biller;
    }
}
