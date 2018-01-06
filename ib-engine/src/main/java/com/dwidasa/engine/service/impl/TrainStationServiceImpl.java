package com.dwidasa.engine.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.dao.TrainStationDao;
import com.dwidasa.engine.model.TrainStation;
import com.dwidasa.engine.service.TrainStationService;

@Service("trainStationService")
public class TrainStationServiceImpl extends GenericServiceImpl<TrainStation, Long> implements TrainStationService {
	@Autowired
	public TrainStationServiceImpl(TrainStationDao trainStationDao) {
		super(trainStationDao);
	}

}
