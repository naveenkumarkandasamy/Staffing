package com.envision.Staffing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.envision.Staffing.model.Clinician;
import com.envision.Staffing.services.ShiftCalculator;
import com.envision.Staffing.services.ShiftPlanningService;

class ShiftCalculatorTest {
	
	
	@Test
	public void testGetClinicianCountByName() throws Throwable{
		//Given
		ShiftCalculator testObject = new ShiftCalculator();
		
		//when
		String name = "physician";
		
		Clinician[] clinicians = new Clinician[3];
		for(int i=0;i<3;i++) {
			clinicians[i] = new Clinician();
		}
		
		int[] countArray = new int[168];
		for(int i=0;i<168;i++) {
			countArray[i]=i;
		}
		countArray[11] = 11;
		clinicians[0].setName("physician");
		clinicians[0].setClinicianCountPerHour(countArray);

		clinicians[1].setName("app");
		clinicians[1].setClinicianCountPerHour(countArray);

		clinicians[2].setName("scribe");
		clinicians[2].setClinicianCountPerHour(countArray);

	    int hour = 11;
		
		int result = testObject.getClinicianCountByName(name, clinicians, hour);
		
		//Then
		assertEquals(11,result,0);
		
		
		result = testObject.getClinicianCountByName(name, clinicians, 145);
		assertEquals(145,result,0);
		
		
		name="app";
		
		result = testObject.getClinicianCountByName(name, clinicians, 65);
		assertEquals(65,result,0);
		
		result = testObject.getClinicianCountByName(name, clinicians, 167);
		assertEquals(167,result,0);
		
		
		name="Scribe";
		result = testObject.getClinicianCountByName(name, clinicians, 0);
		assertEquals(0,result,0);
		
		result = testObject.getClinicianCountByName(name,clinicians,99);
		assertEquals(99,result,0);
		
		
		
		
		
	
	}

//	@Test
//	void test() {
//		fail("Not yet implemented");
//	}

}
