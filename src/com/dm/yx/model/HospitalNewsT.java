package com.dm.yx.model;

import java.util.Date;

/**
 * HospitalNewsT entity. @author MyEclipse Persistence Tools
 */

public class HospitalNewsT implements java.io.Serializable {

	// Fields

	private String newsId;
	private String hospitalId;
	private String newsTitle;
	private String content;
	private String newsImages;
	private String state;
	private String createDate;
	private String typeName;
	// Constructors

	/** default constructor */
	public HospitalNewsT() {
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/** minimal constructor */
	public HospitalNewsT(String newsId, String hospitalId, String newsTitle,
			String content) {
		this.newsId = newsId;
		this.hospitalId = hospitalId;
		this.newsTitle = newsTitle;
		this.content = content;
	}

	/** full constructor */
	public HospitalNewsT(String newsId, String hospitalId, String newsTitle,
			String content, String newsImages, String state, String createDate,String typeName) {
		this.newsId = newsId;
		this.hospitalId = hospitalId;
		this.newsTitle = newsTitle;
		this.content = content;
		this.newsImages = newsImages;
		this.state = state;
		this.createDate = createDate;
		this.typeName=typeName;
	}

	// Property accessors

	public String getNewsId() {
		return this.newsId;
	}

	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}

	public String getHospitalId() {
		return this.hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getNewsTitle() {
		return this.newsTitle;
	}

	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}

	public String getNewsContent() {
		return this.content;
	}

	public void setNewsContent(String content) {
		this.content = content;
	}

	public String getNewsImages() {
		return this.newsImages;
	}

	public void setNewsImages(String newsImages) {
		this.newsImages = newsImages;
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