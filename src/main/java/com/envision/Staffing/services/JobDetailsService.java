package com.envision.Staffing.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.envision.Staffing.model.Clinician;
import com.envision.Staffing.model.JobDetails;
import com.envision.Staffing.repository.FileDetailsRepository;
import com.envision.Staffing.repository.FtpDetailsRepository;
import com.envision.Staffing.repository.JobDetailsRepository;

@Service
public class JobDetailsService {

	@Autowired
	private JobDetailsRepository jobDetailsRepository;

	@Autowired
	private FtpDetailsRepository ftpDetailsRepository;

	@Autowired
	private FileDetailsRepository fileDetailsRepository;

	@Autowired
	private QuartzSchedulerService quartzSchedulerService;

	public List<JobDetails> getAllJobDetails() {
		List<JobDetails> jobDetailsList = (List<JobDetails>) jobDetailsRepository.findAll();

		if (jobDetailsList.size() > 0) {
			return jobDetailsList;
		} else {
			return new ArrayList<JobDetails>();
		}
	}

	public JobDetails getJobDetailsById(String id) {
		JobDetails jobDetails = jobDetailsRepository.getByIdLeftJoin(id);
		return jobDetails;
//
//		if (jobDetails.isPresent()) {
//			return jobDetails.get();
//		} else {
//			return null;
//			// throw new RecordNotFoundException("No jobDetails record exist for given id");
		// }
	}

	public JobDetails createOrUpdateJobDetails(JobDetails entity, byte[] fileData) {
		String expression = null;
		String status = null;
		String fileId = null;
		String inputFtpId = null;
		String outputFtpId = null;
		int flag = 0;

		if (entity.getInputFormat().equals("DATA_FILE")) {
			entity.getInputFileDetails().setDataFile(fileData);
		}
		String id = entity.getId();
		if (id != null) {
			JobDetails jobdetails = jobDetailsRepository.getByIdLeftJoin(id);
			expression = jobdetails.getCronExpression();
			status = jobdetails.getStatus();
			
			// Updating the null id's of updated jobDetails (entity)
			if (!entity.getClinicians().isEmpty()) {
				this.copyClinician(jobdetails.getClinicians(), entity);
			}
			if (entity.getInputFileDetails() != null && jobdetails.getInputFileDetails() != null) {
				entity.getInputFileDetails().setId(jobdetails.getInputFileDetails().getId());
			} else if (entity.getInputFileDetails() == null && jobdetails.getInputFileDetails() != null) {
				fileId = jobdetails.getInputFileDetails().getId();
				flag = 1;
			}
			if (entity.getInputFtpDetails() != null && jobdetails.getInputFtpDetails() != null) {
				entity.getInputFtpDetails().setId(jobdetails.getInputFtpDetails().getId());
			} else if (entity.getInputFtpDetails() == null && jobdetails.getInputFtpDetails() != null) {
				inputFtpId = jobdetails.getInputFtpDetails().getId();
				flag = 1;
			}
			if (entity.getOutputFtpDetails() != null && jobdetails.getOutputFtpDetails() != null) {
				entity.getOutputFtpDetails().setId(jobdetails.getOutputFtpDetails().getId());
			} else if (entity.getOutputFtpDetails() == null && jobdetails.getOutputFtpDetails() != null) {
				outputFtpId = jobdetails.getOutputFtpDetails().getId();
				flag = 1;
			}
		}

		if (id != null) {
			entity = jobDetailsRepository.save((JobDetails) entity); // updating job details
			if (entity.getInputFileDetails() == null && flag == 1) {
				fileDetailsRepository.deleteJobDetailById(fileId); // deleting previous row of file details if updated
																	// value is null
			}
			if (entity.getInputFtpDetails() == null && flag == 1) {
				ftpDetailsRepository.deleteJobDetailById(inputFtpId);
			}
			if (entity.getOutputFtpDetails() == null && flag == 1) {
				ftpDetailsRepository.deleteJobDetailById(outputFtpId);
			}
		} else {
			entity = jobDetailsRepository.save(entity); // creating job details
		}

		if (entity.getStatus().equals("SCHEDULED") && (id == null || id != null && status.equals("DRAFT"))) {
			quartzSchedulerService.scheduleJob(entity); // add when implementing quartz for Jobs
		} else if (entity.getStatus().equals("SCHEDULED") && id != null) {
			if (status.equals("SCHEDULED") && !entity.getCronExpression().equals(expression)) {
				quartzSchedulerService.rescheduleJob(entity.getId(), entity);
			}
		}
		return entity;
	}

	private void copyClinician(List<Clinician> jobClinician, JobDetails entity) {
		int j;
		for (int i = 0; i < jobClinician.size(); i++) {
			for (j = 0; j < jobClinician.size(); j++) {
				if (entity.getClinicians().get(i).getName().equals(jobClinician.get(j).getName())) {
					break;
				}
			}
			entity.getClinicians().get(i).setId(jobClinician.get(j).getId());
		}
	}

	public void deleteJobDetailsById(String id) {
		Optional<JobDetails> jobDetails = jobDetailsRepository.findById(id);

		if (jobDetails.isPresent()) {
			jobDetailsRepository.deleteById(id);
		} else {
			// throw new RecordNotFoundException("No jobDetails record exist for given id");
		}
	}

}