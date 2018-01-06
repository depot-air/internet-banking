package com.dwidasa.engine.model.train;

import java.util.Map;

public class TrainWagon {
	private String wagonCode;
	private String wagonNumber;
	private int rowCount;
	private int colCount;
	private String noSeat;
	private String filledSeat;
	private String activeSeat;
//	private List<TrainSeat> trainSeatList;
	
	private Map<String, Integer> trainSeatMap;
	
	public String getWagonCode() {
		return wagonCode;
	}
	public void setWagonCode(String wagonCode) {
		this.wagonCode = wagonCode;
	}
	public String getWagonNumber() {
		return wagonNumber;
	}
	public void setWagonNumber(String wagonNumber) {
		this.wagonNumber = wagonNumber;
	}
    public Map<String, Integer> getTrainSeatMap() {
		return trainSeatMap;
	}
	public void setTrainSeatMap(Map<String, Integer> trainSeatMap) {
		this.trainSeatMap = trainSeatMap;
	}
	//	public List<TrainSeat> getTrainSeatList() {
//		return trainSeatList;
//	}
//	public void setTrainSeatList(List<TrainSeat> trainSeatList) {
//		this.trainSeatList = trainSeatList;
//	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public int getColCount() {
		return colCount;
	}
	public void setColCount(int colCount) {
		this.colCount = colCount;
	}
	public String getNoSeat() {
		return noSeat;
	}
	public void setNoSeat(String noSeat) {
		this.noSeat = noSeat;
	}
	public String getFilledSeat() {
		return filledSeat;
	}
	public void setFilledSeat(String filledSeat) {
		this.filledSeat = filledSeat;
	}
	public String getActiveSeat() {
		return activeSeat;
	}
	public void setActiveSeat(String activeSeat) {
		this.activeSeat = activeSeat;
	}
}
