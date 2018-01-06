package com.dwidasa.engine.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.dao.AirportDao;
import com.dwidasa.engine.model.Airport;
import com.dwidasa.engine.service.AirportService;

@Service("airportService")
public class AirportServiceImpl extends GenericServiceImpl<Airport, Long> implements AirportService {
	private AirportDao airportDao;
	
    @Autowired
    public AirportServiceImpl(AirportDao airportDao) {
        super(airportDao);
        this.airportDao = airportDao;
    }

    public List<Airport> getAirports(Long customerId, String transactionType, String billerCode, String fromTo) {
    	List<Airport> airports = airportDao.getAirports(customerId, transactionType, billerCode, fromTo);
        return airports;
    }
}
