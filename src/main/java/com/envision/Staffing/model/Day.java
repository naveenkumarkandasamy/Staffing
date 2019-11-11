package com.envision.Staffing.model;

public class Day {

	private String name;
	private Double[] expectedPatientsPerHour;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double[] getExpectedPatientsPerHour() {
		return expectedPatientsPerHour;
	}
	public void setExpectedPatientsPerHour(Double[] expectedPatientsPerHour) {
		this.expectedPatientsPerHour = expectedPatientsPerHour;
	}
}
