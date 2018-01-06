package com.dwidasa.engine.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.dao.AirlineDao;
import com.dwidasa.engine.model.Airline;
import com.dwidasa.engine.service.AirlineService;

@Service("airlineService")
public class AirlineServiceImpl extends GenericServiceImpl<Airline, Long> implements AirlineService {
	private AirlineDao airlineDao;
	
    @Autowired
    public AirlineServiceImpl(AirlineDao airlineDao) {
        super(airlineDao);
        this.airlineDao = airlineDao;
    }

}
