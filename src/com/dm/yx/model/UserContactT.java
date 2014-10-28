package com.dm.yx.model;

import java.util.Date;

/**
 * UserContactT entity. @author MyEclipse Persistence Tools
 */

public class UserContactT implements java.io.Serializable {

	// Fields

	private String contactId;
	private String userId;
	private String contactName;
	private String contactTelephone;
	private String contactSex;
	private String contactNo;
	private String state;
	private String cardId;

	// Constructors

	/** default constructor */
	public UserContactT() {
	}

	/** minimal constructor */
	public UserContactT(String contactId, String userId, String contactTelephone) {
		this.contactId = contactId;
		this.userId = userId;
		this.contactTelephone = contactTelephone;
	}

	/** full constructor */
	public UserContactT(String contactId, String userId, String contactName,
			String contactTelephone, String contactSex, String contactNo,
			Date createDate, String state, String cardId) {
		this.contactId = contactId;
		this.userId = userId;
		this.contactName = contactName;
		this.contactTelephone = contactTelephone;
		this.contactSex = contactSex;
		this.contactNo = contactNo;
		this.state = state;
		this.cardId = cardId;
	}

	// Property accessors

	public String getContactId() {
		return this.contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getContactName() {
		return this.contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactTelephone() {
		return this.contactTelephone;
	}

	public void setContactTelephone(String contactTelephone) {
		this.contactTelephone = contactTelephone;
	}

	public String getContactSex() {
		return this.contactSex;
	}

	public void setContactSex(String contactSex) {
		this.contactSex = contactSex;
	}

	public String getContactNo() {
		return this.contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCardId() {
		return this.cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

}