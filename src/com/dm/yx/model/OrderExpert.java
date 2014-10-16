package com.dm.yx.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class OrderExpert implements Serializable
{
	public String getSkill() {
		return skill;
	}
	public void setSkill(String skill) {
		this.skill = skill;
	}
	@Expose
	private String doctorName;
	@Expose
	private String teamName;
	@Expose
	private String doctorId;
	@Expose
	private String teamId;
	@Expose
	private String day;
	@Expose
	private String introduce;
	@Expose
	private String registerNum;
	@Expose
	private String fee;
	@Expose
	private String workTime;
	@Expose
	private String post;
	@Expose
	private String userOrderNum;
	@Expose
	private String week;
	
	@Expose
	private String skill;
	
	@Expose
	private String photoUrl;
	
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public String getUserFlag() {
		return userFlag;
	}
	public void setUserFlag(String userFlag) {
		this.userFlag = userFlag;
	}
	@Expose
	String display;
	@Expose
	String userFlag;
	@Expose
	String numMax;
	public String getNumMax() {
		return numMax;
	}
	public void setNumMax(String numMax) {
		this.numMax = numMax;
	}
	@Expose
	String orderTeamCount;
	
	public String getOrderTeamCount() {
		return orderTeamCount;
	}
	public void setOrderTeamCount(String orderTeamCount) {
		this.orderTeamCount = orderTeamCount;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getUserOrderNum()
	{
		return userOrderNum;
	}
	public void setUserOrderNum(String userOrderNum)
	{
		this.userOrderNum = userOrderNum;
	}
	public String getRegisterId()
	{
		return registerId;
	}
	public void setRegisterId(String registerId)
	{
		this.registerId = registerId;
	}
	@Expose
	String registerId;
	
	public String getPost()
	{
		return post;
	}
	public void setPost(String post)
	{
		this.post = post;
	}
	public String getDoctorName()
	{
		return doctorName;
	}
	public void setDoctorName(String doctorName)
	{
		this.doctorName = doctorName;
	}
	public String getTeamName()
	{
		return teamName;
	}
	public void setTeamName(String teamName)
	{
		this.teamName = teamName;
	}
	public String getDoctorId()
	{
		return doctorId;
	}
	public void setDoctorId(String doctorId)
	{
		this.doctorId = doctorId;
	}
	public String getTeamId()
	{
		return teamId;
	}
	public void setTeamId(String teamId)
	{
		this.teamId = teamId;
	}
	public String getDay()
	{
		return day;
	}
	public void setDay(String day)
	{
		this.day = day;
	}
	public String getIntroduce()
	{
		return introduce;
	}
	public void setIntroduce(String introduce)
	{
		this.introduce = introduce;
	}
	public String getRegisterNum()
	{
		return registerNum;
	}
	public void setRegisterNum(String registerNum)
	{
		this.registerNum = registerNum;
	}
	public String getFee()
	{
		return fee;
	}
	public void setFee(String fee)
	{
		this.fee = fee;
	}
	public String getWorkTime()
	{
		return workTime;
	}
	public void setWorkTime(String workTime)
	{
		this.workTime = workTime;
	}

	
}
