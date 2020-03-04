package com.envision.Staffing.services;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.envision.Staffing.model.JobDetails;
import com.envision.Staffing.repository.FileDetailsRepository;
import com.envision.Staffing.repository.FtpDetailsRepository;
import com.envision.Staffing.repository.JobDetailsRepository;

@Service
public class JobListService {
	
	@Autowired
	private FileDetailsRepository fileDetailsRepository;
	@Autowired
	
	private FtpDetailsRepository ftpDetailsRepository;
	@Autowired
	
	private JobDetailsRepository jobDetailsRepository;
	
	@Autowired
	private JobDetailsService jobDetailsService;
	
	@Autowired
	private Scheduler scheduler;
	
	public boolean deleteJobById(String jobId) {
		boolean flag;
		try{
			JobDetails jobDetails = jobDetailsService.getJobDetailsById(jobId);
			
			jobDetailsRepository.deleteJobDetailById(jobId); // deletes job and clinicians
			if(jobDetails.getInputFormat().contentEquals("FTP_URL")) {
				ftpDetailsRepository.deleteById(jobDetails.getInputFtpDetails().getId()); // deletes input_ftp
			}
			else if(jobDetails.getInputFormat().contentEquals("DATA_FILE")){
				fileDetailsRepository.deleteById(jobDetails.getInputFileDetails().getId()); // deletes input_file
			}
			if(jobDetails.getOutputFormat().contentEquals("FTP_URL")) {
				ftpDetailsRepository.deleteById(jobDetails.getOutputFtpDetails().getId()); // deletes output_ftp
			}
			if(jobDetails.getStatus().contentEquals("SCHEDULED")) {
				scheduler.deleteJob(new JobKey(jobId, Scheduler.DEFAULT_GROUP)); // deletes related jobs
			}
			flag = true;
			
		}catch(Exception ex) {
			flag = false;
			ex.printStackTrace();
		}
		return flag;
	}

}
