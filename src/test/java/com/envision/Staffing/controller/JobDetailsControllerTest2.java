//package com.envision.Staffing.controller;
//
//import static org.junit.Assert.assertThat;
//import static org.mockito.Matchers.any;
//
//import java.util.LinkedList;
//import java.util.List;
//
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.RequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import com.envision.Staffing.model.Clinician;
//import com.envision.Staffing.model.JobDetails;
//import com.envision.Staffing.services.JobDetailsService;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@RunWith(SpringRunner.class)
//@WebMvcTest(value=JobDetailsControllerTest2.class)
//public class JobDetailsControllerTest2 {
//
//	@Autowired
//	private MockMvc mockMvc;
//	
//	@Autowired
//	private JobDetailsService jobDetailsService;
//
//	@Test
//	public void testaddNewJobDetails() throws Exception {
////		input{"name":"Test77","shiftLengthPreferences":["8","6","4"],"lowerUtilizationFactor":0.85,"upperUtilizationFactor":1.1,"clinicians":[{"patientsPerHour":1.2,"capacity":[1,0.83,0.67],"cost":200,"name":"physician","expressions":[]},{"patientsPerHour":0.6,"capacity":[0.6,0.5,0.4],"cost":65,"name":"app","expressions":[]},{"patientsPerHour":0.37,"capacity":[0.15,0.12,0.1],"cost":20,"name":"scribe","expressions":[]}],"cronExpression":null,"inputFormat":"NULL","inputFtpDetails":null,"inputFileDetails":{"fileExtension":"xlsx","dataFile":null},"outputFormat":"NULL","outputFtpDetails":null,"outputEmailId":"","status":"DRAFT","userId":"osatadmin"}
//		
//		List<Clinician> clinicians = new LinkedList<>();
//		Clinician physician = new Clinician();
//		Clinician app = new Clinician();
//		Clinician scribe = new Clinician();
//		physician.setCoefficient(0);
//		physician.setCost(200);
//		physician.setExpressions(null);
//		physician.setName("physician");
//		physician.setPatientsPerHour(1.2);
//		physician.setCapacity(null);
//		
//		app.setCoefficient(0);
//		app.setCost(100);
//		app.setExpressions(null);
//		app.setName("app");
//		app.setPatientsPerHour(0.6);
//		app.setCapacity(null);
//		
//		scribe.setCoefficient(0);
//		scribe.setCost(60);
//		scribe.setExpressions(null);
//		scribe.setName("scribe");
//		scribe.setPatientsPerHour(0.37);
//		scribe.setCapacity(null);
//		
//		clinicians.add(scribe);
//		clinicians.add(app);
//		clinicians.add(physician);
//		
//
//		JobDetails mockJobDetails = new JobDetails();
//		mockJobDetails.setName("");
//		mockJobDetails.setCronExpression(null);
//		mockJobDetails.setInputFileDetails(null);
//		mockJobDetails.setInputFormat(null);
//		mockJobDetails.setInputFtpDetails(null);
//		mockJobDetails.setLowerUtilizationFactor(null);
//		mockJobDetails.setOutputEmailId(null);
//		mockJobDetails.setOutputFormat(null);
//		mockJobDetails.setOutputFtpDetails(null);
//		mockJobDetails.setShiftLengthPreferences(null);
//		mockJobDetails.setStatus("DRAFT");
//		mockJobDetails.setUpperUtilizationFactor(null);
//		mockJobDetails.setClinicians(clinicians);
//
//		String inputInJson = this.mapToJson(mockJobDetails);
//		System.out.println(inputInJson);
//
//		String URI = "/jobDetails/add";
//
//		Mockito.when(jobDetailsService.createOrUpdateJobDetails(Mockito.any(JobDetails.class), null)).thenReturn(mockJobDetails);
//
//		RequestBuilder requestBuilder = MockMvcRequestBuilders
//				.post(URI)
//				.accept(MediaType.APPLICATION_JSON).content(inputInJson)
//				.contentType(MediaType.APPLICATION_JSON);
//
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//		MockHttpServletResponse response = result.getResponse();
//
//		String outputInJson = response.getContentAsString();
//
//		Assert.assertEquals(outputInJson, inputInJson);
//		Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
//	}
//	
//	private String mapToJson(Object object) throws JsonProcessingException {
//		ObjectMapper objectMapper = new ObjectMapper();
//		return objectMapper.writeValueAsString(object);
//	}
//
//	
//}
