package com.envision.Staffing.model;

public class Day {
	  public String name;
      public double[] workload;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double[] getWorkload() {
		return workload;
	}
	public void setWorkload(double[] workload) {
		this.workload = workload;
	}
	
	public Day() {
		
	}
	public Day(String name, double[] workload) {
		super();
		this.name = name;
		this.workload = workload;
	}
}
