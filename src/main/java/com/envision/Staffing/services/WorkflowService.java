package com.envision.Staffing.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

	@Autowired
	private EmailService emailService;

	private InputStream getInputDataStreamFromAutorunJobDetails(JobDetails jobDetails) {
		String inputType = jobDetails.getInputFormat();

		if (inputType.contentEquals("FTP_URL")) {
			FtpDetails inputFtpDetails = jobDetails.getInputFtpDetails();
			InputStream ftpInputStream = FtpUtil.downloadFile(inputFtpDetails);
			return ftpInputStream;
		} else { // if(inputType.contentEquals("DATA_FILE"))
			byte[] inputFile = jobDetails.getInputFileDetails().getDataFile();
			InputStream fileInputStream = new ByteArrayInputStream(inputFile);
			return fileInputStream;
		}
	}

	private ByteArrayOutputStream getOutputStringFromInputStream(InputStream inputStream, JobDetails jobDetails)
			throws IOException, Exception {

		String inputType = jobDetails.getInputFormat();
		String fileExtension;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		if (inputType.contentEquals("FTP_URL")) {
			fileExtension = FilenameUtils.getExtension(jobDetails.getInputFtpDetails().getFileUrl());
		} else {
			fileExtension = jobDetails.getInputFileDetails().getFileExtension(); // "xlsx"; // *** needs testing
		}
		if (fileExtension.contentEquals("xlsx")) {
			Input input = shiftPlannerSerivce.processFtpInput(inputStream, jobDetails);
			Output output = shiftPlannerSerivce.getShiftPlan(input);
			bos = shiftPlannerSerivce.excelWriter(output, jobDetails);
		} else {
			System.out.println("Given file is not an Excel file");
		}
		return bos;
	}

	private void sendOutput(JobDetails jobDetails, ByteArrayOutputStream outputExcelData) {
		String outputType = jobDetails.getOutputFormat();
		if (outputType.contentEquals("EMAIL")) {
			String email = jobDetails.getOutputEmailId();
			sendOutputToEmail(outputExcelData, email);
		} else {
			putOutputStringToFtpUrl(outputExcelData, jobDetails);
		}
	}

	private void sendOutputToEmail(ByteArrayOutputStream outputExcelData, String email) {
		emailService.sendMail(email, "WorkflowTest-1", "--Successfull--", outputExcelData);
	}

	private void putOutputStringToFtpUrl(ByteArrayOutputStream outputExcelData, JobDetails jobDetails) {
		FtpDetails outputFtpDetails = jobDetails.getOutputFtpDetails();
		FtpUtil.uploadFile(outputFtpDetails, outputExcelData);
	}

	public void autorunWorkflowService(String jobId) throws Exception {

		try {
			JobDetails jobDetails = jobDetailsService.getJobDetailsById(jobId);

			InputStream inputStream = getInputDataStreamFromAutorunJobDetails(jobDetails);

			ByteArrayOutputStream outputExcelData = getOutputStringFromInputStream(inputStream, jobDetails);

			sendOutput(jobDetails, outputExcelData);

			System.out.println("Job: " + jobDetails.getName() + " successfully executed ");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
