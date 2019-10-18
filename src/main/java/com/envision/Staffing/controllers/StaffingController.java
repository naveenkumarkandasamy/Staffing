package com.envision.Staffing.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.envision.Staffing.model.Clinician;
import com.envision.Staffing.model.HourlyDetail;
import com.envision.Staffing.model.Input;
import com.envision.Staffing.services.ShiftPlanningService;

@RestController
public class StaffingController {

	@Autowired
	ShiftPlanningService shiftPlanningService;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

//    @RequestMapping(value="/shiftPlan", method=RequestMethod.POST)
//    public HourlyDetail[] getShiftPlan(@RequestBody Input[] input) throws IOException {
//     	
//    	return shiftPlanningService.getShiftPlan(input);
//     	
//     
//     	
//    }
    
    @RequestMapping(value="/shiftPlan", method=RequestMethod.POST)
    public HourlyDetail[] getShiftPlan(@RequestBody Clinician[] clinician) throws IOException {
     	//checking the input 
//    	System.out.println("Staffing controller \n");
//    	for(Clinician cli : clinician) {
//     		System.out.println(cli.Id + cli.Name + cli.PatientsPerHour + cli.Coefficient + cli.Cost);
//     	}
    	
    	return shiftPlanningService.getShiftPlan(clinician);
     	
     
     	
    }

}
