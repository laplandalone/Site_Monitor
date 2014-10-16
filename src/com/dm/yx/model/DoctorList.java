package com.dm.yx.model;

import java.util.List;

import com.google.gson.annotations.Expose;

public class DoctorList
{
	@Expose
	List<Doctor> doctors;

	public List<Doctor> getDoctors()
	{
		return doctors;
	}

	public void setDoctors(List<Doctor> doctors)
	{
		this.doctors = doctors;
	}
	
	
}
