package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.FormConfig;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/7/11
 * Time: 5:56 PM
 */
@Component("formConfigMapper")
public class FormConfigMapper extends ChainedRowMapper<FormConfig> implements ParameterizedRowMapper<FormConfig> {
    public FormConfigMapper() {
    }

    public FormConfig chainRow(ResultSet rs, int index) throws SQLException {
        FormConfig formConfig = new FormConfig();

        formConfig.setId(rs.getLong(++index));
        formConfig.setFormName(rs.getString(++index));
        formConfig.setTokenType(rs.getInt(++index));
        formConfig.setCreated(rs.getTimestamp(++index));
        formConfig.setCreatedby(rs.getLong(++index));
        formConfig.setUpdated(rs.getTimestamp(++index));
        formConfig.setUpdatedby(rs.getLong(++index));

        return formConfig;
    }
}
