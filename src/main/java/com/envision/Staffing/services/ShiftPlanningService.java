package com.envision.Staffing.services;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.envision.Staffing.model.Clinician;
import com.envision.Staffing.model.HourlyDetail;
import com.envision.Staffing.model.Input;
import com.envision.Staffing.model.Output;
import com.envision.Staffing.model.Shift;
import com.envision.Staffing.model.Workload;

@Service
public class ShiftPlanningService {

	private String[] days = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday","Saturday" };
	// Output Files
	private	String path = "DCM_OUTPUT/Shifts.txt";
	private	String costpath = "DCM_OUTPUT/Cost_Summary.txt";
	private	String utilPath = "DCM_OUTPUT/Utilization_Summary.txt";
	private	String finalCorrectedHours = "DCM_OUTPUT/table.txt";
	private int[] shiftPreferences = new int[]{12,10,8,4};

//	public Output getShiftPlan(Input input) throws IOException {
	public Output getShiftPlan(Clinician[] clinicians) throws IOException {
//		Clinician[] clinicians = input.getClinician();
//		int[] shiftPreferences = input.getShiftLength();
//		double lowerLimitFactor = input.getLowerLimitFactor(); 
		
		
		
		// reading workload from the excel file
		XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream("Heapmap_export.xlsx"));
		XSSFSheet myExcelSheet = myExcelBook.getSheet("Workload");

		Workload work = new Workload();
		// Checking if atleast one clinician is sent and the PatientsPerHour is not
		// empty, mostly physicians
		// ensure the first clinician is physician
		if (clinicians[0] != null && clinicians[0].getPatientsPerHour() != null)// try to check for physician
			work.setDocEfficency(clinicians[0].getPatientsPerHour());

		int k = 0;
		for (int i = 1; i < 8; i++) {
			for (int j = 8; j < 32; j++) {
				// Reading workload from the excel file
				work.getFixedworkloadArray()[k] = myExcelSheet.getRow(j).getCell(i).getNumericCellValue()
						/ work.getDocEfficency();
				work.getWorkloadArray()[k] = work.getFixedworkloadArray()[k] / work.getDocEfficency();
				k++;
			}
		}
		myExcelBook.close();

		int[] totalHours = { 0, 0, 0, 0, 0, 0, 0 };
		int[] cost = { 0, 0, 0, 0, 0, 0, 0 };

		for(int i=0;i<clinicians.length;i++) {
			clinicians[i].setClinicianCountPerHour(new int[168]);
		}
		
		ShiftCalculator shiftCalculator = new ShiftCalculator();
		shiftCalculator.setWorkloads(work);

		
		for(int i: shiftPreferences) {
		if(i!=4)
			shiftCalculator.calculatePhysicianSlotsForAll(i, clinicians);
//				shiftCalculator.calculatePhysicianSlotsForAll(i, clinicians, lowerLimitFactor);
			else
				shiftCalculator.calculate4hourslots(clinicians);
		}

		double[] utilizationArray = shiftCalculator.calculateUtilization();
		List<List<Shift>> dayToshiftsmapping = shiftCalculator.printSlots();
		HourlyDetail[] hourlyDetailList = shiftCalculator.generateHourlyDetail(clinicians , work.getDocEfficency());

		printShifts(totalHours, cost, dayToshiftsmapping);

		printCostSummary(totalHours, cost);
		
		//Calculating costs for each hour
		calculateHourlyCost(clinicians);
		
		
		
		

		// Calculation of utilization
		
		printUtilizationSummary(shiftCalculator, utilizationArray);

		printUtilzationTable(shiftCalculator);
		
		//calculating the count of clinicians starting and ending at each hour
		
		ArrayList<Map<Integer,Map<String,Integer>>> clinicianStartEndCount=new ArrayList<>(168);
		
		String[] clincianCountKeys = {"physicianStart","physicianEnd","appStart","appEnd","scribeStart","scribeEnd"};
		for(int i=0;i<168;i++) {
			Map<Integer,Map<String,Integer>> slotMap = new HashMap<>(); 
			for(int slot:shiftPreferences) {
				
				
				Map<String,Integer> clinicianMap = new HashMap<>(); 
				for(String clinicianKey : clincianCountKeys) {
					
					clinicianMap.put(clinicianKey,0);
				
				}
				slotMap.put(slot,clinicianMap);
				

			}
			
			clinicianStartEndCount.add(slotMap);
			
			
		}
		
		
		List<List<Shift>> dayToshiftsmappingTemp = shiftCalculator.printSlots();
		for (int i = 0; i < 7; i++) {
	
			for (Shift s : dayToshiftsmappingTemp.get(i)) {
				Map<Integer,Map<String,Integer>> slotMapTemp = clinicianStartEndCount.get(s.getStartTime()+(i*24));
				   Map<String,Integer> clinicianMapTempStart = slotMapTemp.get(s.getNoOfHours());
				  
			
				   
				   clinicianMapTempStart.put(s.getPhysicianType()+"Start",clinicianMapTempStart.get(s.getPhysicianType()+"Start") + 1);
				   slotMapTemp.put(s.getNoOfHours(),clinicianMapTempStart);
                   clinicianStartEndCount.set(s.getStartTime()+(i*24),slotMapTemp);
				   
                   
                   //updating end time
				   if(((s.getStartTime()+(i*24))+(s.getNoOfHours())) <168) {
					   slotMapTemp = clinicianStartEndCount.get(s.getStartTime()+(i*24) + s.getNoOfHours());
					   clinicianMapTempStart = slotMapTemp.get(s.getNoOfHours());
					  
				
					   
					   clinicianMapTempStart.put(s.getPhysicianType()+"End",clinicianMapTempStart.get(s.getPhysicianType()+"End") + 1);
					   slotMapTemp.put(s.getNoOfHours(),clinicianMapTempStart);
	                   clinicianStartEndCount.set(s.getStartTime()+(i*24)+s.getNoOfHours(),slotMapTemp);
				
					   
					   
					   }
       	  
			
			}
		}
		
		
		Output out = new Output();
		out.setHourlyDetail(hourlyDetailList);
		out.setClinicianHourCount(clinicianStartEndCount);
		
		return out;
	}

	private void printUtilzationTable(ShiftCalculator shiftCalculator) throws IOException {
		int start;
		Workload wlCalculated = shiftCalculator.wl;
		FileWriter sw3 = new FileWriter(finalCorrectedHours);
		sw3.flush();
		sw3.write("\n");
		sw3.write("-------------------------------------------------\n");
		sw3.write("\n");
		start = 0;
		for (int x = 0; x < 7; x++) {
			sw3.write(days[x] + "\n");
			sw3.write("----------------------------------------\n");
			for (int j = start, i = 0; i < 24 && j < start + 24; j++, i++) {
				sw3.write(i + ":00  " + wlCalculated.getPhysicianCountperhour()[j] + " " + wlCalculated.getFixedworkloadArray()[j]
						+ " " + wlCalculated.getCapacityArray()[j] + " \n");
			}
			start = start + 24;
		}
		sw3.close();
	}

	private void printUtilizationSummary(ShiftCalculator shiftCalculator, double[] utilizationArray)
			throws IOException {
		FileWriter sw2 = new FileWriter(utilPath);
		sw2.flush();
		sw2.write("Utilization Summary");
		sw2.write("\n-------------------------------------------------\n");
		sw2.write("Acceptable utilization level -> 75% <= Utilization <= 110%\n");
		sw2.write("\n----------------------------------------\n");
		sw2.write("Day | Hour | Utilization per Hour (in %)\n");
		int start = 0;
		int z;
		for (int x = 0; x < 7; x++) {
			sw2.write("----------------------------------------\n");
			for (z = start; z < start + 24; z++) {
				int end = z + 1;
				double util = shiftCalculator.round(utilizationArray[z] * 100, 2);
				sw2.write(days[x] + " | " + (z % 24) + ".00 - " + (end % 24) + ".00 | " + util + "\n");
			}
			start = start + 24;
		}
		sw2.close();
	}

	private void calculateHourlyCost(Clinician[] clinicians) {
		int[] hourlyCost = new int[168];
		int[] dayCost = new int[7];
		int weeklyCost=0;
		int dayCostCounter = -1;
		for(int i=0;i<168;i++) {
		
			if(i%24 == 0) {
				if(dayCostCounter!=-1)
					System.out.println("Day Cost "+dayCost[dayCostCounter]);
				dayCostCounter+=1;
				System.out.println("Day "+ dayCostCounter);
				
			}
			hourlyCost[i] = (clinicians[0].getClinicianCountPerHour()[i]* clinicians[0].getCost()) + 
					         (clinicians[1].getClinicianCountPerHour()[i]* clinicians[1].getCost()) +
					         (clinicians[2].getClinicianCountPerHour()[i]* clinicians[2].getCost());
			System.out.println("Hour "+i + " Cost "+hourlyCost[i]);
			dayCost[dayCostCounter]+=hourlyCost[i];
			
		    weeklyCost+=hourlyCost[i];
		}
		
		System.out.println("Weekly cost " + weeklyCost);
	}

	private void printCostSummary(int[] totalHours, int[] cost) throws IOException {
		// Calculation of Costs
		FileWriter sw1 = new FileWriter(costpath, false);
		sw1.flush();

		sw1.write("Cost Summary");
		sw1.write("\n-------------------------------------------------\n");
		sw1.write("Day | Clinician | Total Hours | Total Cost (in $)\n");
		sw1.write("-------------------------------------------------\n");

		for (int x = 0; x < 7; x++) {
			sw1.write(days[x] + " | Physician | " + totalHours[x] + " | " + cost[x] + "\n");
		}
		sw1.close();
	}

	private void printShifts(int[] totalHours, int[] cost, List<List<Shift>> dayToshiftsmapping) throws IOException {
		FileWriter sw = new FileWriter(path, false);

		sw.write("Shifts");
		sw.write("\n------------------------------------------------------------------------\n");
		sw.write("Day | Clinician | Shift Start Time | Shift End Time | Shift Hour Length\n");

		for (int i = 0; i < 7; i++) {
			sw.flush();
			sw.write("------------------------------------------------------------------------\n");
			int totalPhysicianHours = 0;
			int totalAPPHours = 0;

			for (Shift s : dayToshiftsmapping.get(i)) {
				sw.write(days[i] + " | " + s.getPhysicianType() + " | " + s.getStartTime() + " | " + s.getEndTime() + " | "
						+ s.getNoOfHours() + "\n");
				if (s.getPhysicianType().equals("Physician"))
					totalPhysicianHours += s.getNoOfHours();
				else
					totalAPPHours += s.getNoOfHours();

			}
			totalHours[i] = totalPhysicianHours + totalAPPHours;
			cost[i] = 320 * totalPhysicianHours + 200 * totalAPPHours;
		}
		sw.close();
	}

}
