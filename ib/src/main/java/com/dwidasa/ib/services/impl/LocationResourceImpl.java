package com.dwidasa.ib.services.impl;

import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.Location;
import com.dwidasa.engine.service.LocationService;
import com.dwidasa.ib.annotations.PublicPage;
import com.dwidasa.ib.services.LocationResource;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/13/11
 * Time: 11:45 AM
 */
@PublicPage
public class LocationResourceImpl implements LocationResource {
    @Inject
    private LocationService locationService;

    public LocationResourceImpl() {
    }

    public String getLocations(Long customerId, String sessionId, Long locationTypeId, String distance,
            String longitude, String latitude, Integer size) {

        List<Location> locs = locationService.getAll(locationTypeId, Double.valueOf(distance),
                Double.valueOf(longitude), Double.valueOf(latitude), size);
        return PojoJsonMapper.toJson(locs);
    }

    public String getLocations(Long locationTypeId, String distance,
            String longitude, String latitude, Integer size) {

        List<Location> locs = locationService.getAll(locationTypeId, Double.valueOf(distance),
                Double.valueOf(longitude), Double.valueOf(latitude), size);
        return PojoJsonMapper.toJson(locs);
    }

    public String getLocations(Long customerId, String sessionId, Long locationTypeId, String searchCriteria,
            Integer size) {

        List<Location> locs = locationService.getAll(locationTypeId, searchCriteria, size);
        return PojoJsonMapper.toJson(locs);
    }

    public String getLocations(Long locationTypeId, String searchCriteria, Integer size) {
        List<Location> locs = locationService.getAll(locationTypeId, searchCriteria, size);
        return PojoJsonMapper.toJson(locs);
    }
}
