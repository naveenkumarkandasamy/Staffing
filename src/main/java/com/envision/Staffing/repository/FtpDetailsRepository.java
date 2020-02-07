package com.envision.Staffing.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.envision.Staffing.model.FtpDetails;

@Repository
public interface FtpDetailsRepository extends CrudRepository<FtpDetails, String> {	
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query("delete from FtpDetails f where f.id=:id")
	void deleteJobDetailById(@Param("id") String id);
	
}
