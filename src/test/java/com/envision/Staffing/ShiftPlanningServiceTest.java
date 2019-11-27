package com.envision.Staffing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.envision.Staffing.model.Clinician;
import com.envision.Staffing.services.ShiftPlanningService;

class ShiftPlanningServiceTest {

	
	@Test
	public void testCalculateHourlyCost() throws Throwable {
		//Given
		ShiftPlanningService testObject = new ShiftPlanningService();
		
		//When
		Clinician[] clinicians = new Clinician[3];
		
		for(int i=0;i<3;i++) {
			clinicians[i] = new Clinician(); 	
		}
		
        int[] clinicianCount = new int[168];
		for(int i =0;i<168;i++) {
        	clinicianCount[i] = 1;
        }

		 
		//physician
		clinicians[0].setCost(200);
        clinicians[0].setClinicianCountPerHour(clinicianCount);		
        
        //app
        clinicians[1].setCost(65);
        clinicians[0].setClinicianCountPerHour(clinicianCount);
        
        
        
        //scribe
		clinicians[0].setCost(20);
        clinicians[0].setClinicianCountPerHour(clinicianCount);
        
		
        
        
		//Then
//        assertEquals(43901760, );
	}
	@Test
	void test() {
		fail("Not yet implemented");
	}

}
