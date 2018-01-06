package com.dwidasa.engine.model.train;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiBookTicket {

	private String type; //A=Adult, C=Children, I=Infant
	private String firstName;
	private String lastName;
	private String idNumber;
	private String phone;
	private Date birthDate;
	private String member; //Y=Yes, N=No
	private String label;
	
	//seat
	private String wagonCode;
	private String wagonNumber;
	private String seatRow;
	private String seatCol;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public String getMember() {
		return member;
	}
	public void setMember(String member) {
		this.member = member;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
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
	public String getSeatRow() {
		return seatRow;
	}
	public void setSeatRow(String seatRow) {
		this.seatRow = seatRow;
	}
	public String getSeatCol() {
		return seatCol;
	}
	public void setSeatCol(String seatCol) {
		this.seatCol = seatCol;
	}
	
	public String getStrName() {
		if (firstName == null) firstName = "";
		if (lastName == null) lastName = "";
		String name = firstName;
		if (name.length() > 0) {
			name = name + " ";
		}
		name = name + lastName;
		return name;
	}
	
	public String getStrType() {
		if ("A".equals(type)) return "Dewasa";
		if ("C".equals(type)) return "Anak";
		if ("I".equals(type)) return "Infant";
		return "";
	}
	
	public String getStrPhone() {
		if (phone == null) return "";
		return "+62 " + phone;
	}
	
	public String getStrSeat() {
		return wagonCode + "-" + wagonNumber + "/" + seatRow +  seatCol;
	}
	
}
