package com.envision.Staffing.model;

public class Input {

	private Clinician[] clinician; 
	private int[] shiftLength;
	private double lowerLimitFactor;
	
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
	public double getLowerLimitFactor() {
		return lowerLimitFactor;
	}
	public void setLowerLimitFactor(double lowerLimitFactor) {
		this.lowerLimitFactor = lowerLimitFactor;
	}
	
}
