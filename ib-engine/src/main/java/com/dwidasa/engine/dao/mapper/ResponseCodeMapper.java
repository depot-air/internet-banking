package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.ResponseCode;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/5/11
 * Time: 8:41 PM
 */
@Component("responseCodeMapper")
public class ResponseCodeMapper extends ChainedRowMapper<ResponseCode> implements ParameterizedRowMapper<ResponseCode> {
    public ResponseCodeMapper() {
    }

    public ResponseCode chainRow(ResultSet rs, int index) throws SQLException {
        ResponseCode responseCode = new ResponseCode();

        responseCode.setId(rs.getLong(++index));
        responseCode.setResponseCode(rs.getString(++index));
        responseCode.setActionCode(rs.getString(++index));
        responseCode.setDescription(rs.getString(++index));
        responseCode.setCreated(rs.getTimestamp(++index));
        responseCode.setCreatedby(rs.getLong(++index));
        responseCode.setUpdated(rs.getTimestamp(++index));
        responseCode.setUpdatedby(rs.getLong(++index));

        return responseCode;
    }
}
