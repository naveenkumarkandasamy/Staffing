package com.envision.Staffing.model;

//model to map between each day and its workload
public class Day {
	  public String name;
      public double[] workload;
      
      
    //getters and setters for each field  
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
	
	//constructors
	public Day() {
		
	}
	public Day(String name, double[] workload) {
		super();
		this.name = name;
		this.workload = workload;
	}
}
