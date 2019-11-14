package com.envision.Staffing.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//model holds the hour data of capacity,physician count, app count, scribe count 
public class Workload {

    private String day  ;

    private double[] capacityArray = new double[168];

    private int[] physicianCountperhour = new int[168];
    
    private int[] appCountPerHour = new int[168];
    
    private int[] scribeCountPerHour = new int[168];
    
    private int[] physicianStretchingPerHour = new int[168];
    
    private Double docEfficency = new Double(1.2);

    //Stores shift slots for whole week 
    private List<Shift> result = new ArrayList<>();

    private double firstHourCapacity;
    private double midHourCapacity ;
    private double lastHourCapacity;
    private double[] physicianCapacity = {1.0,0.8, 0.6};
   
    private double[] utilizationArray = new double[168];

    //24*7 hours in a week 
    private int sizeOfArray = 168; 

    //Wokload for each hour for 7 days. Will be modified as and when physicians are added.
    private double[] workloadArray = new double[168];

    // Workloads of each hour for 7 days. This array will be used to calculate utilization.
    private double[] fixedworkloadArray = new double[168];

    // 24 hours in a day
    private int dayDuration = 24; 
    
    private Map<Integer, Map<Shift, Double>> hourToShiftMap = new HashMap<>();
    
    private HourlyDetail[] hourlyDetailList = new HourlyDetail[168];
    
    //constructor
    public Workload() {
    	setFirstHourCapacity(1.0);
    	setMidHourCapacity(0.8);
    	setLastHourCapacity(0.6);
    	for(int i=0;i<168;i++)
    		hourlyDetailList[i]= new HourlyDetail();
    }

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public double[] getCapacityArray() {
		return capacityArray;
	}

	public void setCapacityArray(double[] capacityArray) {
		this.capacityArray = capacityArray;
	}

	public int[] getPhysicianCountperhour() {
		return physicianCountperhour;
	}

	public void setPhysicianCountperhour(int[] physicianCountperhour) {
		this.physicianCountperhour = physicianCountperhour;
	}

	public int[] getAppCountperhour() {
		return appCountPerHour;
	}

	public void setAppCountperhour(int[] appCountperhour) {
		this.appCountPerHour = appCountperhour;
	}

	public int[] getScribeCountperhour() {
		return scribeCountPerHour;
	}

	public void setScribeCountperhour(int[] scribeCountperhour) {
		this.scribeCountPerHour = scribeCountperhour;
	}

	public int[] getPhysicianStretchingPerHour() {
		return physicianStretchingPerHour;
	}

	public void setPhysicianStretchingPerHour(int[] physicianStretchingPerHour) {
		this.physicianStretchingPerHour = physicianStretchingPerHour;
	}

	public Double getDocEfficency() {
		return docEfficency;
	}

	public void setDocEfficency(Double docEfficency) {
		this.docEfficency = docEfficency;
	}

	public List<Shift> getResult() {
		return result;
	}

	public void setResult(List<Shift> result) {
		this.result = result;
	}

	public double[] getFixedworkloadArray() {
		return fixedworkloadArray;
	}

	public void setFixedworkloadArray(double[] fixedworkloadArray) {
		this.fixedworkloadArray = fixedworkloadArray;
	}

	public double[] getWorkloadArray() {
		return workloadArray;
	}

	public void setWorkloadArray(double[] workloadArray) {
		this.workloadArray = workloadArray;
	}

	public int getSizeOfArray() {
		return sizeOfArray;
	}

	public void setSizeOfArray(int sizeOfArray) {
		this.sizeOfArray = sizeOfArray;
	}

	public int getDayDuration() {
		return dayDuration;
	}

	public void setDayDuration(int dayDuration) {
		this.dayDuration = dayDuration;
	}

	public HourlyDetail[] getHourlyDetailList() {
		return hourlyDetailList;
	}

	public void setHourlyDetailList(HourlyDetail[] hourlyDetailList) {
		this.hourlyDetailList = hourlyDetailList;
	}

	public Map<Integer, Map<Shift, Double>> getHourToShiftMap() {
		return hourToShiftMap;
	}

	public void setHourToShiftMap(Map<Integer, Map<Shift, Double>> hourToShiftMap) {
		this.hourToShiftMap = hourToShiftMap;
	}

	public double getFirstHourCapacity() {
		return firstHourCapacity;
	}

	public void setFirstHourCapacity(double firstHourCapacity) {
		this.firstHourCapacity = firstHourCapacity;
	}

	public double getMidHourCapacity() {
		return midHourCapacity;
	}

	public void setMidHourCapacity(double midHourCapacity) {
		this.midHourCapacity = midHourCapacity;
	}

	public double getLastHourCapacity() {
		return lastHourCapacity;
	}

	public void setLastHourCapacity(double lastHourCapacity) {
		this.lastHourCapacity = lastHourCapacity;
	}

	public double[] getPhysicianCapacity() {
		return physicianCapacity;
	}

	public double[] getUtilizationArray() {
		return utilizationArray;
	}

	public void setUtilizationArray(double[] utilizationArray) {
		this.utilizationArray = utilizationArray;
	}

	
}
