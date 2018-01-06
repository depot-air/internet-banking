package com.dwidasa.engine.service;

import java.util.List;

import com.dwidasa.engine.model.Airport;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/19/11
 * Time: 4:05 PM
 */
public interface AirportService extends GenericService<Airport, Long> {
	public List<Airport> getAirports(Long customerId, String transactionType, String billerCode, String fromTo);
}
