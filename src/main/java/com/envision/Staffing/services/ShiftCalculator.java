package com.envision.Staffing.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.envision.Staffing.model.Clinician;
import com.envision.Staffing.model.HourlyDetail;
import com.envision.Staffing.model.Shift;
import com.envision.Staffing.model.Workload;

import com.envision.Staffing.services.MinCostClinicianCalculator;

public class ShiftCalculator {

	Workload wl;
	MinCostClinicianCalculator minCostClinicianCalculator;

	public void setWorkloads(Workload w) {
		// Initialize workload array
		wl = w;
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
			//Shift newShift = getNewShift(numberOfHours, start, utilization, minCostClinician);
			//flag = checkIfPhysicianToBeAdded(numberOfHours, start, flag, factor, utilization, newShift,
			//		wl.physicianCapacity);
			
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

	public HourlyDetail[] generateHourlyDetail() {
		HourlyDetail[] hourlyDetailList = wl.hourlyDetailList;
		for (int i = 0; i < 168; i++) {
			hourlyDetailList[i].setNumberOfPhysicians(wl.physicianCountperhour[i]);
			hourlyDetailList[i].setNumberOfAPPs(wl.AppCountperhour[i]);
			hourlyDetailList[i].setExpectedWorkLoad(wl.fixedworkloadArray[i]);
			hourlyDetailList[i].setCapacityWorkLoad(wl.capacityArray[i]);
			hourlyDetailList[i].setHour(i);
			hourlyDetailList[i].setUtilization(wl.fixedworkloadArray[i] / wl.capacityArray[i]);
		}
		return hourlyDetailList;
	}

}
