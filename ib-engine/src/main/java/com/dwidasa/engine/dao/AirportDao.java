package com.dwidasa.engine.dao;

import java.util.List;

import com.dwidasa.engine.model.Airport;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 6:24 PM
 */
public interface AirportDao extends GenericDao<Airport, Long> {
	public List<Airport> getAllSelectedFrom();
	public List<Airport> getAllSelectedTo();
	public List<Airport> getAirports(Long customerId, String transactionType, String billerCode, String fromTo);
}
