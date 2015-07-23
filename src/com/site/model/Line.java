package com.site.model;

public class Line implements java.io.Serializable {

	// Fields

	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	private String endStopName;

	private String lineName;

	private String nextStop;

	private String lineId;

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