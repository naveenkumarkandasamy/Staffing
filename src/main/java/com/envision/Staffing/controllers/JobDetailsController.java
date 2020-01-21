package com.envision.Staffing.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.envision.Staffing.model.JobDetails;
import com.envision.Staffing.services.JobDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(path = "/jobDetails")
public class JobDetailsController {

	@Autowired
	private JobDetailsService jobDetailsService;

	@RequestMapping(path = "/add", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	@ResponseBody
	public String addNewJobDetails(@RequestPart(required = false) MultipartFile file,
			@RequestPart("input") String input) throws IOException {
		JobDetails jobDetails = new ObjectMapper().readValue(input, JobDetails.class);
		if (file != null && jobDetails != null)
			jobDetailsService.createOrUpdateJobDetails(jobDetails, file.getBytes());
		else
			jobDetailsService.createOrUpdateJobDetails(jobDetails, null);
		return "Saved";
	}

	@GetMapping(path = "/all")
	public @ResponseBody Iterable<JobDetails> getAllJobDetails() {
		return jobDetailsService.getAllJobDetails();
	}

}
