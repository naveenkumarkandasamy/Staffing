package com.envision.Staffing.model;

public class Input {

	private Clinician[] clinician;
	private Integer[] shiftLength;
	private Double lowerLimitFactor;
	private Day[] dayWorkload;

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

}
