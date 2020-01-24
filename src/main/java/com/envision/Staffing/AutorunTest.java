package com.envision.Staffing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.envision.Staffing.model.JobDetails;
import com.envision.Staffing.services.WorkflowService;

@SpringBootApplication
public class AutorunTest implements CommandLineRunner{
	
	@Autowired
	private WorkflowService workflowService;

	public static void main(String[] args) {
		SpringApplication.run(AutorunTest.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		String jobId = "666d4d29-1bb4-4256-90fa-2dd870f96f6d";
		JobDetails jobDetails = workflowService.autorunWorkflowService("666d4d29-1bb4-4256-90fa-2dd870f96f6d");
	}

}
