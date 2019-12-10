package com.envision.Staffing.model;

public class Input {

	private Clinician[] clinician;
	private int[] shiftLength;
	private Double lowerLimitFactor;
	private Day[] dayWorkload;

	public Clinician[] getClinician() {
		return clinician;
	}

	public void setClinician(Clinician[] clinician) {
		this.clinician = clinician;
	}

	public int[] getShiftLength() {
		return shiftLength;
	}

	public void setShiftLength(int[] shiftLength) {
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
