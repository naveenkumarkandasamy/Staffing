package com.envision.Staffing.services;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.naming.factory.SendMailFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.envision.Staffing.ftp.FtpUtil;
import com.envision.Staffing.model.FileDetails;
import com.envision.Staffing.model.FtpDetails;
import com.envision.Staffing.model.Input;
import com.envision.Staffing.model.JobDetails;
import com.envision.Staffing.model.Output;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class WorkflowService {

	@Autowired
	private JobDetailsService jobDetailsService;
	
	@Autowired
	private ShiftPlanningService shiftPlannerSerivce;
	
	@Autowired
	private EmailService emailService;
	
	public JobDetails autorunWorkflowService(String jobId){
		JobDetails jobDetails = jobDetailsService.getJobDetailsById(jobId);
		String inputType = jobDetails.getInputFormat();
		String fileExtension;
		InputStream ftpInputStream;
		
		if(inputType.contentEquals("FTP_URL")) {
			FtpDetails inputFtpDetails = jobDetails.getInputFtpDetails();
			fileExtension = FilenameUtils.getExtension(jobDetails.getInputFtpDetails().getFileUrl());
			ftpInputStream= FtpUtil.downloadFile(inputFtpDetails);			
		}
		else {
			byte[] inputFile = jobDetails.getInputFileDetails().getDataFile() ;
			ftpInputStream = new ByteArrayInputStream(inputFile);
			fileExtension = "xlsx"; // ***			
		}

		if(fileExtension.contentEquals("xlsx")) { // only allow if file is Excel Sheet
				Input input = shiftPlannerSerivce.processFtpInput(ftpInputStream, jobDetails);
			try {
				Output output = shiftPlannerSerivce.getShiftPlan(input);
				ObjectMapper Obj = new ObjectMapper();
				String jsonStr = Obj.writeValueAsString(output);
				String outputType = jobDetails.getOutputFormat();
				
				if(outputType.contentEquals("FTP_URL")) {
					FtpDetails outputFtpDetails = jobDetails.getOutputFtpDetails();
					if(FtpUtil.uploadFile(outputFtpDetails, jsonStr) == true) {
						System.out.println("Job created successfully with Job Name: "+ jobDetails.getName());
					}
				}
				else{
					emailService.sendMail("gundla.sushant@gmail.com", "WorkflowTest-1", "--Successfull--", jsonStr);
					System.out.println("Output to Email");
					System.out.println("Job created successfully with Job Name: "+ jobDetails.getName());
				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return jobDetails;
		
	}
}
