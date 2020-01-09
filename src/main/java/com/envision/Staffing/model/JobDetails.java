package com.envision.Staffing.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "job_details")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobDetails {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy =  "org.hibernate.id.UUIDGenerator")
	private String id;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "shift_length_preferences")
	private String shiftLengthPreferences;

	@Column(name = "lower_utilization_factor")
	private Float lowerUtilizationFactor;

	@Column(name = "upper_utilization_factor")
	private Float upperUtilizationFactor;

	@Column(name = "schedule_datetime")
	private Date scheduledDate;

	@Column(name = "job_name")
	private String name;

	@Column(name = "input_type")
	private String inputFormat;

	@OneToOne(fetch = FetchType.EAGER, optional = false, cascade= CascadeType.ALL)
	@JoinColumn(name = "input_id")
	private FtpDetails ftpDetails;
	
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

	public String getShiftLengthPreferences() {
		return shiftLengthPreferences;
	}

	public void setShiftLengthPreferences(String shiftLengthPreferences) {
		this.shiftLengthPreferences = shiftLengthPreferences;
	}

	public Float getLowerUtilizationFactor() {
		return lowerUtilizationFactor;
	}

	public void setLowerUtilizationFactor(Float lowerUtilizationFactor) {
		this.lowerUtilizationFactor = lowerUtilizationFactor;
	}

	public Float getUpperUtilizationFactor() {
		return upperUtilizationFactor;
	}

	public void setUpperUtilizationFactor(Float upperUtilizationFactor) {
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

	public String getInputFormat() {
		return inputFormat;
	}

	public void setInputFormat(String inputFormat) {
		this.inputFormat = inputFormat;
	}

	public FtpDetails getFtpDetails() {
		return ftpDetails;
	}

	public void setFtpDetails(FtpDetails ftpDetails) {
		this.ftpDetails = ftpDetails;
	}


}
