package com.envision.Staffing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.envision.Staffing.services.JobListService;


//@SpringBootApplication
public class AutorunTest implements CommandLineRunner{
	
	@Autowired
	private JobListService jobListService;
	
	public static void main(String[] args) {
		SpringApplication.run(AutorunTest.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
			
		try{
			String jobId = "5466a41b-946f-44e7-a279-049a186d17ba";
//			jobListService.deleteJobById(jobId);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
