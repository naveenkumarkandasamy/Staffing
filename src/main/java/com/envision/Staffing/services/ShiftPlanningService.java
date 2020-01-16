package com.envision.Staffing.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.envision.Staffing.model.Clinician;
import com.envision.Staffing.model.Day;
import com.envision.Staffing.model.HourlyDetail;
import com.envision.Staffing.model.Input;
import com.envision.Staffing.model.Output;
import com.envision.Staffing.model.Shift;
import com.envision.Staffing.model.Workload;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ShiftPlanningService {

	private String[] days = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
			"Saturday" };

	// function to process form-data containing json object and the workload as an
	// .xlsx file
	// return the input object
	public Input processFileInput(MultipartFile excelFile, String inputData) throws IOException {

		Input input = new ObjectMapper().readValue(inputData, Input.class);

		InputStream excelInput = excelFile.getInputStream();

		XSSFWorkbook myExcelBook = new XSSFWorkbook(excelInput);
		XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
		Day[] workload = new Day[7];

		for (int i = 0; i <= 6; i++) {

			workload[i] = new Day();

			// setting days name as saturday or monday etc

			workload[i].setName(days[i]);
			Double[] personPerHour = new Double[24];
			for (int j = 0; j <= 23; j++) {
				personPerHour[j] = Double.valueOf(myExcelSheet.getRow(i).getCell(j).getNumericCellValue());

			}

			workload[i].setExpectedPatientsPerHour(personPerHour);
		}
		input.setDayWorkload(workload);
		myExcelBook.close();
		return input;
	}

	public Output getShiftPlan(Input input) throws IOException {

		int[] shiftPreferences = new int[] { 12, 10, 8, 4 };
		double lowerLimitFactor = 0.75;

		Clinician[] clinicians = input.getClinician();

		if (input.getShiftLength() != null) {
			shiftPreferences = input.getShiftLength();
		}
		if (input.getLowerLimitFactor() != null) {
			lowerLimitFactor = input.getLowerLimitFactor();
		}

		Workload work = new Workload();
		// Checking if at least one clinician is sent and the PatientsPerHour is not
		// empty, mostly physicians
		// ensure the first clinician is physician
		if (clinicians[0] != null && clinicians[0].getPatientsPerHour() != null)// try to check for physician
			work.setDocEfficency(clinicians[0].getPatientsPerHour());

		if (input.getDayWorkload() != null) {
			work = assignWorkload(input, work);
		}

		for (double w : work.getFixedworkloadArray()) {
			System.out.println(w + " ");
		}

		for (double we : work.getWorkloadArray()) {
			System.out.println(we + " ");
		}

		for (int i = 0; i < clinicians.length; i++) {
			clinicians[i].setClinicianCountPerHour(new int[168]);
		}

		ShiftCalculator shiftCalculator = new ShiftCalculator();
		shiftCalculator.setWorkloads(work);

		for (int i = 0; i < shiftPreferences.length; i++) {
			if (i != (shiftPreferences.length - 1))
				shiftCalculator.calculatePhysicianSlotsForAll(shiftPreferences[i], clinicians, lowerLimitFactor);
			else
				shiftCalculator.calculate4hourslots(clinicians, shiftPreferences[i]);
		}

		HourlyDetail[] hourlyDetailList = shiftCalculator.generateHourlyDetail(clinicians, work.getDocEfficency());

		// calculating the count of clinicians starting and ending at each hour

		ArrayList<Map<Integer, Map<String, Integer>>> clinicianStartEndCount = new ArrayList<>(168);

		String[] clincianCountKeys = new String[2 * clinicians.length];
		for (int i = 0; i < clinicians.length; i++) {
			clincianCountKeys[2 * i] = clinicians[i].getName() + "Start";
			clincianCountKeys[2 * i + 1] = clinicians[i].getName() + "End";
		}

		for (int i = 0; i < 168; i++) {
			Map<Integer, Map<String, Integer>> slotMap = new HashMap<>();
			for (int slot : shiftPreferences) {

				Map<String, Integer> clinicianMap = new HashMap<>();
				for (String clinicianKey : clincianCountKeys) {

					clinicianMap.put(clinicianKey, 0);

				}
				slotMap.put(slot, clinicianMap);

			}

			clinicianStartEndCount.add(slotMap);

		}

		List<List<Shift>> dayToshiftsmappingTemp = shiftCalculator.printSlots();
		for (int i = 0; i < 7; i++) {

			for (Shift s : dayToshiftsmappingTemp.get(i)) {
				Map<Integer, Map<String, Integer>> slotMapTemp = clinicianStartEndCount
						.get(s.getStartTime() + (i * 24));
				Map<String, Integer> clinicianMapTempStart = slotMapTemp.get(s.getNoOfHours());

				clinicianMapTempStart.put(s.getPhysicianType() + "Start",
						clinicianMapTempStart.get(s.getPhysicianType() + "Start") + 1);
				slotMapTemp.put(s.getNoOfHours(), clinicianMapTempStart);
				clinicianStartEndCount.set(s.getStartTime() + (i * 24), slotMapTemp);

				// updating end time
				if (((s.getStartTime() + (i * 24)) + (s.getNoOfHours())) < 168) {
					slotMapTemp = clinicianStartEndCount.get(s.getStartTime() + (i * 24) + s.getNoOfHours());
					clinicianMapTempStart = slotMapTemp.get(s.getNoOfHours());

					clinicianMapTempStart.put(s.getPhysicianType() + "End",
							clinicianMapTempStart.get(s.getPhysicianType() + "End") + 1);
					slotMapTemp.put(s.getNoOfHours(), clinicianMapTempStart);
					clinicianStartEndCount.set(s.getStartTime() + (i * 24) + s.getNoOfHours(), slotMapTemp);

				}

			}
		}

		Output out = new Output();
		out.setHourlyDetail(hourlyDetailList);
		out.setClinicianHourCount(clinicianStartEndCount);

		return out;
	}

	private Workload assignWorkload(Input input, Workload work) {
		Day[] day;
		int k = 0;

		if (input.getDayWorkload() != null) {
			day = input.getDayWorkload();
			for (Day eachDay : day) {
				for (Double patientsPerHour : eachDay.getExpectedPatientsPerHour()) {
					work.getFixedworkloadArray()[k] = patientsPerHour / work.getDocEfficency();
					work.getWorkloadArray()[k] = work.getFixedworkloadArray()[k];
					k++;
				}
			}

		}

		return work;
	}

}
