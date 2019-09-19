package com.envision.Staffing.model;

public class Payload {
	 public Day[] day;

	public Day[] getDay() {
		return day;
	}

	public void setDay(Day[] day) {
		this.day = day;
	}

	public Payload() {
		super();
	}
	public Payload(Day[] day) {
		super();
		this.day = day;
	}
	 
}
