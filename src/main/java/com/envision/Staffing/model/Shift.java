package com.envision.Staffing.model;

import java.util.Arrays;

//model to hold shift details
public class Shift {
	private static int count = 0; //count of clinicians in each shift
	private String physicianType;
	private Integer id;
	private Integer startTime;
	private Integer endTime;
	private Integer noOfHours;//indicates the slot length
	private Integer day;      //day of the shift
	private double[] utilization;
	
	//constructor
	public Shift() {
		count++;
		id = count;
	}

	
    //getters and setters for all the fields
	public Integer getStartTime() {
		return startTime;
	}

	public void setStartTime(Integer startTime) {
		this.startTime = startTime;
	}

	public Integer getEndTime() {
		return endTime;
	}

	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}

	public Integer getNoOfHours() {
		return noOfHours;
	}

	public void setNoOfHours(Integer noOfHours) {
		this.noOfHours = noOfHours;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public double[] getUtilization() {
		return utilization;
	}

	public void setUtilization(double[] utilization) {
		this.utilization = utilization;
	}

	public String getPhysicianType() {
		return physicianType;
	}

	public void setPhysicianType(String physicianType) {
		this.physicianType = physicianType;
	}

	
	//overriding equals method to compare shift objects based on startTime, endTime and noOfHours
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Shift shift = (Shift) o;
		if (id != shift.id)
			return false;
		if (startTime != shift.startTime)
			return false;
		if (endTime != shift.endTime)
			return false;
		if (noOfHours != shift.noOfHours)
			return false;
		return day == shift.day;
	}

	@Override
	public int hashCode() {
		Integer result = (Integer) (id ^ (id >>> 32));
		result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
		result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
		result = 31 * result + (noOfHours != null ? noOfHours.hashCode() : 0);
		result = 31 * result + (day != null ? day.hashCode() : 0);
		return result;
	}

	//overriding toString() method
	public String toString() {
		return this.id + " " + this.day + " " + this.startTime + " " + this.endTime + " "
				+ " " + (this.endTime-this.startTime) + " " + this.noOfHours + " "+ Arrays.toString(utilization);
	}
}
