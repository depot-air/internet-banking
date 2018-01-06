package com.dwidasa.engine.model.train;

import java.util.List;

public class ApiGetSchedule {
	private int errorCode;
	private List<TrainSchedule> trainSchedule;
	
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public List<TrainSchedule> getTrainSchedule() {
		return trainSchedule;
	}
	public void setTrainSchedule(List<TrainSchedule> trainSchedule) {
		this.trainSchedule = trainSchedule;
	}
	
	
}
