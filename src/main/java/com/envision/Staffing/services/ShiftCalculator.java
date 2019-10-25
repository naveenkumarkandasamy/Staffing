package com.envision.Staffing.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.envision.Staffing.model.Clinician;
import com.envision.Staffing.model.HourlyDetail;
import com.envision.Staffing.model.Shift;
import com.envision.Staffing.model.Workload;


public class ShiftCalculator {

	Workload wl;
	MinCostClinicianCalculator minCostClinicianCalculator;

	public void setWorkloads(Workload w) {
		// Initialize workload array
		wl = w;
		 minCostClinicianCalculator = new MinCostClinicianCalculator();
	}
   
	
	public void calculatePhysicianSlotsForAll(int shiftLength, Clinician[] clinicians) {
		int start = 0;
		double factor = 0.75;

		// Check every 12 hour slot Eg : 0-12 , 1-13, 2-14 ..... (Assuming numberOfHours
		// = 12)
		while (start + shiftLength <= wl.sizeOfArray) {
			
			int index = clinicians.length - 1;
			int flag = 1;
			boolean conditionalValue = false;
			boolean array[] = { true, true, true };
			boolean shiftNextHour = true;

			for (; index >= 0; index--) {
				Clinician clinician = getClinicianWithLeastCost(index, clinicians);
				conditionalValue = isConditionStatisfied(clinicians, start, shiftLength, index); //check for expressions
				flag = 1;
				do {
					Shift newShift = getNewShift(shiftLength, start, clinician.getName());
					flag = checkIfPhysicianToBeAdded(shiftLength, start, factor, newShift,
							clinician.getCapacity());//checks for utilization
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
			
			if(shiftNextHour == true) {
				shiftNextHour = false;
				start += 1;
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
		//if physician, no need to check for any conditions
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
	while (start < wl.sizeOfArray) {
			
			int j = start;
			flag =false;	
			//checking for utilization < 1.1 to not add clinicians
			if(wl.fixedworkloadArray[j] / wl.capacityArray[j] < 1.1 ) {
				flag = true;
				start = start + 1;
			} else {
				boolean conditionalValue = true;
				for (int i = clinicians.length -1; i >=0; i--) {
					Clinician clinician = getClinicianWithLeastCost(i, clinicians);
				    int tempSlotSize = sizeOfSlot;
					
					   conditionalValue = isConditionStatisfied(clinicians, start, tempSlotSize, i);
					while (conditionalValue) {
						Shift newShift = getNewShift(sizeOfSlot, start, clinician.getName());
						addNewShift(start, sizeOfSlot, newShift, clinician.getCapacity(),
								clinician.getClinicianCountPerHour());
         				conditionalValue = isConditionStatisfied(clinicians, start,sizeOfSlot , i); 
						if(i==0 || (wl.fixedworkloadArray[j] / wl.capacityArray[j] < 1.1)  ) break;
					}

					if(wl.fixedworkloadArray[j] / wl.capacityArray[j] < 1.1) break;
				}
				flag =false;
			}
	
		}
		
	}

	private Clinician getClinicianWithLeastCost(int index, Clinician[] clinicians) {
		// TODO return clinician with ith least cost
		return clinicians[index];
	}

	private void addNewShift(int start, int shiftLength, Shift newShift, double[] capacity, int[] clinicianCounter) {
		int tempShiftLength = shiftLength;
		// A 12 hour slot can be added
		if(start+shiftLength > wl.sizeOfArray) {
			shiftLength = wl.sizeOfArray - start;
		}
			
		calculateNewWorkloads(start, start + shiftLength, capacity);
		calculateCapacities(start, start + shiftLength, capacity);
		for (int x = start; x < start + shiftLength; x++) {
			// Increase the count of the number of physicians used each hour for the 12-hour
			// slot
			clinicianCounter[x]++;
		}
		
		
		wl.hourlyDetailList[start].incrementNumberOfShiftBeginning();
		wl.hourlyDetailList[start + shiftLength - 1].incrementNumberOfShiftEnding();//check for clinicians extending for next week
		
		shiftLength = tempShiftLength;
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

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void addClinician(int numberOfHours, Clinician[] clinician) { 
		
		int start = 0;
		int flag = 1;
		double factor = 0.85; 
		double[] utilization;
		int clinicianTypes = 3; //denoting physician,app and scribe
		// Check every 12 hour slot Eg : 0-12 , 1-13, 2-14 ..... (Assuming numberOfHours
		// = 12) ie., checks every slot
		
		
		
		while(start + numberOfHours < wl.sizeOfArray) {
			flag = 1;
			utilization = new double[numberOfHours];
			Clinician minCostClinician;
			
			for(int i=1;i<=clinicianTypes;i++) { //check for physician, app and scribe
			
			minCostClinician = minCostClinicianCalculator.getMinCostClinician(clinician,i);
			
			}
			
		}
		
	}
	public void calculatePhysicianSlots(int numberOfHours) {
		int start = 0;
		int flag = 1;
		double factor = 0.85;
		double[] utilization;

		// Check every 12 hour slot Eg : 0-12 , 1-13, 2-14 ..... (Assuming numberOfHours
		// = 12)
		while (start + numberOfHours < wl.sizeOfArray) {
			flag = 1;
			utilization = new double[numberOfHours];
			Shift newShift = getNewShift(numberOfHours, start, utilization, "physician");
			flag = checkIfPhysicianToBeAdded(numberOfHours, start, flag, factor, utilization, newShift,
					wl.physicianCapacity);
			if (flag == 1) {
				// A 12 hour slot can be added
				calculateNewWorkloads(start, start + numberOfHours, wl.physicianCapacity);
				calculateCapacities(start, start + numberOfHours, wl.physicianCapacity);
				for (int x = start; x < start + numberOfHours; x++) {
					// Increase the count of the number of physicians used each hour for the 12-hour
					// slot
					Map<Shift, Double> shiftUtilization = wl.hourToShiftMap.get(x);
					if (shiftUtilization == null)
						shiftUtilization = new HashMap<>();
					shiftUtilization.put(newShift, newShift.utilization[x - start]);
					wl.hourToShiftMap.put(x, shiftUtilization);
					wl.physicianCountperhour[x]++;
				}
				wl.hourlyDetailList[start].incrementNumberOfShiftBeginning();
				wl.hourlyDetailList[start + numberOfHours - 1].getNumberOfShiftEnding();
				wl.result.add(newShift);
				for (int i = 0; i < 2; i++)
					this.addAPPs(start, numberOfHours, factor);
			} else {
				// Check the next 12- hour slot
				start = start + 1;
			}
		}
	}

	private void addAPPs(int start, int numberOfHours, double factor) {
		int flag = 1;
		double[] utilization = new double[numberOfHours];
		Shift newShift = getNewShift(numberOfHours, start, utilization, "APP");
		flag = checkIfPhysicianToBeAdded(numberOfHours, start, flag, factor, utilization, newShift, wl.appCapacity);
		if (flag == 1) {
			// A 12 hour slot can be added
			calculateNewWorkloads(start, start + numberOfHours, wl.appCapacity);
			calculateCapacities(start, start + numberOfHours, wl.appCapacity);
			for (int x = start; x < start + numberOfHours; x++) {
				// Increase the count of the number of physicians used each hour for the 12-hour
				// slot
				Map<Shift, Double> shiftUtilization = wl.hourToShiftMap.get(x);
				if (shiftUtilization == null)
					shiftUtilization = new HashMap<>();
				shiftUtilization.put(newShift, newShift.utilization[x - start]);
				wl.hourToShiftMap.put(x, shiftUtilization);
				wl.AppCountperhour[x]++;
			}
			wl.hourlyDetailList[start].incrementNumberOfShiftBeginning();
			wl.hourlyDetailList[start + numberOfHours - 1].getNumberOfShiftEnding();
			wl.result.add(newShift);
		}
	}

	private Shift getNewShift(int numberOfHours, int start, double[] utilization, String physicianType) {
		Shift newShift = new Shift();
		newShift.start_time = start % wl.dayDuration;
		newShift.end_time = (start + numberOfHours) % wl.dayDuration;
		newShift.day = start / wl.dayDuration;
		newShift.no_of_hours = numberOfHours;
		newShift.utilization = utilization;
		newShift.setPhysicianType(physicianType);
		return newShift;
	}

	private int checkIfPhysicianToBeAdded(int numberOfHours, int start, int flag, double factor, double[] utilization,
			Shift newShift, double[] physicianCapacity) {
		double capacityOfCurrentDoctor = 0;
		for (int j = start; j < start + numberOfHours; j++) {
			Map<Shift, Double> hourShiftMap = wl.hourToShiftMap.get(j);
			if (hourShiftMap == null)
				hourShiftMap = new HashMap<>();
			// Checks the total work a physician will be able to complete in a 12-hour
			// slot
			if (wl.physicianCountperhour[j] != 0 && ((wl.physicianCountperhour[j] - wl.physicianStretchingPerHour[j])
					* 0.1 >= round(wl.fixedworkloadArray[j] - wl.capacityArray[j], 2))) {
				while( round(wl.fixedworkloadArray[j] - wl.capacityArray[j], 2)>0.1) {
					double minimumUtilization = min(round(wl.fixedworkloadArray[j] - wl.capacityArray[j], 2), 0.1);
					wl.capacityArray[j] += minimumUtilization;
					wl.physicianStretchingPerHour[j]++;
				}
				
				
				/*Map<Shift, Double> previousHourMap = wl.hourToShiftMap.get(j - 1);
				for (Map.Entry<Shift, Double> entry : hourShiftMap.entrySet()) {
					double minimumUtilization = min(round(wl.fixedworkloadArray[j] - wl.capacityArray[j], 2), 0.1);
					Shift shift = entry.getKey();
					if ((j == 0 || (previousHourMap.get(shift) == null)
							|| (previousHourMap.get(shift) <= 1.0 && entry.getValue() <= 1.0))
							&& minimumUtilization > 0) {
						entry.setValue(entry.getValue() + minimumUtilization);
						wl.capacityArray[j] += minimumUtilization;
						wl.physicianStretchingPerHour[j]++;
						shift.utilization[j - (shift.day * 24 + shift.start_time)] += minimumUtilization;
					}
				}*/
			} else {
				if ((j - start) % wl.dayDuration == 0) {
					capacityOfCurrentDoctor = round(capacityOfCurrentDoctor + physicianCapacity[0], 2);
					utilization[j - start] = round(
							min(wl.fixedworkloadArray[j] - wl.capacityArray[j], physicianCapacity[0])
									/ wl.firstHourCapacity,
							2);
				} else if ((j - start) % wl.dayDuration == numberOfHours - 1) {
					capacityOfCurrentDoctor = round(capacityOfCurrentDoctor + physicianCapacity[2], 2);
					utilization[j - start] = round(
							min(wl.fixedworkloadArray[j] - wl.capacityArray[j], physicianCapacity[2])
									/ wl.lastHourCapacity,
							2);
				} else {
					capacityOfCurrentDoctor = round(capacityOfCurrentDoctor + physicianCapacity[1], 2);
					utilization[j - start] = round(
							min(wl.fixedworkloadArray[j] - wl.capacityArray[j], physicianCapacity[1])
									/ wl.midHourCapacity,
							2);
				}
			}

			// Checks if adding a physician is needed
			if ((j - start) % wl.dayDuration == (numberOfHours - 1)
					&& ((capacityOfCurrentDoctor / (numberOfHours * physicianCapacity[1])) < factor)) {
				flag = 0;
				hourShiftMap.remove(newShift);
				break;
			}
		}
		return flag;
	}

	public void calculate4hourslots() {
		int start = 0;
		boolean flag = false;
		int sizeOfSlot = 4;

		// A four - hour slot is added whenever there are 2 consecutive slots where
		// utilization > given range (110%)
		while (start <= wl.sizeOfArray - sizeOfSlot) {
			flag = false;
			int j = start;
			if (wl.physicianCountperhour[j] != 0 && (round(wl.fixedworkloadArray[j] - wl.capacityArray[j], 2))
					/ wl.physicianCountperhour[j] <= 0.1) {
				flag = true;
			}
			if (flag) {
				start = start + 1;
			} else {
				if ((start + sizeOfSlot) > wl.sizeOfArray - 1) {
					calculateNewWorkloads(start, wl.sizeOfArray, wl.physicianCapacity);
					calculateCapacities(start, wl.sizeOfArray, wl.physicianCapacity);
				} else {
					calculateNewWorkloads(start, start + sizeOfSlot, wl.physicianCapacity);
					calculateCapacities(start, start + sizeOfSlot, wl.physicianCapacity);
				}
				for (int x = start; x < start + sizeOfSlot; x++) {
					wl.physicianCountperhour[x]++;
				}

				Shift shift = getNewShift(sizeOfSlot, start, new double[sizeOfSlot], "physician");
				wl.hourlyDetailList[start].incrementNumberOfShiftBeginning();
				wl.hourlyDetailList[start + sizeOfSlot - 1].getNumberOfShiftEnding();
				wl.result.add(shift); // Add shift in the list
			}
		}
	}

	public void calculateNewWorkloads(int start, int end, double[] capacity) {
		// Reduces the workloads as per the capacities of each physician per hour
		for (int i = start; i < end; i++) {
			if (i == start) {
				wl.workloadArray[i] = round(wl.workloadArray[i] - capacity[0], 2);
			}
			else if (i == end - 1) {
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
			}

			else if (i == end - 1) {
				wl.capacityArray[i] = round(wl.capacityArray[i] + capacity[2], 2);

			}
			else {
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

	public void printWorkloadArray() {
		for (int i = 0; i < wl.sizeOfArray; i++) {
			System.out.println("WorkloadIndex: " + i + " Value :" + wl.workloadArray[i]);
		}
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
//			hourlyDetailList[i].setNumberOfPhysicians(wl.physicianCountperhour[i]);
//			hourlyDetailList[i].setNumberOfAPPs(wl.AppCountperhour[i]);
//			hourlyDetailList[i].setNumberOfScribes(wl.scribeCountperhour[i]);
			
			
			//Assuming the order of clinicians is physician, app and scribe
			hourlyDetailList[i].setNumberOfPhysicians(clinicians[0].getClinicianCountPerHour()[i]);
			hourlyDetailList[i].setNumberOfAPPs(clinicians[1].getClinicianCountPerHour()[i]);
			hourlyDetailList[i].setNumberOfScribes(clinicians[2].getClinicianCountPerHour()[i]);
			
			hourlyDetailList[i].setExpectedWorkLoad(wl.fixedworkloadArray[i]);
			hourlyDetailList[i].setCapacityWorkLoad(wl.capacityArray[i]);
			hourlyDetailList[i].setHour(i);
			hourlyDetailList[i].setUtilization(wl.fixedworkloadArray[i] / wl.capacityArray[i]);
			
			hourlyDetailList[i].setCostPerHour((clinicians[0].getClinicianCountPerHour()[i]* clinicians[0].getCost()) + 
			                                   (clinicians[1].getClinicianCountPerHour()[i]* clinicians[1].getCost()) +
			                                   (clinicians[2].getClinicianCountPerHour()[i]* clinicians[2].getCost()));
			
			
		}
		return hourlyDetailList;
	}

}
