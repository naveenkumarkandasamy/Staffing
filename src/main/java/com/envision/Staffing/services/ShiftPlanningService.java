package com.envision.Staffing.services;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

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

		String path = "DCM_OUTPUT/Shifts.txt";
		String costpath = "DCM_OUTPUT/Cost_Summary.txt";
		String utilPath = "DCM_OUTPUT/Utilization_Summary.txt";
		String finalCorrectedHours = "DCM_OUTPUT/table.txt";
		
		int[] shiftPreferences = new int[]{12, 10, 8, 4};  //shift preference array 

		XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream("Heapmap_export.xlsx"));
		XSSFSheet myExcelSheet = myExcelBook.getSheet("Workload");
	
		Workload work = new Workload();
		if(clinicians[0]!=null && clinicians[0].getPatientsCoveredPerHr()!=null)
			work.docEfficency =clinicians[0].getPatientsCoveredPerHr(); 
		int k = 0;
		
		for (int i = 1; i < 8; i++) {
			for (int j = 8; j < 32; j++) {
				//Reading workload from the excel file
				work.fixedworkloadArray[k] = myExcelSheet.getRow(j).getCell(i).getNumericCellValue() / work.docEfficency;
				work.workloadArray[k] = work.fixedworkloadArray[k] /work.docEfficency;
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
			else
				shiftCalculator.calculate4hourslots(clinicians);
		}
		//shiftCalculator.calculate4hourslots(clinicians);
		
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
			int totalPhysicianHours =0, totalAPPHours =0;
			
			for (Shift s : dayToshiftsmapping.get(i)) {
				sw.write(days[i] + " | " + s.physicianType + " | " + s.start_time + " | " + s.end_time + " | "
						+ s.no_of_hours + "\n");
				if(s.physicianType.equals("Physician")) 
					totalPhysicianHours+=s.no_of_hours;
				else
					totalAPPHours+=s.no_of_hours;
		
			}
			totalHours[i] = totalPhysicianHours+totalAPPHours;
			cost[i] = 320 * totalPhysicianHours+ 200*totalAPPHours;
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
		return hourlyDetailList;
	}
}
