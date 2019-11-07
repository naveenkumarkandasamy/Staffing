package com.envision.Staffing.model;

public class Input {

	private Clinician[] clinician; 
	private int[] shiftLength;
	
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
	
}
