package com.dm.yx.model;
// default package

import java.math.BigDecimal;
import java.util.Date;

/**
 * WakeT entity. @author MyEclipse Persistence Tools
 */

public class WakeT implements java.io.Serializable {

	// Fields

	private BigDecimal wakeId;
	private String wakeType;
	private String wakeValue;
	private String wakeContent;
	private String createDate;
	private String wakeDate;
	private String state;
	private String wakeName;
	private String wakeFlag;
	private String userId;

	// Constructors

	/** default constructor */
	public WakeT() {
	}

	/** minimal constructor */
	public WakeT(BigDecimal wakeId, String wakeType, String createDate,
			String wakeDate, String state, String wakeFlag) {
		this.wakeId = wakeId;
		this.wakeType = wakeType;
		this.createDate = createDate;
		this.wakeDate = wakeDate;
		this.state = state;
		this.wakeFlag = wakeFlag;
	}

	/** full constructor */
	public WakeT(BigDecimal wakeId, String wakeType, String wakeValue,
			String wakeContent, String createDate, String wakeDate, String state,
			String wakeName, String wakeFlag, String userId) {
		this.wakeId = wakeId;
		this.wakeType = wakeType;
		this.wakeValue = wakeValue;
		this.wakeContent = wakeContent;
		this.createDate = createDate;
		this.wakeDate = wakeDate;
		this.state = state;
		this.wakeName = wakeName;
		this.wakeFlag = wakeFlag;
		this.userId = userId;
	}

	// Property accessors

	public BigDecimal getWakeId() {
		return this.wakeId;
	}

	public void setWakeId(BigDecimal wakeId) {
		this.wakeId = wakeId;
	}

	public String getWakeType() {
		return this.wakeType;
	}

	public void setWakeType(String wakeType) {
		this.wakeType = wakeType;
	}

	public String getWakeValue() {
		return this.wakeValue;
	}

	public void setWakeValue(String wakeValue) {
		this.wakeValue = wakeValue;
	}

	public String getWakeContent() {
		return this.wakeContent;
	}

	public void setWakeContent(String wakeContent) {
		this.wakeContent = wakeContent;
	}

	
	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getWakeDate() {
		return this.wakeDate;
	}

	public void setWakeDate(String wakeDate) {
		this.wakeDate = wakeDate;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getWakeName() {
		return this.wakeName;
	}

	public void setWakeName(String wakeName) {
		this.wakeName = wakeName;
	}

	public String getWakeFlag() {
		return this.wakeFlag;
	}

	public void setWakeFlag(String wakeFlag) {
		this.wakeFlag = wakeFlag;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}