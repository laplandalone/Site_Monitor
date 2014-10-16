package com.dm.yx.model;

import java.util.Date;

/**
 * HospitalUserRelationshipT entity. @author MyEclipse Persistence Tools
 */

public class HospitalUserRelationshipT implements java.io.Serializable {

	// Fields

	private String relationshipId;
	private String userId;
	private String hospitalId;
	private String state;
	private Date createDate;

	// Constructors

	/** default constructor */
	public HospitalUserRelationshipT() {
	}

	/** minimal constructor */
	public HospitalUserRelationshipT(String relationshipId) {
		this.relationshipId = relationshipId;
	}

	/** full constructor */
	public HospitalUserRelationshipT(String relationshipId, String userId,
			String hospitalId, String state, Date createDate) {
		this.relationshipId = relationshipId;
		this.userId = userId;
		this.hospitalId = hospitalId;
		this.state = state;
		this.createDate = createDate;
	}

	// Property accessors

	public String getRelationshipId() {
		return this.relationshipId;
	}

	public void setRelationshipId(String relationshipId) {
		this.relationshipId = relationshipId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getHospitalId() {
		return this.hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}