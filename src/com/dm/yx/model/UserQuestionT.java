package com.dm.yx.model;

import java.util.Date;

/**
 * UserQuestionT entity. @author MyEclipse Persistence Tools
 */

public class UserQuestionT implements java.io.Serializable {

	// Fields qes
	private String id;
	private String questionId;
	private String userId;
	private String doctorId;
	private String userTelephone;
	private String recordType="ask";
	private String authType="private";
	private String content;
	private String state="00A";
	private String createDate;
	private String name;
	private String teamId;
	private String hospitalId;
	private String imgUrl;
	// Constructors

	public String getHospitalId() {
		return hospitalId;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/** default constructor */
	public UserQuestionT() {
	}

	/** minimal constructor */
	public UserQuestionT(String questionId) {
		this.questionId = questionId;
	}

	/** full constructor */
	public UserQuestionT(String questionId, String userId, String doctorId,
			String userTelephone, String recordType, String authType,
			String content, String state, String createDate,String imgUrl) {
		this.questionId = questionId;
		this.userId = userId;
		this.doctorId = doctorId;
		this.userTelephone = userTelephone;
		this.recordType = recordType;
		this.authType = authType;
		this.content = content;
		this.state = state;
		this.createDate = createDate;
		this.imgUrl=imgUrl;
	}

	// Property accessors

	public String getQuestionId() {
		return this.questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDoctorId() {
		return this.doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getUserTelephone() {
		return this.userTelephone;
	}

	public void setUserTelephone(String userTelephone) {
		this.userTelephone = userTelephone;
	}

	public String getRecordType() {
		return this.recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getAuthType() {
		return this.authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

}