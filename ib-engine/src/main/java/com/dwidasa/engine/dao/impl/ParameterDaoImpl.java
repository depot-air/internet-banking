package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.dao.ParameterDao;
import com.dwidasa.engine.dao.mapper.ParameterMapper;
import com.dwidasa.engine.model.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/21/11
 * Time: 6:02 PM
 */
@Repository("parameterDao")
public class ParameterDaoImpl extends GenericDaoImpl<Parameter, Long> implements ParameterDao {
    @Autowired
    public ParameterDaoImpl(DataSource dataSource, ParameterMapper parameterMapper) {
        super("m_parameter", dataSource);
        defaultMapper = parameterMapper;

        insertSql = new StringBuilder()
            .append("insert into m_parameter ( ")
            .append("   parameter_name, parameter_value, description, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :parameterName, :parameterValue, :description, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_parameter ")
            .append("set ")
            .append("   parameter_name = :parameterName, parameter_value = :parameterValue, ")
            .append("   description = :description, created = :created, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public Parameter get(String parameterName) {
        StringBuilder sql = new StringBuilder()
                .append("select mp.* ")
                .append("from m_parameter mp ")
                .append("where mp.parameter_name = ? ");

        Parameter parameter = null;

        try {
            parameter = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, parameterName);
        } catch (EmptyResultDataAccessException e) {
            //-- actually do nothing
            e.printStackTrace();
        }

        return parameter;
    }

	@Override
	public String getParameterValueByName(String name) {
		String sqlSelect = "select parameter_value from m_parameter where parameter_name=?";
		return getJdbcTemplate().queryForObject(sqlSelect, new Object[] {name}, String.class);
	}

	@Override
	public void setParameterValueByName(String name, String value) {
		String sqlUpdate = "update m_parameter set parameter_value=? where parameter_name=?";
		getJdbcTemplate().update(sqlUpdate, new Object[] {value, name});
	}
}
