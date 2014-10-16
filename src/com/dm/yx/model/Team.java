package com.dm.yx.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class Team implements Serializable 
{
	@Expose
	public String teamId;
	@Expose
	public String teamName;
	@Expose
	public String introduce;
	
	@Expose
	public String imgUrl;
	
	public String getImgUrl()
	{
		return imgUrl;
	}
	public void setImgUrl(String imgUrl)
	{
		this.imgUrl = imgUrl;
	}
	public String getTeamId()
	{
		return teamId;
	}
	public void setTeamId(String teamId)
	{
		this.teamId = teamId;
	}
	public String getTeamName()
	{
		return teamName;
	}
	public void setTeamName(String teamName)
	{
		this.teamName = teamName;
	}
	public String getIntroduce()
	{
		return introduce;
	}
	public void setIntroduce(String introduce)
	{
		this.introduce = introduce;
	}
	
}
