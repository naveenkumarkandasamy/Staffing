package com.envision.Staffing.model;

public class Clinician implements Comparable<Clinician> {
	
	public int Id;
	public String Name;
	public double PatientsPerHour;
	public double Coefficient;
	public int Cost;
	public double[] Capacity;  //for first hour, mid hour and last hour
	
	
	
	public Clinician(int Id, String Name, double PatientsPerHour, double Coefficient, int Cost, double[] Capacity) {
		this.Id = Id;
		this.Name = Name;
		this.PatientsPerHour = PatientsPerHour;
		this.Coefficient = Coefficient;
		this.Cost = Cost;
		this.Capacity = Capacity;
	}
	
	public Double getPatientsPerHour() {
		return this.PatientsPerHour;
	}

	public int getCost() {
		return this.Cost;
	}

	public int compareTo(Clinician cli) {
		
		int compareCost = ((Clinician) cli).getCost(); 
		
		//descending order
		return compareCost - this.Cost  ;
		
		
	}	
	
}
