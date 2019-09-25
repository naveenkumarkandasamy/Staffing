package com.envision.Staffing.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Workload {

    public String day  ;

    public double[] capacityArray = new double[168];

    public int[] physicianCountperhour = new int[180];
    
    public int[] physicianStretchingPerHour = new int[168];

    //Stores shift slots for whole week 
    public List<Shift> result = new ArrayList<Shift>();

    public double firstHourCapacity = 1.0, midHourCapacity = 0.83, lastHourCapacity = 0.67;

    public double[] utilizationArray = new double[168];

    //24*7 hours in a week 
    public int sizeOfArray = 168; 

    public int physicianSalaryPerHour = 320;

    //Wokload for each hour for 7 days. Will be modified as and when physicians are added.
    public double[] workloadArray = new double[168];

    // Workloads of each hour for 7 days. This array will be used to calculate utilization.
    public double[] fixedworkloadArray = new double[168];

    // 24 hours in a day
    public int dayDuration = 24; 
    
    public Map<Integer, Map<Shift, Double>> hourToShiftMap = new HashMap<Integer, Map<Shift,Double>>();
    
    public HourlyDetail[] hourlyDetailList = new HourlyDetail[168];
    
    public Workload() {
    	for(int i=0;i<168;i++)
    		hourlyDetailList[i]= new HourlyDetail();
    }
}
