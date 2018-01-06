package com.dwidasa.engine.model.train;

import java.util.Date;
import java.util.List;

public class TrainSchedule {
	private String trainNo;
	private String trainName;
	private Date departureDate;
	private Date arrivalDate;
	private List<TrainSubclass> subclassList;
	
	public String getTrainNo() {
		return trainNo;
	}
	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}
	public String getTrainName() {
		return trainName;
	}
	public void setTrainName(String trainName) {
		this.trainName = trainName;
	}
	public Date getDepartureDate() {
		return departureDate;
	}
	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}
	public Date getArrivalDate() {
		return arrivalDate;
	}
	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}
	public List<TrainSubclass> getSubclassList() {
		return subclassList;
	}
	public void setSubclassList(List<TrainSubclass> subclassList) {
		this.subclassList = subclassList;
	}
	
	
}
