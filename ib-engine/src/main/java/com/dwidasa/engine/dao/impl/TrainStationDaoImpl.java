package com.dwidasa.engine.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.dao.TrainStationDao;
import com.dwidasa.engine.dao.mapper.TrainStationMapper;
import com.dwidasa.engine.model.TrainStation;

@Repository("trainStationDao")
public class TrainStationDaoImpl extends GenericDaoImpl<TrainStation, Long> implements TrainStationDao {
	@Autowired
    public TrainStationDaoImpl(DataSource dataSource, TrainStationMapper trainStationMapper) {
        super("m_train_station", dataSource);
        defaultMapper = trainStationMapper;

        insertSql = new StringBuilder()
            .append("insert into m_train_station ( ")
            .append("   city_name, station_code, station_name, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :cityName, :stationCode, :stationName, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_train_station ")
            .append("set ")
            .append("   city_name = :cityName, station_code = :stationCode, stationName = :stationName, ")
            .append("   created = :created, createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }
	
}
