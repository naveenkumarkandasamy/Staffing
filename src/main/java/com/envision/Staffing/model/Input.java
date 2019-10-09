package com.envision.Staffing.model;

public class Input {
	
	private String id;
	
	private String roleDescription;
	
	private Double patientsCoveredPerHr;
	
	private Double cost;

	public Input() {
		super();
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public Double getPatientsCoveredPerHr() {
		return patientsCoveredPerHr;
	}

	public void setPatientsCoveredPerHr(Double patientsCoveredPerHr) {
		this.patientsCoveredPerHr = patientsCoveredPerHr;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}
	
	

}
