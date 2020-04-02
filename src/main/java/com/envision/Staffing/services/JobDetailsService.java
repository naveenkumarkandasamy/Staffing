package com.envision.Staffing.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
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
	Logger log = Logger.getLogger(JobDetailsService.class);

	public List<JobDetails> getAllJobDetails() {
		log.info("Entering method to get all job Details ");
		List<JobDetails> jobDetailsList = (List<JobDetails>) jobDetailsRepository.findAll();

		if (jobDetailsList.size() > 0) {
			return jobDetailsList;
		} else {
			return new ArrayList<JobDetails>();
		}
	}

	public JobDetails getJobDetailsById(String id) {
		log.info("Entering method to get job Details by using id");
		JobDetails jobDetails = jobDetailsRepository.getByIdLeftJoin(id);
		log.info(jobDetails.getName());
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

		if (entity.getInputFormat().equals("DATA_FILE")) {
			entity.getInputFileDetails().setDataFile(fileData);
		}
		String id = entity.getId();
		if (id != null) {
			JobDetails jobdetails = jobDetailsRepository.getByIdLeftJoin(id);
			expression = jobdetails.getCronExpression();
			status = jobdetails.getStatus();
		}

		if (id != null) {
			log.info("Updating job Details");
			entity = jobDetailsRepository.save((JobDetails) entity); // updating job details
		} else {
			entity = jobDetailsRepository.save(entity);
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

	public void deleteJobDetailsById(String id) {
		log.info("Entering method to delete job Details by id ");
		Optional<JobDetails> jobDetails = jobDetailsRepository.findById(id);

		if (jobDetails.isPresent()) {
			jobDetailsRepository.deleteById(id);
		} else {
			// throw new RecordNotFoundException("No jobDetails record exist for given id");
		}
	}

}
