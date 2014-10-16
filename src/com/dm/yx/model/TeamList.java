package com.dm.yx.model;

import java.util.List;

import com.google.gson.annotations.Expose;

public class TeamList
{
	@Expose
	List<Team> teams;

	public List<Team> getTeams()
	{
		return teams;
	}

	public void setTeams(List<Team> teams)
	{
		this.teams = teams;
	}
	
}
