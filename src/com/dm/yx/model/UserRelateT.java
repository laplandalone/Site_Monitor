package com.dm.yx.model;

import java.util.Date;

/**
 * UserRelateT entity. @author MyEclipse Persistence Tools
 */

public class UserRelateT implements java.io.Serializable {

	// Fields

	private String relateId;
	private String userId;
	private String relatePhone;
	private String relatePass;
	private String channelId;
	private String pushUserId;
	private String createDate;
	private String state;
	private String relateName;

	// Constructors

	public String getRelateName() {
		return relateName;
	}

	public void setRelateName(String relateName) {
		this.relateName = relateName;
	}

	/** default constructor */
	public UserRelateT() {
	}

	/** minimal constructor */
	public UserRelateT(String relateId, String userId) {
		this.relateId = relateId;
		this.userId = userId;
	}

	/** full constructor */
	public UserRelateT(String relateId, String userId, String relatePhone,
			String relatePass, String channelId, String pushUserId,
			String createDate, String state,String relateName) {
		this.relateId = relateId;
		this.userId = userId;
		this.relatePhone = relatePhone;
		this.relatePass = relatePass;
		this.channelId = channelId;
		this.pushUserId = pushUserId;
		this.createDate = createDate;
		this.state = state;
		this.relateName=relateName;
	}

	// Property accessors

	public String getRelateId() {
		return this.relateId;
	}

	public void setRelateId(String relateId) {
		this.relateId = relateId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRelatePhone() {
		return this.relatePhone;
	}

	public void setRelatePhone(String relatePhone) {
		this.relatePhone = relatePhone;
	}

	public String getRelatePass() {
		return relatePass;
	}

	public void setRelatePass(String relatePass) {
		this.relatePass = relatePass;
	}

	public String getChannelId() {
		return this.channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getPushUserId() {
		return this.pushUserId;
	}

	public void setPushUserId(String pushUserId) {
		this.pushUserId = pushUserId;
	}

	public String getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

}