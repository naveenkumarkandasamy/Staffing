package com.envision.Staffing.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.envision.Staffing.ftp.FtpUtil;
import com.envision.Staffing.model.FtpDetails;
import com.envision.Staffing.model.Input;
import com.envision.Staffing.model.JobDetails;
import com.envision.Staffing.model.Output;

@Component
public class WorkflowService {

	@Autowired
	private JobDetailsService jobDetailsService;
	
	@Autowired
	private ShiftPlanningService shiftPlannerSerivce;
	
	public JobDetails autorunWorkflowService(String jobId){
		JobDetails jobDetails = jobDetailsService.getJobDetailsById(jobId);
	
		String ftpUrl = jobDetails.getInputFtpDetails().getFileUrl();
		String username = jobDetails.getInputFtpDetails().getUsername();
		String password = jobDetails.getInputFtpDetails().getPassword();
		String fileExtension = FilenameUtils.getExtension(ftpUrl);
		
		InputStream ftpInputStream= FtpUtil.downloadFile(ftpUrl, username, password);
		if(fileExtension.contentEquals("xlsx")) { // only allow if file is Excel Sheet
			Input input = shiftPlannerSerivce.processFtpInput(ftpInputStream, jobDetails);
			try {
				Output output = shiftPlannerSerivce.getShiftPlan(input);
								
				if(FtpUtil.uploadFile(ftpUrl, username, password, output) == true) {
					System.out.println("Job created successfully with Job Name: "+ jobDetails.getName());
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return jobDetails;
		
	}
}
