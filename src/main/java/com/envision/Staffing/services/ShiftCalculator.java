package com.envision.Staffing.services;

import java.util.ArrayList;
import java.util.List;

import com.envision.Staffing.model.Clinician;
import com.envision.Staffing.model.HourlyDetail;
import com.envision.Staffing.model.Shift;
import com.envision.Staffing.model.Workload;

public class ShiftCalculator {

	Workload wl;

	public void setWorkloads(Workload w) {
		// Initialize workload array
		wl = w;
	}

	public void calculatePhysicianSlotsForAll(int shiftLength, Clinician[] clinicians) {
		int start = 0;
		double factor = 0.75;

		// Check every 12 hour slot Eg : 0-12 , 1-13, 2-14 ..... (Assuming numberOfHours
		// = 12)
		while (start + shiftLength < wl.sizeOfArray) {
			int index = clinicians.length - 1;
			int flag = 1;
			boolean conditionalValue = false;
			boolean array[] = { true, true, true };

			for (; index >= 0; index--) {
				Clinician clinician = getClinicianWithLeastCost(index, clinicians);
				conditionalValue = isConditionStatisfied(clinicians, start, shiftLength, index);
				flag = 1;
				do {
					Shift newShift = getNewShift(shiftLength, start, clinician.getDescription());
					flag = checkIfPhysicianToBeAdded(shiftLength, start, factor, newShift,
							clinician.getClinicianCapacity());

					if (flag == 1 && conditionalValue) { // add clinician and check with next
						addNewShift(start, shiftLength, newShift, clinician.getClinicianCapacity(),
								clinician.getClinicianCountPerHour());
					} else if (flag == 0 && index == clinicians.length - 1) {
						start = start + 1; // since scribe cannot be added
						break;
					} else {
						array[index] = false;
					}
					conditionalValue = isConditionStatisfied(clinicians, start, shiftLength, index);
				} while (conditionalValue && flag == 1 && index != 0);
			}
			for (boolean value : array) {
				if (value)
					break;
				start = start + 1;
			}
		}
	}

	private double evaluate(String expression, int value) {

		String[] elements = expression.split(" ");
		switch (elements[1]) {
		case "+":
			return Double.parseDouble(elements[0]) + value;
		case "*":
			return Double.parseDouble(elements[0]) * value;
		default:
			return 0;
		}

	}

	private boolean isConditionStatisfied(Clinician[] clinicians, int start, int shiftLength, int index) {
		if (index == 0)
			return true;
		else {
			for (int hour = start; hour < start + shiftLength; hour++) {
				double value = 0.0d;
				for (int j = 0; j < index; j++) {
					value += evaluate(clinicians[index].getExpressions()[j],
							clinicians[j].getClinicianCountPerHour()[hour]);
				}
				if (evaluateFunction(clinicians[index].getClinicianCountPerHour()[hour] + 1, value, ">"))
					// TODO: handle different operators here instead of '<='
					return false;
			}
		}
		return true;
	}

	private boolean evaluateFunction(double lefthandValue, double rightHandValue, String operator) {
		switch (operator) {
		case "<":
			return lefthandValue < rightHandValue;
		case ">":
			return lefthandValue > rightHandValue;
		case ">=":
			return lefthandValue >= rightHandValue;
		case "<=":
			return lefthandValue <= rightHandValue;
		case "==":
			return lefthandValue == rightHandValue;
		case "!=":
			return lefthandValue != rightHandValue;
		}
		return false;
	}

	private Clinician getClinicianWithLeastCost(int index, Clinician[] clinicians) {
		// TODO return clinician with ith least cost
		return clinicians[index];
	}

	private void addNewShift(int start, int shiftLength, Shift newShift, double[] capacity, int[] clinicianCounter) {
		// A 12 hour slot can be added
		calculateNewWorkloads(start, start + shiftLength, capacity);
		calculateCapacities(start, start + shiftLength, capacity);
		for (int x = start; x < start + shiftLength; x++) {
			// Increase the count of the number of physicians used each hour for the 12-hour
			// slot
			clinicianCounter[x]++;
		}
		wl.hourlyDetailList[start].incrementNumberOfShiftBeginning();
		wl.hourlyDetailList[start + shiftLength - 1].incrementNumberOfShiftEnding();
		wl.result.add(newShift);
	}

	private Shift getNewShift(int numberOfHours, int start, String physicianType) {
		Shift newShift = new Shift();
		newShift.start_time = start % wl.dayDuration;
		newShift.end_time = (start + numberOfHours) % wl.dayDuration;
		newShift.day = start / wl.dayDuration;
		newShift.no_of_hours = numberOfHours;
		newShift.setPhysicianType(physicianType);
		return newShift;
	}

	private int checkIfPhysicianToBeAdded(int numberOfHours, int start, double factor, Shift newShift,
			double[] physicianCapacity) {
		int flag = 1;
		double capacityOfCurrentDoctor = 0;
		for (int j = start; j < start + numberOfHours; j++) {

			double value = wl.fixedworkloadArray[j] - wl.capacityArray[j];
			if (value < 0)
				value = 0;
			if ((j - start) % wl.dayDuration == 0) {
				capacityOfCurrentDoctor = round(capacityOfCurrentDoctor + min(physicianCapacity[0], value), 2);

			} else if ((j - start) % wl.dayDuration == numberOfHours - 1) {
				capacityOfCurrentDoctor = round(capacityOfCurrentDoctor + min(physicianCapacity[2], value), 2);

			} else {
				capacityOfCurrentDoctor = round(capacityOfCurrentDoctor + min(physicianCapacity[1], value), 2);

			}

			// Checks if adding a physician is needed
			if ((j - start) == (numberOfHours - 1)
					&& ((capacityOfCurrentDoctor / (numberOfHours * physicianCapacity[1])) < factor)) {
				flag = 0;
				break;
			}
		}
		return flag;
	}

	public void calculate4hourslots(Clinician[] clinicians) {
		int start = 0;
		int sizeOfSlot = 4;
		boolean flag = false;
		// A four - hour slot is added whenever there are 2 consecutive slots where
		// utilization > given range (110%)
		while (start <= wl.sizeOfArray - sizeOfSlot) {
			int j = start;
			flag =false;
			if (clinicians[0].getClinicianCountPerHour()[j] != 0
					&& (round(wl.fixedworkloadArray[j] - wl.capacityArray[j], 2))
							/ clinicians[0].getClinicianCountPerHour()[j] <= 0.1) {
				flag = true;
			}
			if (flag) {
				start = start + 1;
			} else {
				boolean conditionalValue = true;
				for (int i = clinicians.length -1; i >=0; i--) {
					Clinician clinician = getClinicianWithLeastCost(i, clinicians);
					conditionalValue = isConditionStatisfied(clinicians, start, sizeOfSlot, i);
					while (conditionalValue) {
						Shift newShift = getNewShift(sizeOfSlot, start, clinician.getDescription());
						if ((start + sizeOfSlot - 1) > wl.sizeOfArray ) {
							// dont do anything
							addNewShift(start, sizeOfSlot, newShift, clinician.getClinicianCapacity(),
									clinician.getClinicianCountPerHour());
						} else {
							addNewShift(start, sizeOfSlot, newShift, clinician.getClinicianCapacity(),
									clinician.getClinicianCountPerHour());

						}
						conditionalValue = isConditionStatisfied(clinicians, start, sizeOfSlot, i); 
						if(i==0 ) break;
					}

				}
				flag =false;
			}
			
		}
	}

	public void calculateNewWorkloads(int start, int end, double[] capacity) {
		// Reduces the workloads as per the capacities of each physician per hour
		for (int i = start; i < end; i++) {
			if (i == start) {
				wl.workloadArray[i] = round(wl.workloadArray[i] - capacity[0], 2);
			} else if (i == end - 1) {
				wl.workloadArray[i] = round(wl.workloadArray[i] - capacity[2], 2);

			} else {
				wl.workloadArray[i] = round(wl.workloadArray[i] - capacity[1], 2);
			}
		}

	}

	public void calculateCapacities(int start, int end, double[] capacity) {
		for (int i = start; i < end; i++) {
			if (i == start) {
				wl.capacityArray[i] = round(wl.capacityArray[i] + capacity[0], 2);
			} else if (i == end - 1) {
				wl.capacityArray[i] = round(wl.capacityArray[i] + capacity[2], 2);
			} else {
				wl.capacityArray[i] = round(wl.capacityArray[i] + capacity[1], 2);
			}
		}
	}

	public double round(double value, int places) {
		long factor = (long) Math.pow(10, places);
		value = value * factor;
		double tmp = Math.round(value);
		return tmp / factor;
	}

	public double min(double a, double b) {
		if (a > b)
			return b;
		else
			return a;
	}

	public List<List<Shift>> printSlots() {
		List<List<Shift>> dayToShiftMapping = new ArrayList<>(); // Maps --- Day --->> shift-slots per day
		for (int i = 0; i < 7; i++) // Loop through List with foreach
		{
			List<Shift> shiftList = new ArrayList<>();
			for (Shift s : wl.result) {
				if (s.day == i) {
					shiftList.add(s);
				}
			}
			dayToShiftMapping.add(shiftList);
		}
		return dayToShiftMapping;
	}

	public double[] calculateUtilization() {
		for (int i = 0; i < wl.sizeOfArray; i++) {
			wl.utilizationArray[i] = (wl.fixedworkloadArray[i]) / (wl.capacityArray[i]);
			System.out.println("UtiLIndex: " + i + " Value :" + wl.utilizationArray[i]);
		}
		return wl.utilizationArray;
	}

	public HourlyDetail[] generateHourlyDetail(Clinician[] clinicians) {
		HourlyDetail[] hourlyDetailList = wl.hourlyDetailList;
		for (int i = 0; i < 168; i++) {
			hourlyDetailList[i].setNumberOfPhysicians(clinicians[0].getClinicianCountPerHour()[i]);
			hourlyDetailList[i].setNumberOfAPPs(clinicians[1].getClinicianCountPerHour()[i]);
			hourlyDetailList[i].setNumberOfScribes(clinicians[2].getClinicianCountPerHour()[i]);
			hourlyDetailList[i].setExpectedWorkLoad(wl.fixedworkloadArray[i]);
			hourlyDetailList[i].setCapacityWorkLoad(wl.capacityArray[i]);
			hourlyDetailList[i].setHour(i);
			hourlyDetailList[i].setUtilization(wl.fixedworkloadArray[i] / wl.capacityArray[i]);
		}
		return hourlyDetailList;
	}

}
