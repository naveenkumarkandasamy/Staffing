package com.envision.Staffing.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.envision.Staffing.model.Payload;
import com.envision.Staffing.services.ShiftPlanningService;

@RestController
public class StaffingController {

	@Autowired
	ShiftPlanningService shiftPlanningService;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping(value="/shiftPlan", method=RequestMethod.POST)
    public void getShiftPlan(@RequestBody Payload p1 ) throws IOException {
    	shiftPlanningService.getShiftPlan(p1);
    }

}
