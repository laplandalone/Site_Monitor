package com.dm.yx.model;


/**
 * HospitalNewsT entity. @author MyEclipse Persistence Tools
 */

public class CheckRstT implements java.io.Serializable {

	// Fields

	private String check_time;
	private String patient_id;
	private String check_type;
	private String check_type_id;
	
	
	public String getCheck_type_id() {
		return check_type_id;
	}
	public void setCheck_type_id(String check_type_id) {
		this.check_type_id = check_type_id;
	}
	public String getCheck_type() {
		return check_type;
	}
	public void setCheck_type(String check_type) {
		this.check_type = check_type;
	}
	public String getCheck_time() {
		return check_time;
	}
	public void setCheck_time(String check_time) {
		this.check_time = check_time;
	}
	public String getPatient_id() {
		return patient_id;
	}
	public void setPatient_id(String patient_id) {
		this.patient_id = patient_id;
	}
	
	
	

}