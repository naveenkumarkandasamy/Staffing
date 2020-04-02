package com.envision.Staffing.services;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
//import org.junit.jupiter.api.Test;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.envision.Staffing.model.Clinician;
import com.envision.Staffing.services.ShiftCalculator;

//@RunWith(SpringRunner.class)
@RunWith(MockitoJUnitRunner.Silent.class)
@SpringBootTest
public class ShiftCalculatorTest {

	@Autowired
	ShiftCalculator testObject;

	Clinician[] clinicians;

	@Before
	public void initializeCliniciansArray() {
		clinicians = new Clinician[3];
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
		clinicians[0].setCost(200);

		clinicians[1].setName("app");
		clinicians[1].setClinicianCountPerHour(countArray);
		clinicians[1].setCost(65);

		List<String> appExpressions = new ArrayList<>();
		appExpressions.add("1 * physician");
		clinicians[1].setExpressions(appExpressions);

		List<String> scribeExpressions = new ArrayList<>();
		scribeExpressions.add("1 * physician");
		scribeExpressions.add("1 * app");
		clinicians[2].setName("scribe");
		clinicians[2].setClinicianCountPerHour(countArray);
		clinicians[2].setExpressions(scribeExpressions);
		clinicians[2].setCost(20);
		// return clinicians;
	}

	@Test
	public void testGetClinicianCountByName() throws Throwable {
		// Given
		ShiftCalculator testObject = new ShiftCalculator();

		// when
		String name = "physician";

//		Clinician[] clinicians = initializeCliniciansArray();

		int hour = 11;

		int result = testObject.getClinicianCountByName(name, clinicians, hour);

		// Then
		Assert.assertEquals(11, result, 0);

		result = testObject.getClinicianCountByName(name, clinicians, 145);
		Assert.assertEquals(145, result, 0);

		name = "app";

		result = testObject.getClinicianCountByName(name, clinicians, 65);
		Assert.assertEquals(65, result, 0);

		result = testObject.getClinicianCountByName(name, clinicians, 167);
		Assert.assertEquals(167, result, 0);

		name = "Scribe";
		result = testObject.getClinicianCountByName(name, clinicians, 0);
		Assert.assertEquals(0, result, 0);

		result = testObject.getClinicianCountByName(name, clinicians, 99);
		Assert.assertEquals(99, result, 0);

	}

	@Test
	public void testMin() throws Throwable {
		// Given

		double a = 0.0;
		double b = 1.2;

		double result = testObject.min(a, b);
		Assert.assertEquals(0.0, result, 0.001);

		result = testObject.min(2.2, 2.1);
		Assert.assertEquals(2.1, result, 0.01);

		result = testObject.min(5, 5.1);
		Assert.assertEquals(5.0, result, 0.01);

		result = testObject.min(10, 20);
		Assert.assertEquals(10, result, 0.01);

	}

	@Test
	public void testEvaluate() throws Throwable {

		// Given
//		ShiftCalculator testObject = new ShiftCalculator();

		String expression = "1 * physician";
		int hour = 2;

		// Clinician[] clinicians = initializeCliniciansArray();

		double result = testObject.evaluate(expression, clinicians, hour);
		Assert.assertEquals(2, result, 0.001);

		expression = "2 * physician";
		hour = 100;
		result = testObject.evaluate(expression, clinicians, hour);
		Assert.assertEquals(200, result, 0.001);

		expression = "1 * app";
		hour = 67;
		result = testObject.evaluate(expression, clinicians, hour);
		Assert.assertEquals(67, result, 0.001);

		expression = "2 * app";
		hour = 43;
		result = testObject.evaluate(expression, clinicians, hour);
		Assert.assertEquals(86, result, 0.001);
	}

	@Test
	public void testEvaluateFunction() throws Throwable {
		// Given
		// ShiftCalculator testObject = new ShiftCalculator();

		Boolean result = testObject.evaluateFunction(12, 10, ">");
		Assert.assertEquals(true, result);
	}

	@Test
	public void testIsConditionSatisfied() throws Throwable {
		// Given
		int start = 0;
		int shiftLength = 4;
		int index = 1;
		boolean result = testObject.isConditionStatisfied( clinicians, start, shiftLength, index);
		// write Assert.assertEquals
		Assert.assertEquals(false, result);
	}

	@Test
	public void testIsConditionSatisfied_Phyiscian() throws Throwable {
		// Given
		int start = 0;
		int shiftLength = 4;
		int index = 0;
		boolean result = testObject.isConditionStatisfied( clinicians, start, shiftLength, index);
		// write Assert.assertEquals
		Assert.assertEquals(true, result);
	}

	@Test
	public void testGetClinicianWithLeastCost() throws Throwable {
		// Given
//		ShiftCalculator testObject = new ShiftCalculator();

		// when
		// Clinician[] clinicians = initializeCliniciansArray();
		Clinician result = testObject.getClinicianWithLeastCost(2, clinicians);

		// Then
		Assert.assertEquals("scribe", result.getName());
	}

}