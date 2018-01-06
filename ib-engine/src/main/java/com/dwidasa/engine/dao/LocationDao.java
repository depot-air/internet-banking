package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.Location;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/5/11
 * Time: 7:25 PM
 */
public interface LocationDao extends GenericDao<Location, Long> {
    /**
     * Get all location by location type based on range of logitude and latitude
     * sorted by nearest location first.
     * @param locationTypeId location type id such as branch, atm, adm, etc.
     * @param loLatitude lower bound for latitude degree of GPS coordinate
     * @param hiLatitude upper bound for latitude degree of GPS coordinate
     * @param loLongitude lower bound for longitude degree of GPS coordinate
     * @param hiLongitude upper bound for longitude degree of GPS coordinate
     * @param size size of set to be returned
     * @return sorted set of {@link Location}
     */
    public List<Location> getAll(Long locationTypeId, Double loLongitude, Double hiLongitude,
                                 Double loLatitude, Double hiLatitude, Integer size);

    /**
     * Get all location by location type based on searchCriteria.
     * @param locationTypeId location type id such as branch, atm, adm, etc.
     * @param searchCriteria this criteria will be compared to these field, street; city; province
     * @param size size of set to be returned
     * @return set of {@link Location} that match to specified criteriatangera
     */
    public List<Location> getAll(Long locationTypeId, String searchCriteria, Integer size);

    public List<Location> getByLocationTypeId(Long locationTypeId);
}
