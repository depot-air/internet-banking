package com.dwidasa.engine.service.impl;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.LocationDao;
import com.dwidasa.engine.model.Location;
import com.dwidasa.engine.service.LocationService;
import com.dwidasa.engine.util.location.LocationCalc;
import com.dwidasa.engine.util.location.Locations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 8/1/11
 * Time: 9:47 AM
 */
@Service("locationService")
public class LocationServiceImpl extends GenericServiceImpl<Location, Long> implements LocationService {
    @Autowired
    private LocationDao locationDao;

    @Autowired
    public LocationServiceImpl(LocationDao locationDao) {
        super(locationDao);
    }

    /**
     * {@inheritDoc}
     */
    public List<Location> getAll(Long locationTypeId, Double distance, Double longitude, Double latitude,
                                 Integer size) {
        distance = (distance == 0 ? Constants.MAX_DISTANCE : distance);
        size = (size == 0 ? Integer.valueOf(Constants.MAX_SIZE) : size);

        Double latDelta = LocationCalc.getLatitudeDifference(distance);
        Double longDelta = LocationCalc.getLongitudeDifference(latitude, longitude, distance);

        List<Location> mls = locationDao.getAll(locationTypeId,
                longitude - longDelta, longitude + longDelta,
                latitude - latDelta, latitude + latDelta, size);

        Locations locs = new Locations(latitude, longitude, mls);
        List<Location> locations = locs.getSortedLocation();

        for (Location location : locations) {
            location.setDistance(locs.getDistance(location));
        }

        return locations;
    }

    /**
     * {@inheritDoc}
     */
    public List<Location> getAll(Long locationTypeId, String searchCriteria, Integer size) {
        size = (size == 0 ? Integer.valueOf(Constants.MAX_SIZE) : size);
        return locationDao.getAll(locationTypeId, searchCriteria, size);
    }

    @Override
    public List<Location> getByLocationTypeId(Long locationTypeId) {
        return locationDao.getByLocationTypeId(locationTypeId);
    }
}
