package com.site.model;


public class Car implements java.io.Serializable {

	// Fields

	private String carNo;
	
	private String deltStops;
	
	private String lineName;
	
	private String lineId;

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public String getDeltStops() {
		return deltStops;
	}

	public void setDeltStops(String deltStops) {
		this.deltStops = deltStops;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	 
	 
}