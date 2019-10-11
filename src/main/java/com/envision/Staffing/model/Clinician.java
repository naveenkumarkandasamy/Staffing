package com.envision.Staffing.model;

public class Clinician {
	
	private double[] clinicianCapacity;
	
	private String description;
	
	private int[] clinicianCountPerHour;
	
	private double salaryPerHour;

	public double[] getClinicianCapacity() {
		return clinicianCapacity;
	}

	public void setClinicianCapacity(double[] clinicianCapacity) {
		this.clinicianCapacity = clinicianCapacity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int[] getClinicianCountPerHour() {
		return clinicianCountPerHour;
	}

	public void setClinicianCountPerHour(int[] clinicianCountPerHour) {
		this.clinicianCountPerHour = clinicianCountPerHour;
	}

	public double getSalaryPerHour() {
		return salaryPerHour;
	}

	public void setSalaryPerHour(double salaryPerHour) {
		this.salaryPerHour = salaryPerHour;
	}
	
	

}
