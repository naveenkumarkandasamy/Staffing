package com.envision.Staffing.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.envision.Staffing.model.JobDetails;
import com.envision.Staffing.repository.JobDetailsRepository;

@Service
public class JobDetailsService {

	@Autowired
	private JobDetailsRepository jobDetailsRepository;
	
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
	//	}
	}

	public JobDetails createOrUpdateJobDetails(JobDetails entity, byte[] fileData) {
		if (entity.getInputFormat().equals("DATA_FILE")) {
			entity.getInputFileDetails().setDataFile(fileData);
		}
		entity = jobDetailsRepository.save(entity);
		quartzSchedulerService.scheduleJob(entity); // add when implementing quartz for Jobs
		return entity;
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
