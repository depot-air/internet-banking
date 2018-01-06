package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.dao.VersionDao;
import com.dwidasa.engine.dao.mapper.VersionMapper;
import com.dwidasa.engine.model.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/6/11
 * Time: 10:51 PM
 */
@Repository("versionDao")
public class VersionDaoImpl extends GenericDaoImpl<Version, Long> implements VersionDao {
    @Autowired
    public VersionDaoImpl(DataSource dataSource, VersionMapper versionMapper) {
        super("m_version", dataSource);
        defaultMapper = versionMapper;

        insertSql = new StringBuilder()
            .append("insert into m_version ( ")
            .append("   table_name, version, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :tableName, :version, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_version ")
            .append("set ")
            .append("   table_name = :tableName, version = :version, created = :created, ")
            .append("   createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public Version get(String tableName) {
        StringBuilder sql = new StringBuilder()
                .append("select mv.* ")
                .append("from m_version mv ")
                .append("where mv.table_name = ? ");

        Version result = null;

        try {
            result = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, tableName);
        } catch (DataAccessException e) {
        	e.printStackTrace();
        }

        return result;
    }
}
