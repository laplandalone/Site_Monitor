package com.dm.yx.model;

import java.util.Date;

/**
 * RegisterOrderT entity. @author MyEclipse Persistence Tools
 */

public class RegisterOrderT implements java.io.Serializable {

	// Fields

	private String orderId;
	private String userId;
	private String registerId;
	private String doctorId;
	private String doctorName;
	private String orderNum;
	private String orderState;
	private String orderFee;
	private String registerTime;
	private String userName;
	private String userNo;
	private String userTelephone;
	private String sex;
	private String teamId;
	private String teamName;
	private String state;
	private String createDate;
	private String hospitalId;
	private String payState;
	

	// Constructors

	public String getPayState() {
		return payState;
	}

	public void setPayState(String payState) {
		this.payState = payState;
	}

	/** default constructor */
	public RegisterOrderT() {
	}

	/** minimal constructor */
	public RegisterOrderT(String orderId, String userId, String registerId,
			String orderState, String registerTime, String userName,
			String userNo, String userTelephone, String sex, String teamId,
			String teamName) {
		this.orderId = orderId;
		this.userId = userId;
		this.registerId = registerId;
		this.orderState = orderState;
		this.registerTime = registerTime;
		this.userName = userName;
		this.userNo = userNo;
		this.userTelephone = userTelephone;
		this.sex = sex;
		this.teamId = teamId;
		this.teamName = teamName;
	}

	/** full constructor */
	public RegisterOrderT(String orderId, String userId, String registerId,
			String doctorId, String doctorName, String orderNum,
			String orderState, String orderFee, String registerTime,
			String userName, String userNo, String userTelephone, String sex,
			String teamId, String teamName, String state, String createDate,
			String hospitalId,String payState) {
		this.orderId = orderId;
		this.userId = userId;
		this.registerId = registerId;
		this.doctorId = doctorId;
		this.doctorName = doctorName;
		this.orderNum = orderNum;
		this.orderState = orderState;
		this.orderFee = orderFee;
		this.registerTime = registerTime;
		this.userName = userName;
		this.userNo = userNo;
		this.userTelephone = userTelephone;
		this.sex = sex;
		this.teamId = teamId;
		this.teamName = teamName;
		this.state = state;
		this.createDate = createDate;
		this.hospitalId = hospitalId;
		this.payState=payState;
	}

	// Property accessors

	public String getOrderId() {
		return this.orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRegisterId() {
		return this.registerId;
	}

	public void setRegisterId(String registerId) {
		this.registerId = registerId;
	}

	public String getDoctorId() {
		return this.doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorName() {
		return this.doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getOrderNum() {
		return this.orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getOrderState() {
		return this.orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public String getOrderFee() {
		return this.orderFee;
	}

	public void setOrderFee(String orderFee) {
		this.orderFee = orderFee;
	}

	public String getRegisterTime() {
		return this.registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserNo() {
		return this.userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getUserTelephone() {
		return this.userTelephone;
	}

	public void setUserTelephone(String userTelephone) {
		this.userTelephone = userTelephone;
	}

	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getTeamId() {
		return this.teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return this.teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(String createDate)
	{
		this.createDate = createDate;
	}

	public String getHospitalId() {
		return this.hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

}