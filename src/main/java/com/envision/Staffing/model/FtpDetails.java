package com.envision.Staffing.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "ftp_details")
@JsonIgnoreProperties(ignoreUnknown = true)
public class FtpDetails {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy =  "org.hibernate.id.UUIDGenerator")
	private String id;
	
	@Column(name="file_url")
	private String fileUrl;
	
	private String username;
	
	private String password;
	
	@Transient
	private String host;
	
	@Transient
	private String fileName;
	
	@Transient
	private String dirPath;
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDirPath() {
		return dirPath;
	}

	public void setDirPath(String dirPath) {
		this.dirPath = dirPath;
	}
	
	public FtpDetails() {
		super();
	}
	

	public FtpDetails(String fileUrl, String username, String password) {
		super();
		this.fileUrl = fileUrl;
		this.username = username;
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
