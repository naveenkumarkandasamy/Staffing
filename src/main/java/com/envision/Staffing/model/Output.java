package com.envision.Staffing.model;

import java.util.ArrayList;
import java.util.Map;

public class Output {


	private HourlyDetail[] hourlyDetail;
	private ArrayList<Map<Integer,Map<String,Integer>>> clinicianHourCount;
	
	
	
	
	public HourlyDetail[] getHourlyDetail() {
		return hourlyDetail;
	}
	public void setHourlyDetail(HourlyDetail[] hourlyDetail) {
		this.hourlyDetail = hourlyDetail;
	}
	public ArrayList<Map<Integer,Map<String,Integer>>> getClinicianHourCount() {
		return clinicianHourCount;
	}
	public void setClinicianHourCount(ArrayList<Map<Integer,Map<String,Integer>>> clinicianHourCount) {
		this.clinicianHourCount = clinicianHourCount;
	}
}
