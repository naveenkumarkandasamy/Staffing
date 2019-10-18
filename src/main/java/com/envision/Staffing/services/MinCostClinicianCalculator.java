package com.envision.Staffing.services;

import org.springframework.stereotype.Service;

import com.envision.Staffing.model.Clinician;

@Service
public class MinCostClinicianCalculator {

	
	//function to get the clinician with the minimum cost
	public  Clinician getMinCostClinician(Clinician[] clinician, int k) {
		
		int min_cost= Integer.MAX_VALUE;
		Clinician min_clinician = null;
		for(Clinician cli : clinician) {
			if(cli.Cost < min_cost) {
				min_cost=cli.Cost;
				min_clinician = cli;
			}
		}
		return min_clinician;
	}
}
