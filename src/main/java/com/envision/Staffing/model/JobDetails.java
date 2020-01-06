package com.envision.Staffing.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "job_details")
public class JobDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id ;
	
	@Column(name="user_id")
	private String userId;

	@Column(name="shify_length_preferences")
	private String shiftLengthPPreferences;
	
	@Column(name="lower_utilization_factor")
	private float lowerUtilizationFactor;
	
	@Column(name="upper_utilization_factor")
	private float upperUtilizationFactor;
	
	@Column(name="schedule_datetime")
	private Date scheduledDate;
	
	
	@Column(name="job_name")
	private String name;
	
	@Column(name="input_type")
	private String inputType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getShiftLengthPPreferences() {
		return shiftLengthPPreferences;
	}

	public void setShiftLengthPPreferences(String shiftLengthPPreferences) {
		this.shiftLengthPPreferences = shiftLengthPPreferences;
	}

	public float getLowerUtilizationFactor() {
		return lowerUtilizationFactor;
	}

	public void setLowerUtilizationFactor(float lowerUtilizationFactor) {
		this.lowerUtilizationFactor = lowerUtilizationFactor;
	}

	public float getUpperUtilizationFactor() {
		return upperUtilizationFactor;
	}

	public void setUpperUtilizationFactor(float upperUtilizationFactor) {
		this.upperUtilizationFactor = upperUtilizationFactor;
	}

	public Date getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}
}

