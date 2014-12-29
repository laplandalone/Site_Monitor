package com.dm.yx.model;

/**
 * HospitalConfigT entity. @author MyEclipse Persistence Tools
 */

public class HospitalConfigT implements java.io.Serializable {

	// Fields

	private String configId;
	private String hospitalId;
	private String configName;
	private String configVal;
	private String configType;
	private String state;
	private String remark;

	// Constructors

	/** default constructor */
	public HospitalConfigT() {
	}

	/** minimal constructor */
	public HospitalConfigT(String configId, String hospitalId,
			String configName, String configVal) {
		this.configId = configId;
		this.hospitalId = hospitalId;
		this.configName = configName;
		this.configVal = configVal;
	}

	/** full constructor */
	public HospitalConfigT(String configId, String hospitalId,
			String configName, String configVal, String configType,
			String state, String remark) {
		this.configId = configId;
		this.hospitalId = hospitalId;
		this.configName = configName;
		this.configVal = configVal;
		this.configType = configType;
		this.state = state;
		this.remark = remark;
	}

	// Property accessors

	public String getConfigId() {
		return this.configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public String getHospitalId() {
		return this.hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getConfigName() {
		return this.configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public String getConfigVal() {
		return this.configVal;
	}

	public void setConfigVal(String configVal) {
		this.configVal = configVal;
	}

	public String getConfigType() {
		return this.configType;
	}

	public void setConfigType(String configType) {
		this.configType = configType;
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

}