package com.envision.Staffing.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.envision.Staffing.model.Clinician;
import com.envision.Staffing.model.HourlyDetail;
import com.envision.Staffing.model.Shift;
import com.envision.Staffing.model.Workload;

@Service
public class ShiftCalculator {

	Workload wl;

	public void setWorkloads(Workload w) {
		// Initialize workload array
		wl = w;
	}

	public void calculatePhysicianSlotsForAll(int shiftLength, Clinician[] clinicians, double lowerLimitFactor) {
		int start = 0;
		double factor = lowerLimitFactor;
		// Check every 12 hour slot Eg : 0-12 , 1-13, 2-14 ..... (Assuming numberOfHours
		// = 12)
		while (start + shiftLength < wl.getSizeOfArray()) {

			int index = clinicians.length - 1;
			int flag = 1;
			boolean conditionalValue = false;
			boolean[] array = { true, true, true };
			boolean shiftNextHour = true;

			for (; index >= 0; index--) {
				Clinician clinician = getClinicianWithLeastCost(index, clinicians);
				conditionalValue = isConditionStatisfied(clinicians, start, shiftLength, index); // check for
																									// expressions
				do {
					Shift newShift = getNewShift(shiftLength, start, clinician.getName());
					flag = checkIfPhysicianToBeAdded(shiftLength, start, factor, clinician.getCapacity());// checks for
																											// utilization
					if (flag == 1 && conditionalValue) { // add clinician and check with next
						addNewShift(start, shiftLength, newShift, clinician.getCapacity(),
								clinician.getClinicianCountPerHour());
					} else if (flag == 0 && index == clinicians.length - 1) {
						start = start + 1; // since scribe cannot be added, shift to the next hour
						break;
					} else {
						array[index] = false;
					}
					conditionalValue = isConditionStatisfied(clinicians, start, shiftLength, index);

				} while (conditionalValue && flag == 1 && index != 0);
			}

			for (boolean value : array) {
				if (value)
					shiftNextHour = false;
			}

			if (shiftNextHour) {
				shiftNextHour = false;
				start += 1;
			}

		}
	}

	public double evaluate(String expression, Clinician[] clinicians, int hour) {

		String[] elements = expression.split(" ");
		int value = getClinicianCountByName(elements[2], clinicians, hour);
		switch (elements[1]) {
		case "+":
			return Double.parseDouble(elements[0]) + value;
		case "*":
			return Double.parseDouble(elements[0]) * value;
		default:
			return 0;
		}

	}

	// function to return the count per hour of the clinician specified by the name
	public int getClinicianCountByName(String name, Clinician[] clinicians, int hour) {
		if (name != null) {
			Clinician clinician = Arrays.stream(clinicians).filter(p -> name.equalsIgnoreCase(p.getName())).findFirst()
					.get();
			if (clinician != null) {
				return clinician.getClinicianCountPerHour()[hour];
			}

		}
		
		return 0;
	}

	public boolean isConditionStatisfied(Clinician[] clinicians, int start, int shiftLength, int index) {
		// if physician, no need to check for any conditions
		if (clinicians[index].getName() != null) {
			if (clinicians[index].getName().equalsIgnoreCase(clinicians[0].getName()))
				return true;

			else {
				for (int hour = start; hour < start + shiftLength && hour < 168; hour++) {
					double value = 0.0d;
					for (int j = 0; j < clinicians[index].getExpressions().length; j++) {
						value += evaluate(clinicians[index].getExpressions()[j], clinicians, hour);
					}
					if (evaluateFunction(clinicians[index].getClinicianCountPerHour()[hour] + 1, value, ">"))
						// TODO: handle different operators here instead of '<='
						return false;
				}
				return true;
			}
		}
		return false;
	}

	public boolean evaluateFunction(double lefthandValue, double rightHandValue, String operator) {
		switch (operator) {

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

		case "<":
		default:
			return lefthandValue < rightHandValue;

		}
	}

	private int checkIfPhysicianToBeAdded(int numberOfHours, int start, double factor, double[] physicianCapacity) {
		int flag = 1;
		double capacityOfCurrentDoctor = 0;
		for (int j = start; j < start + numberOfHours; j++) {
			double value = wl.getFixedworkloadArray()[j] - wl.getCapacityArray()[j];
			if (value < 0)
				value = 0;

			if ((j - start) % wl.getDayDuration() == 0) {
				capacityOfCurrentDoctor = round(capacityOfCurrentDoctor + min(physicianCapacity[0], value), 2);
			} else if ((j - start) % wl.getDayDuration() == numberOfHours - 1) {
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
		// A four - hour slot is added whenever there are 2 consecutive slots where
		// utilization > given range (110%)
		while (start < wl.getSizeOfArray()) {
			int j = start;

			// checking for utilization < 1.1 to not add clinicians
			if (wl.getFixedworkloadArray()[j] / wl.getCapacityArray()[j] < 1.1) {
				start = start + 1;
			} else {
				boolean conditionalValue = true;
				for (int i = clinicians.length - 1; i >= 0; i--) {
					Clinician clinician = getClinicianWithLeastCost(i, clinicians);
					int tempSlotSize = sizeOfSlot;
					conditionalValue = isConditionStatisfied(clinicians, start, tempSlotSize, i);

					while (conditionalValue) {
						Shift newShift = getNewShift(sizeOfSlot, start, clinician.getName());
						addNewShift(start, sizeOfSlot, newShift, clinician.getCapacity(),
								clinician.getClinicianCountPerHour());
						conditionalValue = isConditionStatisfied(clinicians, start, sizeOfSlot, i);
						if (i == 0 || (wl.getFixedworkloadArray()[j] / wl.getCapacityArray()[j] < 1.1))
							break;
					}

					if (wl.getFixedworkloadArray()[j] / wl.getCapacityArray()[j] < 1.1)
						break;
				}
			}
		}
	}

	public Clinician getClinicianWithLeastCost(int index, Clinician[] clinicians) {
		Clinician[] tempArray = Arrays.copyOf(clinicians, clinicians.length);
		Comparator<Clinician> comparator = Comparator.comparing(Clinician::getCost);
		Arrays.sort(tempArray, comparator);
		return tempArray[clinicians.length - index - 1];
	}

	private void addNewShift(int start, int shiftLength, Shift newShift, double[] capacity, int[] clinicianCounter) {
		// A 12 hour slot can be added
		if (start + shiftLength > wl.getSizeOfArray()) {
			shiftLength = wl.getSizeOfArray() - start;
		}

		calculateNewWorkloads(start, start + shiftLength, capacity);
		calculateCapacities(start, start + shiftLength, capacity);
		for (int x = start; x < start + shiftLength; x++) {
			// Increase the count of the number of physicians used each hour for the 12-hour
			// slot
			clinicianCounter[x]++;
		}

		wl.getHourlyDetailList()[start].incrementNumberOfShiftBeginning();
		wl.getHourlyDetailList()[start + shiftLength - 1].incrementNumberOfShiftEnding();// check for clinicians
																							// extending for next week
		wl.getResult().add(newShift);
	}

	private Shift getNewShift(int numberOfHours, int start, String physicianType) {
		Shift newShift = new Shift();
		newShift.setStartTime(start % wl.getDayDuration());
		newShift.setEndTime((start + numberOfHours) % wl.getDayDuration());
		newShift.setDay(start / wl.getDayDuration());
		newShift.setNoOfHours(numberOfHours);
		newShift.setPhysicianType(physicianType);
		return newShift;
	}

	public void calculateNewWorkloads(int start, int end, double[] capacity) {
		// Reduces the workloads as per the capacities of each physician per hour
		for (int i = start; i < end; i++) {
			if (i == start) {
				wl.getWorkloadArray()[i] = round(wl.getWorkloadArray()[i] - capacity[0], 2);
			} else if (i == end - 1) {
				wl.getWorkloadArray()[i] = round(wl.getWorkloadArray()[i] - capacity[2], 2);

			} else {
				wl.getWorkloadArray()[i] = round(wl.getWorkloadArray()[i] - capacity[1], 2);
			}
		}

	}

	public void calculateCapacities(int start, int end, double[] capacity) {
		for (int i = start; i < end; i++) {
			if (i == start) {
				wl.getCapacityArray()[i] = round(wl.getCapacityArray()[i] + capacity[0], 2);
			}

			else if (i == end - 1) {
				wl.getCapacityArray()[i] = round(wl.getCapacityArray()[i] + capacity[2], 2);

			} else {
				wl.getCapacityArray()[i] = round(wl.getCapacityArray()[i] + capacity[1], 2);
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
			for (Shift s : wl.getResult()) {
				if (s.getDay() == i) {
					shiftList.add(s);
				}
			}
			dayToShiftMapping.add(shiftList);
		}
		return dayToShiftMapping;
	}

	public HourlyDetail[] generateHourlyDetail(Clinician[] clinicians, double docEfficiency) {
		HourlyDetail[] hourlyDetailList = wl.getHourlyDetailList();
		for (int i = 0; i < 168; i++) {
			// Assuming the order of clinicians is physician, app and scribe
			hourlyDetailList[i].setNumberOfPhysicians(clinicians[0].getClinicianCountPerHour()[i]);
			hourlyDetailList[i].setNumberOfAPPs(clinicians[1].getClinicianCountPerHour()[i]);
			hourlyDetailList[i].setNumberOfScribes(clinicians[2].getClinicianCountPerHour()[i]);
			hourlyDetailList[i].setExpectedWorkLoad(wl.getFixedworkloadArray()[i] * docEfficiency);
			hourlyDetailList[i].setCapacityWorkLoad(wl.getCapacityArray()[i] * docEfficiency);
			hourlyDetailList[i].setHour(i);
			hourlyDetailList[i].setUtilization(wl.getFixedworkloadArray()[i] / wl.getCapacityArray()[i]);
			hourlyDetailList[i].setCostPerHour((clinicians[0].getClinicianCountPerHour()[i] * clinicians[0].getCost())
					+ (clinicians[1].getClinicianCountPerHour()[i] * clinicians[1].getCost())
					+ (clinicians[2].getClinicianCountPerHour()[i] * clinicians[2].getCost()));
		}
		return hourlyDetailList;
	}

}
