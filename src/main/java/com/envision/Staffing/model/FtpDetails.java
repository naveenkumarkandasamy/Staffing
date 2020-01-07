package com.envision.Staffing.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "ftp_details")
public class FtpDetails {
	
	@javax.persistence.Id
	private String Id;
	
	@Column(name="file_url")
	private String fileUrl;
	
	private String username;
	
	private String password;

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
