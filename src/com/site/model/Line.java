package com.site.model;


/**
 * HospitalNewsT entity. @author MyEclipse Persistence Tools
 */

public class Line implements java.io.Serializable {

	// Fields

	private String endStopName;
	
	private String lineName;
	
	private String nextStop;
	
	

	public String getNextStop() {
		return nextStop;
	}

	public void setNextStop(String nextStop) {
		this.nextStop = nextStop;
	}

	public String getEndStopName() {
		return endStopName;
	}

	public void setEndStopName(String endStopName) {
		this.endStopName = endStopName;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}
	
	 
}