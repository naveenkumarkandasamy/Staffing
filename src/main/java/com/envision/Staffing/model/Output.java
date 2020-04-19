package com.envision.Staffing.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//Model to combine the fields to be sent to the output
public class Output implements Serializable{

    
	private HourlyDetail[] hourlyDetail;
	private ArrayList<Map<Integer,Map<String,Integer>>> clinicianHourCount;//list containing the count of each clinician starting and ending in a specific slot
	      //ArrayList<Map<Slot,Map<"Physician Start/End" , count>>>
	
	
	//getters and setters for all the fields
	public HourlyDetail[] getHourlyDetail() {
		return hourlyDetail;
	}
	public void setHourlyDetail(HourlyDetail[] hourlyDetail) {
		this.hourlyDetail = hourlyDetail;
	}
	public List<Map<Integer,Map<String,Integer>>> getClinicianHourCount() {
		return clinicianHourCount;
	}
	public void setClinicianHourCount(List<Map<Integer,Map<String,Integer>>> clinicianHourCount) {
		this.clinicianHourCount = (ArrayList<Map<Integer, Map<String, Integer>>>) clinicianHourCount;
	}
}
