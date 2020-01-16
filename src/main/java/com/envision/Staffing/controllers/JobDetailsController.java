package com.envision.Staffing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.envision.Staffing.model.JobDetails;
import com.envision.Staffing.services.JobDetailsService;

@Controller
@RequestMapping(path="/jobDetails") 
public class JobDetailsController {

	@Autowired
	private JobDetailsService jobDetailsService;

	@PostMapping(path = "/add") 
	public @ResponseBody String addNewUser(@RequestBody JobDetails jobDetails) {
		jobDetailsService.createOrUpdateJobDetails(jobDetails);
		return "Saved";
	}

	@GetMapping(path = "/all")
	public @ResponseBody Iterable<JobDetails> getAllJobDetails() {
		// This returns a JSON or XML with the users
		return jobDetailsService.getAllJobDetails();
	}
}
