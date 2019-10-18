package com.envision.Staffing.services;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.envision.Staffing.model.Clinician;

@Service
public class MinCostClinicianCalculator {

	
	//function to get the clinician with the minimum cost
	public  Clinician getMinCostClinician(Clinician[] clinician, int k) {
				
		
		Clinician[] sorted_clinician = clinician;
		Arrays.sort(sorted_clinician);
		
		return sorted_clinician[k-1];
		
		
	}
}
