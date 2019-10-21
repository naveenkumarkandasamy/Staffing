package com.envision.Staffing.model;

public class Clinician implements Comparable<Clinician> {

	public int id;
	public String name;
	public Double patientsPerHour;
	public double coefficient;
	public int cost;
	public double[] capacity;
	private int[] clinicianCountPerHour;// for first hour, mid hour and last hour
	private String[] expressions;

	public Clinician(int id, String name, Double patientsPerHour, double coefficient, int cost, double[] capacity) {
		this.id = id;
		this.name = name;
		this.patientsPerHour = patientsPerHour;
		this.coefficient = coefficient;
		this.cost = cost;
		this.capacity = capacity;
	}

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
		return cost;
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

	public int compareTo(Clinician cli) {
		int comparecost = cli.getCost();
		// ascending order
		return this.cost - comparecost;

	}

}
