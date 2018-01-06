package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.dao.LocationTypeDao;
import com.dwidasa.engine.dao.mapper.LocationTypeMapper;
import com.dwidasa.engine.model.LocationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/5/11
 * Time: 7:41 PM
 */
@Repository("locationType")
public class LocationTypeDaoImpl extends GenericDaoImpl<LocationType, Long> implements LocationTypeDao {
    @Autowired
    public LocationTypeDaoImpl(DataSource dataSource, LocationTypeMapper locationTypeMapper) {
        super("m_location_type", dataSource);
        defaultMapper = locationTypeMapper;

        insertSql = new StringBuilder()
            .append("insert into m_location_type ( ")
            .append("   location_type, description, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :locationType, :description, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_location_type ")
            .append("set ")
            .append("   location_type = :locationType, description = :description, created = :created, ")
            .append("   createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }
}
