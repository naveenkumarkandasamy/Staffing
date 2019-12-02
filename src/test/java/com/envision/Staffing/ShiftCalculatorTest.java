package com.envision.Staffing;

import static org.junit.jupiter.api.Assertions.*;

//import org.junit.jupiter.api.Test;
import org.junit.Test;

import com.envision.Staffing.model.Clinician;
import com.envision.Staffing.services.ShiftCalculator;

public class ShiftCalculatorTest{

	@Test
	public void testGetClinicianCountByName() throws Throwable {
		// Given
		ShiftCalculator testObject = new ShiftCalculator();

		// when
		String name = "physician";

		Clinician[] clinicians = new Clinician[3];
		for (int i = 0; i < 3; i++) {
			clinicians[i] = new Clinician();
		}

		int[] countArray = new int[168];
		for (int i = 0; i < 168; i++) {
			countArray[i] = i;
		}
		
		clinicians[0].setName("physician");
		clinicians[0].setClinicianCountPerHour(countArray);

		clinicians[1].setName("app");
		clinicians[1].setClinicianCountPerHour(countArray);

		clinicians[2].setName("scribe");
		clinicians[2].setClinicianCountPerHour(countArray);

		int hour = 11;

		int result = testObject.getClinicianCountByName(name, clinicians, hour);

		// Then
		assertEquals(11, result, 0);

		result = testObject.getClinicianCountByName(name, clinicians, 145);
		assertEquals(145, result, 0);

		name = "app";

		result = testObject.getClinicianCountByName(name, clinicians, 65);
		assertEquals(65, result, 0);

		result = testObject.getClinicianCountByName(name, clinicians, 167);
		assertEquals(167, result, 0);

		name = "Scribe";
		result = testObject.getClinicianCountByName(name, clinicians, 0);
		assertEquals(0, result, 0);

		result = testObject.getClinicianCountByName(name, clinicians, 99);
		assertEquals(99, result, 0);

	}

	@Test
	public void testMin() throws Throwable {
		// Given
		ShiftCalculator testObject = new ShiftCalculator();

		double a = 0.0;
		double b = 1.2;

		double result = testObject.min(a, b);
		assertEquals(0.0, result);

		result = testObject.min(2.2, 2.1);
		assertEquals(2.1, result);

		result = testObject.min(5, 5.1);
		assertEquals(5.0, result);

		result = testObject.min(10, 20);
		assertEquals(10, result);

	}
	
	@Test
	public void testEvaluate() throws Throwable{
		
		// Given
		ShiftCalculator testObject = new ShiftCalculator();
		
		String expression = "1 * physician";
		int hour = 2;
		
		Clinician[] clinicians = new Clinician[3];
		for (int i = 0; i < 3; i++) {
			clinicians[i] = new Clinician();
		}
		
		int[] countArray = new int[168];
		for (int i = 0; i < 168; i++) {
			countArray[i] = i;
		}
		
		clinicians[0].setName("physician");
		clinicians[0].setClinicianCountPerHour(countArray);

		clinicians[1].setName("app");
		clinicians[1].setClinicianCountPerHour(countArray);

		clinicians[2].setName("scribe");
		clinicians[2].setClinicianCountPerHour(countArray);
				
		double result = testObject.evaluate(expression, clinicians , hour);
		assertEquals(2, result);
		
		expression = "2 * physician" ; 
		hour =  100;
		result = testObject.evaluate(expression, clinicians , hour);
		assertEquals(200, result);
		
		expression = "1 * app" ; 
		hour =  67;
		result = testObject.evaluate(expression, clinicians , hour);
		assertEquals(67, result);
		
		expression = "2 * app" ; 
		hour =  43;
		result = testObject.evaluate(expression, clinicians , hour);
		assertEquals(86, result);
		
		
	}
	
	
	@Test
	public void testEvaluateFunction() throws Throwable{
		//Given
		ShiftCalculator testObject = new ShiftCalculator();
		
		Boolean result = testObject.evaluateFunction(12,10,">");
		assertEquals(true , result);
	}
	
	
	@Test
	public void testIsConditionSatisfied() throws Throwable{
		// Given
		ShiftCalculator testObject = new ShiftCalculator();
		
		Clinician[] clinicians = new Clinician[3];
		for (int i = 0; i < 3; i++) {
			clinicians[i] = new Clinician();
		}
		
		int[] countArray = new int[168];
		for (int i = 0; i < 168; i++) {
			countArray[i] = i;
		}
		
		clinicians[0].setName("physician");
		clinicians[0].setClinicianCountPerHour(countArray);
		clinicians[0].setExpressions(null);

		clinicians[1].setName("app");
		clinicians[1].setClinicianCountPerHour(countArray);
		
		String[] expressions = new String[1];
		expressions[0] = "1 * physician";
		clinicians[1].setExpressions(expressions);
		
		String[] scribeExpressions = new String[2];
		scribeExpressions[0] = "1 * physician";
		scribeExpressions[1] = "1 * app";
		clinicians[2].setName("scribe");
		clinicians[2].setClinicianCountPerHour(countArray);
		clinicians[2].setExpressions(scribeExpressions);
		
		int start = 0;
		int shiftLength = 4;
		int index = 1;
		
		boolean result = testObject.isConditionStatisfied(clinicians, start, shiftLength, index);
		//write assertEquals
		assertEquals(false , result);
	}

}
