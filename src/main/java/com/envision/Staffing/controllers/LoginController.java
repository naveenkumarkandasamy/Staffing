package com.envision.Staffing.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.envision.Staffing.model.User;

@RestController
public class LoginController {

	@RequestMapping("/validateLogin")
	public User validateLogin() {
		return new User();
	}

}
