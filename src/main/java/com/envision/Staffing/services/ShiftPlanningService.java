package com.envision.Staffing.services;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.envision.Staffing.model.Clinician;
import com.envision.Staffing.model.HourlyDetail;
import com.envision.Staffing.model.Shift;
import com.envision.Staffing.model.Workload;

@Service
public class ShiftPlanningService {

	public String[] days = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
			"Saturday" };

	public HourlyDetail[] getShiftPlan(Clinician[] clinicians) throws IOException {

		// Output Files
		String path = "DCM_OUTPUT/Shifts.txt";
		String costpath = "DCM_OUTPUT/Cost_Summary.txt";
		String utilPath = "DCM_OUTPUT/Utilization_Summary.txt";
		String finalCorrectedHours = "DCM_OUTPUT/table.txt";

		int[] shiftPreferences = new int[] { 12, 10, 8, 4 }; // shift preference array

		// reading workload from the excel file
		XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream("Heapmap_export.xlsx"));
		XSSFSheet myExcelSheet = myExcelBook.getSheet("Workload");

		Workload work = new Workload();
		// Checking if atleast one clinician is sent and the PatientsPerHour is not
		// empty, mostly physicians
		// ensure the first clinician is physician
		if (clinicians[0] != null && clinicians[0].getPatientsPerHour() != null)// try to check for physician
			work.docEfficency = clinicians[0].getPatientsPerHour();

		int k = 0;
		for (int i = 1; i < 8; i++) {
			for (int j = 8; j < 32; j++) {
				// Reading workload from the excel file
				work.fixedworkloadArray[k] = myExcelSheet.getRow(j).getCell(i).getNumericCellValue()
						/ work.docEfficency;
				work.workloadArray[k] = work.fixedworkloadArray[k] / work.docEfficency;
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

		/*for (int i : shiftPreferences) {
			shiftCalculator.addClinician(i, clinicians);
		}*/
		for(int i: shiftPreferences) {
		if(i!=4)
				shiftCalculator.calculatePhysicianSlotsForAll(i, clinicians);
			else
				shiftCalculator.calculate4hourslots(clinicians);
		}

		double[] utilizationArray = shiftCalculator.calculateUtilization();
		List<List<Shift>> dayToshiftsmapping = shiftCalculator.printSlots();
		HourlyDetail[] hourlyDetailList = shiftCalculator.generateHourlyDetail(clinicians);

		FileWriter sw = new FileWriter(path, false);

		sw.write("Shifts");
		sw.write("\n------------------------------------------------------------------------\n");
		sw.write("Day | Clinician | Shift Start Time | Shift End Time | Shift Hour Length\n");

		for (int i = 0; i < 7; i++) {
			sw.flush();
			sw.write("------------------------------------------------------------------------\n");
			int totalPhysicianHours = 0, totalAPPHours = 0;

			for (Shift s : dayToshiftsmapping.get(i)) {
				sw.write(days[i] + " | " + s.physicianType + " | " + s.start_time + " | " + s.end_time + " | "
						+ s.no_of_hours + "\n");
				if (s.physicianType.equals("Physician"))
					totalPhysicianHours += s.no_of_hours;
				else
					totalAPPHours += s.no_of_hours;

			}
			totalHours[i] = totalPhysicianHours + totalAPPHours;
			cost[i] = 320 * totalPhysicianHours + 200 * totalAPPHours;
		}
		sw.close();

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
		
		//Calculating costs for each hour
		int hourly_Cost[] = new int[168];
		int dayCost[] = new int[7];
		int weeklyCost=0;
		int dayCostCounter = -1;
		for(int i=0;i<168;i++) {
		
			if(i%24 == 0) {
				if(dayCostCounter!=-1)
					System.out.println("Day Cost "+dayCost[dayCostCounter]);
				dayCostCounter+=1;
				System.out.println("Day "+ dayCostCounter);
				
			}
			hourly_Cost[i] = (clinicians[0].getClinicianCountPerHour()[i]* clinicians[0].getCost()) + 
					         (clinicians[1].getClinicianCountPerHour()[i]* clinicians[1].getCost()) +
					         (clinicians[2].getClinicianCountPerHour()[i]* clinicians[2].getCost());
			System.out.println("Hour "+i + " Cost "+hourly_Cost[i]);
			dayCost[dayCostCounter]+=hourly_Cost[i];
			
		    weeklyCost+=hourly_Cost[i];
		}
		
		System.out.println("Weekly cost " + weeklyCost);
		
		
		
		

		// Calculation of utilization
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
				sw3.write(i + ":00  " + wlCalculated.physicianCountperhour[j] + " " + wlCalculated.fixedworkloadArray[j]
						+ " " + wlCalculated.capacityArray[j] + " \n");
			}
			start = start + 24;
		}
		sw3.close();
		
		//check this
		//ArrayList<Map<Integer,Map<String,Integer>>> arr=new ArrayList<Map<Integer,Map<String,Integer>>>(Collections.nCopies(168, Map<Integer,Map<String,Integer>>));
		ArrayList<Map<Integer,Map<String,Integer>>> clinicianStartEndCount=new ArrayList<Map<Integer,Map<String,Integer>>>(168);
		
		String[] clincian_count_keys = {"physicianStart","physicianEnd","appStart","appEnd","scribeStart","scribeEnd"};
		for(int i=0;i<168;i++) {
			Map<Integer,Map<String,Integer>> slotMap = new HashMap<Integer,Map<String,Integer>>(); 
			for(int slot:shiftPreferences) {
				
				
				Map<String,Integer> clinicianMap = new HashMap<String,Integer>(); 
				for(String clinicianKey : clincian_count_keys) {
					
					clinicianMap.put(clinicianKey,0);
					
					//System.out.println("hour "+i+ " "+slot+ " "+clinicianKey+" "+0 );
				
				}
				slotMap.put(slot,clinicianMap);
				

			}
			
			clinicianStartEndCount.add(slotMap);
			
			
		}
		
		
		List<List<Shift>> dayToshiftsmappingTemp = shiftCalculator.printSlots();
		for (int i = 0; i < 7; i++) {
	
			for (Shift s : dayToshiftsmappingTemp.get(i)) {
//				System.out.println(days[i] + " | " + s.physicianType + " | " + s.start_time + " | " + s.end_time + " | "
//						+ s.no_of_hours+ " | " + ((s.start_time)+(i*24)) + "\n");
				Map<Integer,Map<String,Integer>> slotMapTemp = new HashMap<Integer,Map<String,Integer>>(); 
				Map<String,Integer> clinicianMapTempStart = new HashMap<String,Integer>();
				Map<String,Integer> clinicianMapTempEnd = new HashMap<String,Integer>();
				
				   slotMapTemp = clinicianStartEndCount.get(s.start_time+(i*24));
				   clinicianMapTempStart = slotMapTemp.get(s.no_of_hours);
				  
			
				   
				   clinicianMapTempStart.put(s.physicianType+"Start",clinicianMapTempStart.get(s.physicianType+"Start") + 1);
				   slotMapTemp.put(s.no_of_hours,clinicianMapTempStart);
                   clinicianStartEndCount.set(s.start_time+(i*24),slotMapTemp);
				   
                   
                   //updating end time
				   if(((s.start_time+(i*24))+(s.no_of_hours)) <168) {
					   slotMapTemp = clinicianStartEndCount.get(s.start_time+(i*24) + s.no_of_hours);
					   clinicianMapTempStart = slotMapTemp.get(s.no_of_hours);
					  
				
					   
					   clinicianMapTempStart.put(s.physicianType+"End",clinicianMapTempStart.get(s.physicianType+"End") + 1);
					   slotMapTemp.put(s.no_of_hours,clinicianMapTempStart);
	                   clinicianStartEndCount.set(s.start_time+(i*24)+s.no_of_hours,slotMapTemp);
				
					   
					   
					   }
       	  
			
			}
		}
		
				
//		for(int i=0;i<168;i++) {
//			System.out.println("Hour "+ i);
//			Map<Integer,Map<String,Integer>> slotInfo = clinicianStartEndCount.get(i);
//			 for (Map.Entry<Integer,Map<String, Integer>> entry : slotInfo.entrySet()) {
//			        System.out.println("Slot "+entry.getKey());
//			        Map<String,Integer> value = entry.getValue();
//			        
//			        for(Map.Entry<String,Integer> countIter : value.entrySet()) {
//			        	System.out.println(countIter.getKey()+" "+countIter.getValue());
//			        }
//			    }
//			
//		}
//		
		
		return hourlyDetailList;
	}

}
