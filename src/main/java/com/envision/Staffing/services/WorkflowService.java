package com.envision.Staffing.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.envision.Staffing.ftp.FtpUtil;
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
	Logger log = Logger.getLogger(WorkflowService.class);
	
	private InputStream getInputDataStreamFromAutorunJobDetails(JobDetails jobDetails) {
		String inputType = jobDetails.getInputFormat();
		
		if(inputType.contentEquals("FTP_URL")) {
			log.info("Getting input details from the InputType(FTP_URL)");
			FtpDetails inputFtpDetails = jobDetails.getInputFtpDetails();
			InputStream ftpInputStream= FtpUtil.downloadFile(inputFtpDetails);	
			return ftpInputStream;
		}
		else { // if(inputType.contentEquals("DATA_FILE"))
			log.info("Getting input details from the InputType(DATA_FILE)");
			byte[] inputFile = jobDetails.getInputFileDetails().getDataFile() ;
			InputStream fileInputStream = new ByteArrayInputStream(inputFile);
			return fileInputStream;
		}
	}
	
//	private 
	
	private String getOutputStringFromInputStream(InputStream inputStream, JobDetails jobDetails) throws IOException, Exception {
		log.info("Entering function to get OutputString From InputStream");
		String inputType = jobDetails.getInputFormat();
		String fileExtension;
		String jsonStr;
		if(inputType.contentEquals("FTP_URL")) {
			fileExtension = FilenameUtils.getExtension(jobDetails.getInputFtpDetails().getFileUrl());
		}
		else {
			fileExtension = jobDetails.getInputFileDetails().getFileExtension(); //"xlsx"; // *** needs testing
		}
		log.info("fileExtension  :"+ fileExtension);
		if(fileExtension.contentEquals("xlsx")) {
			log.info("if File Extension is xlsx, retrieving details and convert into json String");
			Input input = shiftPlannerSerivce.processFtpInput(inputStream, jobDetails);
			Output output = shiftPlannerSerivce.getShiftPlan(input);
			ObjectMapper Obj = new ObjectMapper();
			jsonStr = Obj.writeValueAsString(output);
		}
		else {
			log.info("If json String is empty,given file is not an Excel File");
			jsonStr = "";
			System.out.println("Given file is not an Excel file");
		}
		return jsonStr;   
	}
	
	private void sendOutput(JobDetails jobDetails, String jsonStr) {
		log.info("Method for Sending output :");
		String outputType = jobDetails.getOutputFormat();
		if(outputType.contentEquals("EMAIL")) {
			String email = jobDetails.getOutputEmailId();
			sendOutputToEmail(jsonStr, email);
			log.info("if Output type is EMAIL,send output to email '"+email+"' with message --Successful--");
		}
		else{
			log.info("if Output type is not EMAIL,send output string to FtpUrl");
			putOutputStringToFtpUrl(jsonStr, jobDetails);
		}
	}
	
	private void sendOutputToEmail(String jsonStr, String email) {
		emailService.sendMail(email, "WorkflowTest-1", "--Successfull--", jsonStr);
	}
	
	private void putOutputStringToFtpUrl(String jsonStr, JobDetails jobDetails) {
		FtpDetails outputFtpDetails = jobDetails.getOutputFtpDetails();
		FtpUtil.uploadFile(outputFtpDetails, jsonStr);
	}
	
	public void autorunWorkflowService(String jobId) throws Exception{
		
		try {
			JobDetails jobDetails = jobDetailsService.getJobDetailsById(jobId);
		
			InputStream inputStream = getInputDataStreamFromAutorunJobDetails(jobDetails);
			
			String outputJsonString = getOutputStringFromInputStream(inputStream, jobDetails);
			
			sendOutput(jobDetails, outputJsonString);
			log.info("job '"+jobDetails.getName()+"' successfully executed ");
			System.out.println("Job: "+ jobDetails.getName() + " successfully executed ");
			
		} catch (IOException e) {
			log.error("Error happened in autorunWorkflowService :"+e);
			e.printStackTrace();
		}
		
			
//		JobDetails jobDetails = jobDetailsService.getJobDetailsById(jobId);
//		String inputType = jobDetails.getInputFormat();
//		
//		InputStream ftpInputStream;
//		
//		if(inputType.contentEquals("FTP_URL")) {
//			FtpDetails inputFtpDetails = jobDetails.getInputFtpDetails();
//			fileExtension = FilenameUtils.getExtension(jobDetails.getInputFtpDetails().getFileUrl());
//			ftpInputStream= FtpUtil.downloadFile(inputFtpDetails);			
//		}
//		else {
//			byte[] inputFile = jobDetails.getInputFileDetails().getDataFile() ;
//			ftpInputStream = new ByteArrayInputStream(inputFile);
//						
//		}
//
//		if(fileExtension.contentEquals("xlsx")) { // only allow if file is Excel Sheet
//				Input input = shiftPlannerSerivce.processFtpInput(ftpInputStream, jobDetails);
//			try {
//				Output output = shiftPlannerSerivce.getShiftPlan(input);
//				ObjectMapper Obj = new ObjectMapper();
//				String jsonStr = Obj.writeValueAsString(output);
//				String outputType = jobDetails.getOutputFormat();
//				
//				if(outputType.contentEquals("FTP_URL")) {
//					FtpDetails outputFtpDetails = jobDetails.getOutputFtpDetails();
//					if(FtpUtil.uploadFile(outputFtpDetails, jsonStr) == true) {
//						System.out.println("Job: "+ jobDetails.getName() + " successfully executed ");
//					}
//				}
//				else{
//					emailService.sendMail("gundla.sushant@gmail.com", "WorkflowTest-1", "--Successfull--", jsonStr);
//					System.out.println("Job: "+ jobDetails.getName() + " successfully executed ");
//				}
//				
//				
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return jobDetails;
		
	}
}
