package com.dwidasa.engine.dao.impl;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.dao.CityDao;
import com.dwidasa.engine.dao.mapper.CityMapper;
import com.dwidasa.engine.model.City;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 6:21 PM
 */
@Repository("cityDao")
public class CityDaoImpl extends GenericDaoImpl<City, Long> implements CityDao {
    @Autowired
    public CityDaoImpl(DataSource dataSource, CityMapper cityMapper) {
        super("m_city", dataSource);
        defaultMapper = cityMapper;

        insertSql = new StringBuilder()
            .append("insert into m_city ( ")
            .append("   city_code, city_name, city_fullname, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :cityCode, :cityName, :cityFullName, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_city ")
            .append("set ")
            .append("   city_code = :cityCode, city_name = :cityName, city_fullname = :cityFullname, ")
            .append("   created = :created, createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public City get(String code) {
        StringBuilder sql = new StringBuilder()
                .append("select mc.* ")
                .append("from m_city mc ")
                .append("where mc.city_code = ? ");

        return getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, code);
    }

}
