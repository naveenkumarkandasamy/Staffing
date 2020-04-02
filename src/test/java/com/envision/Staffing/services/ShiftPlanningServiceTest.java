package com.envision.Staffing.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.envision.Staffing.model.Clinician;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShiftPlanningServiceTest {

	// partial code

	@Test
	public void testCalculateHourlyCost() throws Throwable {
		// Given
		ShiftPlanningService testObject = new ShiftPlanningService();

		// When
		Clinician[] clinicians = new Clinician[3];

		for (int i = 0; i < 3; i++) {
			clinicians[i] = new Clinician();
		}

		int[] clinicianCount = new int[168];
		for (int i = 0; i < 168; i++) {
			clinicianCount[i] = 1;
		}

		// physician
		clinicians[0].setCost(200);
		clinicians[0].setClinicianCountPerHour(clinicianCount);

		// app
		clinicians[1].setCost(65);
		clinicians[0].setClinicianCountPerHour(clinicianCount);

		// scribe
		clinicians[0].setCost(20);
		clinicians[0].setClinicianCountPerHour(clinicianCount);

		// Then
//        assertEquals(43901760, );
	}
//	@Test
//	void test() {
//		fail("Not yet implemented");
//	}

}