package com.dwidasa.engine.model.train;

import java.util.List;

public class ApiChooseSeat {

	private int rowCount;
	private int colCount;
	private String selectedWagonCode;
	private String selectedWagonNumber;
	private String selectedSeat; // separated by comma
	private List<TrainWagon> trainWagonList;

	// private List<TrainSelectedSeat> trainSelectedSeatList;
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

	public String getSelectedWagonCode() {
		return selectedWagonCode;
	}

	public void setSelectedWagonCode(String selectedWagonCode) {
		this.selectedWagonCode = selectedWagonCode;
	}

	public String getSelectedWagonNumber() {
		return selectedWagonNumber;
	}

	public void setSelectedWagonNumber(String selectedWagonNumber) {
		this.selectedWagonNumber = selectedWagonNumber;
	}

	public String getSelectedSeat() {
		return selectedSeat;
	}

	public void setSelectedSeat(String selectedSeat) {
		this.selectedSeat = selectedSeat;
	}

	public List<TrainWagon> getTrainWagonList() {
		return trainWagonList;
	}

	public void setTrainWagonList(List<TrainWagon> trainWagonList) {
		this.trainWagonList = trainWagonList;
	}
	// public List<TrainSelectedSeat> getTrainSelectedSeatList() {
	// return trainSelectedSeatList;
	// }
	// public void setTrainSelectedSeatList(
	// List<TrainSelectedSeat> trainSelectedSeatList) {
	// this.trainSelectedSeatList = trainSelectedSeatList;
	// }
	//

}
