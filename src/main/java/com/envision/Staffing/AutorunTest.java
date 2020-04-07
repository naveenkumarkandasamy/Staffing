package com.envision.Staffing;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.envision.Staffing.model.Input;
import com.envision.Staffing.model.JobDetails;
import com.envision.Staffing.model.Response;
import com.envision.Staffing.repository.JobDetailsRepository;
import com.envision.Staffing.services.JobListService;

//@SpringBootApplication
public class AutorunTest implements CommandLineRunner {

	@Autowired
	private JobDetailsRepository jobListService;
	static Logger log = Logger.getLogger(AutorunTest.class);

	public static void main(String[] args) {
		SpringApplication.run(AutorunTest.class, args);
		log.info("Started AutoRun Test Method");
	}

	@Override
	public void run(String... args) throws Exception {

		try {
//			String jobId = "2af196ba-bc91-46af-a774-d3d3451b52ef";
//			JobDetails jobDetails =jobListService.getByIdLeftJoin(jobId);
//			System.out.println(jobDetails.getInputFormat());
//			System.out.println(jobDetails.getName());

		} catch (Exception ex) {
			log.error("Error happened in AutoRun test class ", ex);
			ex.printStackTrace();
		}
	}

}
