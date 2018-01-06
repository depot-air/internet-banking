package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.Parameter;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/21/11
 * Time: 6:01 PM
 */
@Component("parameterMapper")
public class ParameterMapper extends ChainedRowMapper<Parameter> implements ParameterizedRowMapper<Parameter> {
    public ParameterMapper() {
    }

    public Parameter chainRow(ResultSet rs, int index) throws SQLException {
        Parameter parameter = new Parameter();

        parameter.setId(rs.getLong(++index));
        parameter.setParameterName(rs.getString(++index));
        parameter.setParameterValue(rs.getString(++index));
        parameter.setDescription(rs.getString(++index));
        parameter.setCreated(rs.getTimestamp(++index));
        parameter.setCreatedby(rs.getLong(++index));
        parameter.setUpdated(rs.getTimestamp(++index));
        parameter.setUpdatedby(rs.getLong(++index));

        return parameter;
    }
}
