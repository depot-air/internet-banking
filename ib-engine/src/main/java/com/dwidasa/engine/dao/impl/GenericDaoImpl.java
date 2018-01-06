package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.dao.GenericDao;
import com.dwidasa.engine.model.BaseObject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.util.List;

/**
 * This class serves as the Base class for all other DAOs - namely to hold
 * common CRUD methods that they might all use. You should only need to extend
 * this class when your require custom CRUD logic.
 *
 * @author rk
 * @param <T> a type variable
 * @param <PK> the primary key for that type
 */
public class GenericDaoImpl<T extends BaseObject, PK> extends SimpleJdbcDaoSupport
        implements GenericDao<T, PK> {
    //-- sql related components
    protected final String tableName;

    protected StringBuilder insertSql;
    protected StringBuilder updateSql;
    private final String deleteSql;

    private final String getSql;
    private final String getAllSql;
    
    // sql to update UpdatedBy field (this operation is required before delete, so that delete trigger can recognize who delete the record);
    private final String updateUpdatedBy;

    //-- default resultset to bean mapper
    protected RowMapper<T> defaultMapper;

    public GenericDaoImpl(String tableName, DataSource dataSource) {
        this.tableName = tableName;
        getAllSql = "select * from " + tableName;
        getSql = getAllSql + " where id = ? ";
        deleteSql = "delete from " + tableName + " where id = ? ";
        updateUpdatedBy = "update " + tableName + " set updatedby=? where id = ?";

        setDataSource(dataSource);
    }

    /**
     * {@inheritDoc}
     */
    public List<T> getAll() {
        return getSimpleJdbcTemplate().query(getAllSql, defaultMapper);
    }

    /**
     * {@inheritDoc}
     */
    public T get(PK id) {
        T object = null;

        try {
            object = getSimpleJdbcTemplate().queryForObject(getSql, defaultMapper, id);
        } catch (EmptyResultDataAccessException e) {
            //-- do nothing
        }

        return object;
    }

    /**
     * {@inheritDoc}
     */
    public Boolean exists(PK id) {
        Boolean result = true;
        String sql = "select id from " + tableName + " where id = ? ";
        try {
            getSimpleJdbcTemplate().queryForInt(sql, id);
        } catch (EmptyResultDataAccessException e) {
            result = false;
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public T save(T object) {
    	SqlParameterSource parameters = new BeanPropertySqlParameterSource(object);
        if (object.getId() == null) {
            //-- insert
        	KeyHolder keyHolder = new GeneratedKeyHolder();            
            new NamedParameterJdbcTemplate(getDataSource()).update(insertSql.toString(), parameters, keyHolder);
//            object.setId(keyHolder.getKey().longValue());
            Long id = (Long) keyHolder.getKeyList().get(0).get("id");
            object.setId(id);
        }
        else {
            //-- update
            new NamedParameterJdbcTemplate(getDataSource()).update(updateSql.toString(), parameters);
        }

        return object;
    }

    /**
     * {@inheritDoc}
     */
    public void remove(PK id, Long userId) {
    	updateUpdatedBy(id, userId);
        getSimpleJdbcTemplate().update(deleteSql, id);
    }
    
    /**
     * {@inheritDoc}
     */
    public void updateUpdatedBy(PK id, Long userId) {
    	getSimpleJdbcTemplate().update(updateUpdatedBy, userId, id);
    }

    /**
     * {@inheritDoc}
     */
    public List<T> getCurrentPageRows(int startIndex, int pageSize, List<String> restrictions,
                                      List<String> orders, Object... params) {
        String sql = getAllSql + " ";

        if (restrictions != null && restrictions.size() != 0) {
            sql += "where ";
            for (String restriction : restrictions) {
                sql += restriction;
                sql += " and ";
            }

            sql = sql.substring(0, sql.length()-4);
        }

        if (orders != null && orders.size() != 0) {
            sql += "order by ";
            for (String order : orders) {
                sql += order;
                sql += ", ";
            }

            sql = sql.substring(0, sql.length()-2);
        }

        sql += " limit ? offset ?";
        int pageNo = startIndex / pageSize + (startIndex % pageSize == 0 ? 0 : 1) + 1;

        Object[] newParams;
        int index;

        if (params != null) {
            newParams = new Object[params.length + 2];
            index = params.length;

            System.arraycopy(params, 0, newParams, 0, params.length);
        }
        else {
            newParams = new Object[2];
            index = 0;
        }

        newParams[index++] = pageSize;
        newParams[index] = (pageNo - 1) * pageSize;

        return getSimpleJdbcTemplate().query(sql, defaultMapper, newParams);
    }

    /**
     * {@inheritDoc}
     */
    public int getRowCount(List<String> restrictions, Object... params) {
        String sql = "select count(*) from " + tableName + " ";

        if (restrictions != null && restrictions.size() != 0) {
            sql += "where ";
            for (String restriction : restrictions) {
                sql += restriction;
                sql += " and ";
            }

            sql = sql.substring(0, sql.length()-4);
        }

        return getSimpleJdbcTemplate().queryForInt(sql, params);
    }
}
