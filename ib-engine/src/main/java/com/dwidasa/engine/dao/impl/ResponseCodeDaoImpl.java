package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.dao.ResponseCodeDao;
import com.dwidasa.engine.dao.mapper.ResponseCodeMapper;
import com.dwidasa.engine.model.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/5/11
 * Time: 8:39 PM
 */
@Repository("responseCodeDao")
public class ResponseCodeDaoImpl extends GenericDaoImpl<ResponseCode, Long> implements ResponseCodeDao {
    @Autowired
    public ResponseCodeDaoImpl(DataSource dataSource, ResponseCodeMapper responseCodeMapper) {
        super("m_response_code", dataSource);
        defaultMapper = responseCodeMapper;

        insertSql = new StringBuilder()
            .append("insert into m_response_code ( ")
            .append("   response_code, action_code, description, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :responseCode, :actionCode, :description, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_response_code ")
            .append("set ")
            .append("   response_code = :responseCode, action_code = :actionCode, description = :description, ")
            .append("   created = :created, createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public ResponseCode get(String responseCode) {
        StringBuilder sql = new StringBuilder()
                .append("select mrc.* ")
                .append("from m_response_code mrc ")
                .append("where mrc.response_code = ? ");
        ResponseCode result = null;

        try {
            result = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, responseCode);
        } catch (DataAccessException e) {
        }

        return result;
    }
}
