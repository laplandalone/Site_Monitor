package com.dm.yx.model;

import java.util.Date;

/**
 * TeamT entity. @author MyEclipse Persistence Tools
 */

public class TeamT implements java.io.Serializable {

	// Fields

	private String teamId;
	private String hospitalId;
	private String teamName;
	private String expertFlag;
	private String introduce;
	private String state;
	private String createDate;
	private String busRoute;
	private String phoneNum;
	private String address;
	private String x;
	private String y;
	private String teamType;

	// Constructors

	/** default constructor */
	public TeamT() {
	}

	/** minimal constructor */
	public TeamT(String teamId, String hospitalId, String teamName) {
		this.teamId = teamId;
		this.hospitalId = hospitalId;
		this.teamName = teamName;
	}

	/** full constructor */
	public TeamT(String teamId, String hospitalId, String teamName,
			String expertFlag, String introduce, String state, String createDate,
			String busRoute, String phoneNum, String address, String x,
			String y, String teamType) {
		this.teamId = teamId;
		this.hospitalId = hospitalId;
		this.teamName = teamName;
		this.expertFlag = expertFlag;
		this.introduce = introduce;
		this.state = state;
		this.createDate = createDate;
		this.busRoute = busRoute;
		this.phoneNum = phoneNum;
		this.address = address;
		this.x = x;
		this.y = y;
		this.teamType = teamType;
	}

	// Property accessors

	public String getTeamId() {
		return this.teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getHospitalId() {
		return this.hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getTeamName() {
		return this.teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getExpertFlag() {
		return this.expertFlag;
	}

	public void setExpertFlag(String expertFlag) {
		this.expertFlag = expertFlag;
	}

	public String getIntroduce() {
		return this.introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getBusRoute() {
		return this.busRoute;
	}

	public void setBusRoute(String busRoute) {
		this.busRoute = busRoute;
	}

	public String getPhoneNum() {
		return this.phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getX() {
		return this.x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return this.y;
	}

	public void setY(String y) {
		this.y = y;
	}

	public String getTeamType() {
		return this.teamType;
	}

	public void setTeamType(String teamType) {
		this.teamType = teamType;
	}

}