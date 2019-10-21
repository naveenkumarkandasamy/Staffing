package com.envision.Staffing.model;

public class HourlyDetail {
	
	private Integer hour;
	
	private Integer numberOfPhysicians;
	
	private Integer numberOfAPPs;
	
	private Integer numberOfScribes ;
	
	private Integer numberOfShiftBeginning;
	
	private Integer numberOfShiftEnding;
	
	private Double expectedWorkLoad;
	
	private Double capacityWorkLoad;
	
	private Double utilization;

	public HourlyDetail() {
		this.numberOfShiftBeginning=0;
		this.numberOfShiftEnding=0;
	}
	public Integer getHour() {
		return hour;
	}

	public void setHour(Integer hour) {
		this.hour = hour;
	}

	public Integer getNumberOfPhysicians() {
		return numberOfPhysicians;
	}

	public void setNumberOfPhysicians(Integer numberOfPhysicians) {
		this.numberOfPhysicians = numberOfPhysicians;
	}

	public Integer getNumberOfShiftBeginning() {
		return numberOfShiftBeginning;
	}

	public void setNumberOfShiftBeginning(Integer numberOfShiftBeginning) {
		this.numberOfShiftBeginning = numberOfShiftBeginning;
	}

	public Integer getNumberOfShiftEnding() {
		return numberOfShiftEnding;
	}

	public void setNumberOfShiftEnding(Integer numberOfShiftEnding) {
		this.numberOfShiftEnding = numberOfShiftEnding;
	}

	public Double getExpectedWorkLoad() {
		return expectedWorkLoad;
	}

	public void setExpectedWorkLoad(Double expectedWorkLoad) {
		this.expectedWorkLoad = expectedWorkLoad;
	}

	public Double getCapacityWorkLoad() {
		return capacityWorkLoad;
	}

	public void setCapacityWorkLoad(Double capacityWorkLoad) {
		this.capacityWorkLoad = capacityWorkLoad;
	}

	public Double getUtilization() {
		return utilization;
	}

	public void setUtilization(Double utilization) {
		this.utilization = utilization;
	}
	
	public void incrementNumberOfShiftBeginning() {
		if(numberOfShiftBeginning==null)
			numberOfShiftBeginning=1;
		else
		this.numberOfShiftBeginning++;
	}
	
	public void incrementNumberOfShiftEnding() {
		if(numberOfShiftEnding==null)
			numberOfShiftEnding=1;
		else
		this.numberOfShiftEnding++;
	}
	public Integer getNumberOfAPPs() {
		return numberOfAPPs;
	}
	public void setNumberOfAPPs(Integer numberOfAPPs) {
		this.numberOfAPPs = numberOfAPPs;
	}
	public Integer getNumberOfScribes() {
		return numberOfScribes;
	}
	public void setNumberOfScribes(Integer numberOfScribes) {
		this.numberOfScribes = numberOfScribes;
	}
	

}
