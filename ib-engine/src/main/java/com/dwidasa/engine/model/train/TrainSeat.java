package com.dwidasa.engine.model.train;

public class TrainSeat {
	private String seatRow;
	private String seatColumn;
	private String subclass;
	private int status; //0=kosong, 1=sudah ditempati, 2=kursi ditempati sendiri
	
	public String getSeatRow() {
		return seatRow;
	}
	public void setSeatRow(String seatRow) {
		this.seatRow = seatRow;
	}
	public String getSeatColumn() {
		return seatColumn;
	}
	public void setSeatColumn(String seatColumn) {
		this.seatColumn = seatColumn;
	}
	public String getSubclass() {
		return subclass;
	}
	public void setSubclass(String subclass) {
		this.subclass = subclass;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
