package com.dwidasa.engine.service.impl;

import com.dwidasa.engine.dao.LocationTypeDao;
import com.dwidasa.engine.model.LocationType;
import com.dwidasa.engine.service.LocationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 8/4/11
 * Time: 10:48 AM
 */
@Service("locationTypeService")
public class LocationTypeServiceImpl extends GenericServiceImpl<LocationType, Long> implements LocationTypeService {
    @Autowired
    public LocationTypeServiceImpl(LocationTypeDao locationTypeDao) {
        super(locationTypeDao);
    }
}
