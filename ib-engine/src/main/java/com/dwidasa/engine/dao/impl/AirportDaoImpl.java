package com.dwidasa.engine.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.dao.AirportDao;
import com.dwidasa.engine.dao.mapper.AirportMapper;
import com.dwidasa.engine.model.Airport;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 6:21 PM
 */
@Repository("airportDao")
public class AirportDaoImpl extends GenericDaoImpl<Airport, Long> implements AirportDao {
    @Autowired
    public AirportDaoImpl(DataSource dataSource, AirportMapper airportMapper) {
        super("m_airport", dataSource);
        defaultMapper = airportMapper;

        insertSql = new StringBuilder()
            .append("insert into m_airport ( ")
            .append("   airport_code, airport_name, airport_city, airport_country, airport_fullname, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :airportCode, :airportName, :airportCity, :airportCountry, :airportFullname, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_airport ")
            .append("set ")
            .append("   airport_code = :airportCode, airport_name = :airportName, airport_city = :airportCity, airport_country = :airportCountry, airport_fullname = :airportFullname, ")
            .append("   created = :created, createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public Airport get(String code) {
        StringBuilder sql = new StringBuilder()
                .append("select ma.* ")
                .append("from m_airport ma ")
                .append("where mc.airport_code = ? ");

        return getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, code);
    }

	@Override
    public List<Airport> getAllSelectedFrom(){
		StringBuilder sql = new StringBuilder()
	        .append("SELECT ap.* FROM m_customer_register mc ") 
	        .append("INNER JOIN m_airport ap ")
	        .append("ON mc.data2 = ap.airport_code ")
	        .append("order by mc.customer_reference ");
	
	    List<Airport> airports = getSimpleJdbcTemplate().query(sql.toString(), defaultMapper);
	    
	    return airports;
    }
	
	@Override
    public List<Airport> getAllSelectedTo(){
		StringBuilder sql = new StringBuilder()
	        .append("SELECT ap.* FROM m_customer_register mc ") 
	        .append("INNER JOIN m_airport ap ")
	        .append("ON mc.data3 = ap.airport_code ")
	        .append("order by mc.customer_reference ");
	
	    List<Airport> airports = getSimpleJdbcTemplate().query(sql.toString(), defaultMapper);
	    
	    return airports;
    }
    
	@Override
	public List<Airport> getAirports(Long customerId, String transactionType, String billerCode, String fromTo) {
		StringBuilder sql = new StringBuilder()
	        .append("SELECT ap.* FROM m_customer_register mc ") 
	        .append("INNER JOIN m_airport ap ");
		if (fromTo.equals("FROM")) {
			sql.append(" ON mc.data2 = ap.airport_code ");
		}  else {	//to
			sql.append(" ON mc.data3 = ap.airport_code ");
		}
        sql.append("WHERE  mc.m_customer_id= ? ") 
	        .append("and mc.transaction_type= ? and mc.data1= ? ")
	        .append("order by mc.customer_reference ");

        List<Airport> airports = getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, customerId, transactionType, billerCode);

        List<Airport> distincts = new ArrayList<Airport>();

        Iterator iterator = airports.iterator();

        while (iterator.hasNext())
        {
            Airport o = (Airport) iterator.next();
            if(!distincts.contains(o)) distincts.add(o);
        }
        
        if (distincts.size() <= 5) {
        	return distincts;	
        } else {
        	List<Airport> fiveAirports = new ArrayList<Airport>();
        	for (int i = 0; i < 5; i++) {
        		fiveAirports.add(airports.get(i));
        	}
        	return fiveAirports;
        }
	}
}
