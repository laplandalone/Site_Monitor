package com.dm.yx.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class Doctor implements Serializable
{
	@Expose
	public String onlineFlag;
	@Expose
	public String sex;
	@Expose
	public String post;
	@Expose
	public String remark;
	@Expose
	public String introduce;
	
	public String state;
	@Expose
	public String hospitalId;
	@Expose
	public String workAddress;
	@Expose
	public String teamId;
	@Expose
	public String skill;
	@Expose
	public String doctorId;
	@Expose
	public String name;
	@Expose
	public String workTime;
	@Expose
	public String expertFlag;
	@Expose
	public String registerFee;
	@Expose
	public String photoUrl;
	@Expose
	public String createDate;
	@Expose
	public String registerNum;
	@Expose
	public String telephone;
	
	@Expose
	public String pinYin;
	
	public String getPinYin() {
		return pinYin;
	}
	public void setPinYin(String pinYin) {
		this.pinYin = pinYin;
	}
	public String getOnlineFlag()
	{
		return onlineFlag;
	}
	public void setOnlineFlag(String onlineFlag)
	{
		this.onlineFlag = onlineFlag;
	}
	public String getSex()
	{
		return sex;
	}
	public void setSex(String sex)
	{
		this.sex = sex;
	}
	public String getPost()
	{
		return post;
	}
	public void setPost(String post)
	{
		this.post = post;
	}
	public String getRemark()
	{
		return remark;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	public String getIntroduce()
	{
		return introduce;
	}
	public void setIntroduce(String introduce)
	{
		this.introduce = introduce;
	}
	public String getState()
	{
		return state;
	}
	public void setState(String state)
	{
		this.state = state;
	}
	public String getHospitalId()
	{
		return hospitalId;
	}
	public void setHospitalId(String hospitalId)
	{
		this.hospitalId = hospitalId;
	}
	public String getWorkAddress()
	{
		return workAddress;
	}
	public void setWorkAddress(String workAddress)
	{
		this.workAddress = workAddress;
	}
	public String getTeamId()
	{
		return teamId;
	}
	public void setTeamId(String teamId)
	{
		this.teamId = teamId;
	}
	public String getSkill()
	{
		return skill;
	}
	public void setSkill(String skill)
	{
		this.skill = skill;
	}
	public String getDoctorId()
	{
		return doctorId;
	}
	public void setDoctorId(String doctorId)
	{
		this.doctorId = doctorId;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getWorkTime()
	{
		return workTime;
	}
	public void setWorkTime(String workTime)
	{
		this.workTime = workTime;
	}
	public String getExpertFlag()
	{
		return expertFlag;
	}
	public void setExpertFlag(String expertFlag)
	{
		this.expertFlag = expertFlag;
	}
	public String getRegisterFee()
	{
		return registerFee;
	}
	public void setRegisterFee(String registerFee)
	{
		this.registerFee = registerFee;
	}
	public String getPhotoUrl()
	{
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl)
	{
		this.photoUrl = photoUrl;
	}
	public String getCreateDate()
	{
		return createDate;
	}
	public void setCreateDate(String createDate)
	{
		this.createDate = createDate;
	}
	
	public String getRegisterNum()
	{
		return registerNum;
	}
	public void setRegisterNum(String registerNum)
	{
		this.registerNum = registerNum;
	}
	public String getTelephone()
	{
		return telephone;
	}
	public void setTelephone(String telephone)
	{
		this.telephone = telephone;
	}
	
	
}
