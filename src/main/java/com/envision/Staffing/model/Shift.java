package com.envision.Staffing.model;

import java.util.Arrays;

public class Shift {
	public static int count =0 ;
	public Integer id;
	public Integer start_time ;
    public Integer end_time;
    public Integer no_of_hours ;
    public Integer day;
    
    public Shift(){
    	count++;
    	id =count;
    }
    public double[] utilization;
	public Integer getStart_time() {
		return start_time;
	}
	public void setStart_time(Integer start_time) {
		this.start_time = start_time;
	}
	public Integer getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Integer end_time) {
		this.end_time = end_time;
	}
	public Integer getNo_of_hours() {
		return no_of_hours;
	}
	public void setNo_of_hours(Integer no_of_hours) {
		this.no_of_hours = no_of_hours;
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

	
	   @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;
	        Shift shift = (Shift) o;
	        if (id != shift.id) return false;
	        if (start_time!=shift.start_time) return false;
	        if (end_time!=shift.end_time) return false;
	        if (no_of_hours!=shift.no_of_hours) return false;
	       return day == shift.day;
	    }
	    @Override
	    public int hashCode() {
	        Integer result = (Integer) (id ^ (id >>> 32));
	        result = 31 * result + (start_time != null ? start_time.hashCode() : 0);
	        result = 31 * result + (end_time != null ? end_time.hashCode() : 0);
	        result = 31 * result + (no_of_hours != null ? no_of_hours.hashCode() : 0);
	        result = 31 * result + (day != null ? day.hashCode() : 0);
	        return result;
	    }

	    
	    public String toString() {
	    	return this.id+" "+this.day+" "+this.start_time+" "+this.end_time+" "+Arrays.toString(utilization);
	    }
}
