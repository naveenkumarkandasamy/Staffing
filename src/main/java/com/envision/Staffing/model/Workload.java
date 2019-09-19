package com.envision.Staffing.model;

import java.util.ArrayList;
import java.util.List;

public class Workload {

    public String day  ;

    public double[] capacityArray = new double[168];

    public int[] physicianCountperhour = new int[180];

    //Stores shift slots for whole week 
    public List<Shift> result = new ArrayList<Shift>();

    public double firstHourCapacity = 1.2, midHourCapacity = 1.0, lastHourCapacity = 0.8;

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
}
