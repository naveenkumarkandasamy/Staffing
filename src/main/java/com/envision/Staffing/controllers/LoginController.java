package com.envision.Staffing.controllers;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.envision.Staffing.model.User;
import com.envision.Staffing.services.EmailService;

@RestController
public class LoginController {
	Logger log = Logger.getLogger(LoginController.class);  
	@RequestMapping("/validateLogin") 
	public User validateLogin() {
		log.info("Entered with endpoint :/validateLogin");
		return new User();
	}

}
