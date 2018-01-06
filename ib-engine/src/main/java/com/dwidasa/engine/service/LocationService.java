package com.dwidasa.engine.service;

import com.dwidasa.engine.model.Location;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 8/1/11
 * Time: 9:46 AM
 */
public interface LocationService extends GenericService<Location, Long> {
    /**
     * Get all location by location type based on logitude, latitude and distance,
     * sorted with nearest location first.
     * @param locationTypeId location type id such as branch, atm, adm, etc.
     * @param latitude latitude degree of GPS coordinate
     * @param longitude longitude degree of GPS coordinate
     * @param distance distance in meters
     * @param size size of set to be returned
     * @return sorted set of {@link Location}
     */
    public List<Location> getAll(Long locationTypeId, Double distance, Double longitude,
                                 Double latitude, Integer size);

    /**
     * Get all location by location type based on searchCriteria.
     * @param locationTypeId location type id such as branch, atm and adm, etc.
     * @param searchCriteria this criteria will be compare to these field, street; city; province
     * @param size size of set to be returned, if zero then default value which is ten will be used
     * @return set of {@link Location} that match to specified criteria
     */
    public List<Location> getAll(Long locationTypeId, String searchCriteria, Integer size);

    public List<Location> getByLocationTypeId(Long locationTypeId);
}
