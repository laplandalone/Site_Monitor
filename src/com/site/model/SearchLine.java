package com.site.model;


/**
 * HospitalNewsT entity. @author MyEclipse Persistence Tools
 */

public class SearchLine implements java.io.Serializable {

	private String stopName;
	
	private String lineNum;
	
	private String stopId;

	public String getStopName() {
		return stopName;
	}

	public void setStopName(String stopName) {
		this.stopName = stopName;
	}

	public String getLineNum() {
		return lineNum;
	}

	public void setLineNum(String lineNum) {
		this.lineNum = lineNum;
	}

	public String getStopId() {
		return stopId;
	}

	public void setStopId(String stopId) {
		this.stopId = stopId;
	}
	 
}