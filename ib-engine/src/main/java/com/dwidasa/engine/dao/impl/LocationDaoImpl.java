package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.dao.LocationDao;
import com.dwidasa.engine.dao.mapper.LocationMapper;
import com.dwidasa.engine.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/5/11
 * Time: 7:25 PM
 */
@Repository("locationDao")
public class LocationDaoImpl extends GenericDaoImpl<Location, Long> implements LocationDao {
    @Autowired
    public LocationDaoImpl(DataSource dataSource, LocationMapper locationMapper) {
        super("m_location", dataSource);
        defaultMapper = locationMapper;

        insertSql = new StringBuilder()
            .append("insert into m_location ( ")
            .append("   m_location_type_id, street, city, province, longitude, latitude, contact_person, ")
            .append("   contact_number, description, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :locationTypeId, :street, :city, :province, :longitude, :latitude, :contactPerson, ")
            .append("   :contactNumber, :description, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_location ")
            .append("set ")
            .append("   m_location_type_id = :locationTypeId, street = :street, city = :city, province = :province, ")
            .append("   longitude = :longitude, latitude = :latitude, contact_person = :contactPerson, ")
            .append("   contact_number = :contactNumber, description = :description, created = :created, ")
            .append("   createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public List<Location> getAll(Long locationTypeId, Double loLongitude, Double hiLongitude,
                                 Double loLatitude, Double hiLatitude, Integer size) {
        StringBuilder sql = new StringBuilder()
                .append("select ml.* ")
                .append("from m_location ml ")
                .append("where ml.m_location_type_id = ? ")
                .append("and ml.longitude between ? and ? ")
                .append("and ml.latitude between ? and ? ")
                .append("order by id ")
                .append("limit ? ");

        return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, locationTypeId, loLongitude,
                hiLongitude, loLatitude, hiLatitude, size);
    }

    /**
     * {@inheritDoc}
     */
    public List<Location> getAll(Long locationTypeId, String searchCriteria, Integer size) {
        StringBuilder sql = new StringBuilder()
                .append("select ml.* ")
                .append("from m_location ml ")
                .append("where m_location_type_id = ? ")
                .append("and lower(coalesce(street,'') || '#' || coalesce(city,'') || '#' || ")
                .append("          coalesce(province,'') || '#' || coalesce(description,'')) like ? ")
                .append("limit ? ");

        return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, locationTypeId,
                '%' + searchCriteria.toLowerCase().trim() + '%', size);
    }

    @Override
    public List<Location> getByLocationTypeId(Long locationTypeId) {
        StringBuilder sql = new StringBuilder()
                .append("select ml.* ")
                .append("from m_location ml ")
                .append("where m_location_type_id = ? ");

        return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, locationTypeId);
    }
}
