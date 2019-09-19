package com.envision.Staffing.model;

public class Shift {
    public int start_time ;
    public int end_time;
    public int no_of_hours ;
    public int day;
	public int getStart_time() {
		return start_time;
	}
	public void setStart_time(int start_time) {
		this.start_time = start_time;
	}
	public int getEnd_time() {
		return end_time;
	}
	public void setEnd_time(int end_time) {
		this.end_time = end_time;
	}
	public int getNo_of_hours() {
		return no_of_hours;
	}
	public void setNo_of_hours(int no_of_hours) {
		this.no_of_hours = no_of_hours;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}

}
