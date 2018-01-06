package com.dwidasa.engine.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import com.dwidasa.engine.model.IbMerchant;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/20/11
 * Time: 6:04 PM
 */
@Component("ibMerchantMapper")
public class IbMerchantMapper extends ChainedRowMapper<IbMerchant> implements ParameterizedRowMapper<IbMerchant> {
    public IbMerchantMapper() {
    }

    public IbMerchant chainRow(ResultSet rs, int index) throws SQLException {
    	IbMerchant ibToken = new IbMerchant();

        ibToken.setId(rs.getLong(++index));
        ibToken.setCustomerId(rs.getLong(++index));
        ibToken.setTerminalId(rs.getString(++index));
        ibToken.setStatus(rs.getString(++index));        
        ibToken.setCreated(rs.getTimestamp(++index));
        ibToken.setCreatedby(rs.getLong(++index));
        ibToken.setUpdated(rs.getTimestamp(++index));
        ibToken.setUpdatedby(rs.getLong(++index));
        ibToken.setSerialNumber(rs.getString(++index));
        
        return ibToken;
    }
}
