package com.envision.Staffing;

import org.junit.runner.*;
import org.junit.runner.notification.Failure;

class AllTestsRunner {

	public static void main(String[] args) {
//		Result result = JUnitCore.runClasses(AllTestsSuite.class);

		Result result = JUnitCore.runClasses(ShiftCalculatorTest.class);

		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}

		System.out.println(result.wasSuccessful());
	}
}
