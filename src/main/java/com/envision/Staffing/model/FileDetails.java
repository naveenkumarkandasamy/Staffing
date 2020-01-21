package com.envision.Staffing.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="file_details")
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileDetails {
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy =  "org.hibernate.id.UUIDGenerator")
	private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte[] getDataFile() {
		return dataFile;
	}

	public void setDataFile(byte[] dataFile) {
		this.dataFile = dataFile;
	}

	@Column(name="data_file")
	private byte[] dataFile;

}
