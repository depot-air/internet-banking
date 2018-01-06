package com.dwidasa.engine.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import com.dwidasa.engine.model.IbToken;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/20/11
 * Time: 6:04 PM
 */
@Component("ibTokenMapper")
public class IbTokenMapper extends ChainedRowMapper<IbToken> implements ParameterizedRowMapper<IbToken> {
    public IbTokenMapper() {
    }

    public IbToken chainRow(ResultSet rs, int index) throws SQLException {
    	IbToken ibToken = new IbToken();

        ibToken.setId(rs.getLong(++index));
        ibToken.setSerialNumber(rs.getString(++index));
        ibToken.setCustomerId(rs.getLong(++index));
        ibToken.setStatus(rs.getString(++index));        
        ibToken.setCreated(rs.getTimestamp(++index));
        ibToken.setCreatedby(rs.getLong(++index));
        ibToken.setUpdated(rs.getTimestamp(++index));
        ibToken.setUpdatedby(rs.getLong(++index));

        return ibToken;
    }
}
