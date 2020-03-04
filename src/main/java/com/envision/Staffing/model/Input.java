package com.envision.Staffing.model;

public class Input {

	private Clinician[] clinician;
	private Integer[] shiftLength;
	private Double lowerLimitFactor;
	private Day[] dayWorkload;
	private Integer from;
	private Integer to;
	private Integer hourwait;

	public Clinician[] getClinician() {
		return clinician;
	}

	public void setClinician(Clinician[] clinician) {
		this.clinician = clinician;
	}

	public Integer[] getShiftLength() {
		return shiftLength;
	}

	public void setShiftLength(Integer[] shiftLength) {
		this.shiftLength = shiftLength;
	}

	public Double getLowerLimitFactor() {
		return lowerLimitFactor;
	}

	public void setLowerLimitFactor(double lowerLimitFactor) {
		this.lowerLimitFactor = lowerLimitFactor;
	}

	public Day[] getDayWorkload() {
		return dayWorkload;
	}

	public void setDayWorkload(Day[] dayWorkload) {
		this.dayWorkload = dayWorkload;
	}

	public Integer getFrom() {
		return from;
	}

	public void setFrom(Integer from) {
		this.from = from;
	}

	public Integer getTo() {
		return to;
	}

	public void setTo(Integer to) {
		this.to = to;
	}

	public Integer getHourwait() {
		return hourwait;
	}

	public void setHourwait(Integer hourwait) {
		this.hourwait = hourwait;
	}
    
}
