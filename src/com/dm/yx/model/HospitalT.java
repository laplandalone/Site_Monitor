package com.dm.yx.model;

import java.util.Date;

/**
 * HospitalT entity. @author MyEclipse Persistence Tools
 */

public class HospitalT implements java.io.Serializable {

	// Fields

	private String hospitalId;
	private String hospitalName;
	private String state;
	private String remark;
	private String imageUrl;
	private String createDate;
	private String introduce;
	private String webUrl;

	// Constructors

	/** default constructor */
	public HospitalT() {
	}

	/** minimal constructor */
	public HospitalT(String hospitalId) {
		this.hospitalId = hospitalId;
	}

	/** full constructor */
	public HospitalT(String hospitalId, String hospitalName, String state,
			String remark, String imageUrl, String createDate, String introduce,
			String webUrl) {
		this.hospitalId = hospitalId;
		this.hospitalName = hospitalName;
		this.state = state;
		this.remark = remark;
		this.imageUrl = imageUrl;
		this.createDate = createDate;
		this.introduce = introduce;
		this.webUrl = webUrl;
	}

	// Property accessors

	public String getHospitalId() {
		return this.hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getHospitalName() {
		return this.hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getImageUrl() {
		return this.imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getIntroduce() {
		return this.introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getWebUrl() {
		return this.webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

}