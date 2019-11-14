package com.envision.Staffing.model;

//Model to hold data about a clinician
public class Clinician implements Comparable<Clinician> {

	private int id;
	private String name; // clinician description
	private Double patientsPerHour; //indicates the number of patients the clinician can handle in an hour
	private double coefficient; //denotes the percentage with respect to physician 
	private int cost; // cost of each clinician per hour
	private double[] capacity; // for first hour, mid hour and last hour
	private int[] clinicianCountPerHour;  
	private String[] expressions; //expressions to handle the relationships between clinicians 

	
	//constructor
	public Clinician(int id, String name, Double patientsPerHour, double coefficient, int cost, double[] capacity) {
		this.id = id;
		this.name = name;
		this.patientsPerHour = patientsPerHour;
		this.coefficient = coefficient;
		this.cost = cost;
		this.capacity = capacity;
	}

	
	//getters and setters for each field
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPatientsPerHour() {
		return patientsPerHour;
	}

	public void setPatientsPerHour(Double patientsPerHour) {
		this.patientsPerHour = patientsPerHour;
	}

	public double getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(double coefficient) {
		this.coefficient = coefficient;
	}

	public int getCost() {
		return this.cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public double[] getCapacity() {
		return capacity;
	}

	public void setCapacity(double[] capacity) {
		this.capacity = capacity;
	}

	public int[] getClinicianCountPerHour() {
		return clinicianCountPerHour;
	}

	public void setClinicianCountPerHour(int[] clinicianCountPerHour) {
		this.clinicianCountPerHour = clinicianCountPerHour;
	}

	public String[] getExpressions() {
		return expressions;
	}

	public void setExpressions(String[] expressions) {
		this.expressions = expressions;
	}

	public Clinician() {
		super();
	}

	//overriding compare to function, to provide sort on clinician objects based on the clinician's cost in ascending order
	public int compareTo(Clinician cli) {
		int comparecost = cli.getCost();
		// ascending order
		return this.cost - comparecost;

	}
	
	
	
}
