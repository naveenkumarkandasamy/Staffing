package com.envision.Staffing.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.envision.Staffing.model.JobDetails;

@Repository
public interface JobDetailsRepository extends CrudRepository<JobDetails, String> {

}
