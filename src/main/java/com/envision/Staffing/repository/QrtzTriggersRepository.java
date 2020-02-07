package com.envision.Staffing.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.envision.Staffing.model.FileDetails;

@Repository
public interface QrtzTriggersRepository extends CrudRepository<FileDetails, String> {
	
}
