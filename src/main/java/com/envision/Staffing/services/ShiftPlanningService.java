package com.envision.Staffing.services;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.envision.Staffing.model.Clinician;
import com.envision.Staffing.model.Day;
import com.envision.Staffing.model.DayShift;
import com.envision.Staffing.model.HourlyDetail;
import com.envision.Staffing.model.Input;
import com.envision.Staffing.model.JobDetails;
import com.envision.Staffing.model.Output;
import com.envision.Staffing.model.Shift;
import com.envision.Staffing.model.Workload;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

@Service
public class ShiftPlanningService {

	private String[] days = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
			"Saturday" };

	public Input processFtpInput(InputStream ftpInputStream, JobDetails jobDetails) throws Exception {

		Input input = new Input();

		input.setClinician(jobDetails.getClinicians().stream().toArray(Clinician[]::new));
		input.setLowerLimitFactor(jobDetails.getLowerUtilizationFactor());
		input.setShiftLength(jobDetails.getShiftLengthPreferences());
		input.setDayWorkload(getDataFromExcelFile(ftpInputStream));
		return input;
	}

	// function to process form-data containing json object and the workload as an
	// .xlsx file
	// return the input object
	public Input processFileInput(MultipartFile excelFile, String inputData) throws Exception {

		Input input = new ObjectMapper().readValue(inputData, Input.class);
		InputStream excelInput = excelFile.getInputStream();
		input.setDayWorkload(getDataFromExcelFile(excelInput));
		return input;
	}

	public Day[] getDataFromExcelFile(InputStream excelInputStream) throws Exception {

		XSSFWorkbook myExcelBook;
		Day[] workload = new Day[7];
		try {
			myExcelBook = new XSSFWorkbook(excelInputStream);
			XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);

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
			myExcelBook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException illegalStateException) {
			throw new Exception(illegalStateException.toString());
		} catch (NumberFormatException numberFormatException) {
			throw new Exception(numberFormatException.toString());
		}

		return workload;
	}

	public Output getShiftPlan(Input input) throws IOException {

		Integer[] shiftPreferences = new Integer[] { 12, 10, 8, 4 };
		double lowerLimitFactor = 0.75;
		double upperLimitFactor = 1.1;
		Integer notAllocatedStartTime = 1;
		Integer notAllocatedEndTime = 6;
		Integer patientHourWait = 3;

		Clinician[] clinicians = input.getClinician();
		if (input.getShiftLength() != null) {
			shiftPreferences = input.getShiftLength();
		}

		if (input.getLowerLimitFactor() != null) {
			lowerLimitFactor = input.getLowerLimitFactor();
		}

		if (input.getUpperLimitFactor() != null) {
			upperLimitFactor = input.getUpperLimitFactor();
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

		for (int i = 0; i < clinicians.length; i++) {
			clinicians[i].setClinicianCountPerHour(new int[168]);
		}

		ShiftCalculator shiftCalculator = new ShiftCalculator();
		shiftCalculator.setWorkloads(work);

		// checking which clinician is always true and store index in arrindex for this
		// clinician

		for (int i = 0; i < shiftPreferences.length; i++) {
			if (i != (shiftPreferences.length - 1)) {
				shiftCalculator.calculatePhysicianSlotsForAll(notAllocatedStartTime, notAllocatedEndTime,
						shiftPreferences[i], clinicians, lowerLimitFactor);
			} else
				shiftCalculator.calculateLastHourSlots(upperLimitFactor, notAllocatedStartTime, notAllocatedEndTime,
						clinicians, shiftPreferences[i]);
		}

		HourlyDetail[] hourlyDetailList = shiftCalculator.generateHourlyDetail(patientHourWait, clinicians,
				work.getDocEfficency(), lowerLimitFactor);

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
		System.out.println(out.toString());
		return out;
	}

	private Workload assignWorkload(Input input, Workload work) {
		Day[] day;
		int k = 0;

		if (input.getDayWorkload() != null) {
			day = input.getDayWorkload();
			for (Day eachDay : day) {
				for (Double patientsPerHour : eachDay.getExpectedPatientsPerHour()) {
					work.getFixedworkloadArray()[k] = patientsPerHour;
					work.getWorkloadArray()[k] = work.getFixedworkloadArray()[k];
					k++;
				}
			}

		}
		return work;
	}

	public ByteArrayOutputStream excelWriter(Output output, JobDetails jobDetails) throws IOException {

		Workbook workbook = new XSSFWorkbook();
		CreationHelper createHelper = workbook.getCreationHelper();
		Sheet sheet1 = workbook.createSheet("Coverage Summary");
		Sheet sheet2 = workbook.createSheet("Shifts Summary");
		HourlyDetail[] hourlyDetailsList = output.getHourlyDetail();

		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.RED.getIndex());

		Font headingFont = workbook.createFont();
		headingFont.setBold(true);
		headingFont.setFontHeightInPoints((short) 20);
		headingFont.setColor(IndexedColors.BLUE.getIndex());

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		CellStyle headingCellStyle = workbook.createCellStyle();
		headingCellStyle.setFont(headingFont);

		// Sheet 1
		Row headerRow = sheet1.createRow(0);
		String[] columns1 = { "Hour", "Physician Coverage", "App Coverage", "Scribe Coverage", "Total Coverage",
				"Percent Physician", "Expected Patient Arriving", "Covered Patient Arriving", "Difference",
				"Expected Patient Per Provider", "Covered Patient Per Provider", "Cost", "Hour Wait ", "Patient Lost" };

		for (int i = 0; i < columns1.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(columns1[i]);
			cell.setCellStyle(headerCellStyle);
		}

		double totalCost = 0;
		double totalExpectedPatient = 0;
		double totalCapacityWorkload = 0;
		double totalPatientsWaiting = 0;
		double totalPatientsLoss = 0;
		int rowCount1 = 1;
//		System.out.println(hourlyDetailsList.toString());

		for (HourlyDetail hourlyDetails : hourlyDetailsList) {
			double totalCoverage = hourlyDetails.getNumberOfPhysicians() + hourlyDetails.getNumberOfAPPs()
					+ hourlyDetails.getNumberOfScribes() + 1;
			double percentPhysician = hourlyDetails.getNumberOfPhysicians() / totalCoverage;
			double expectedPatientsPerProvider = hourlyDetails.getExpectedWorkLoad() / totalCoverage * 100 / 100;
			double coveredPatientsPerProvider = hourlyDetails.getCapacityWorkLoad() / totalCoverage * 100 / 100;
			double loss = hourlyDetails.getLoss() * 100 / 100;
			double wait = hourlyDetails.getWait() * 100 / 100;
			double differnceBetweenCapacityAndWorkload = (hourlyDetails.getCapacityWorkLoad()
					- hourlyDetails.getExpectedWorkLoad()) * 100 / 100;

			Row row = sheet1.createRow(rowCount1++);
			row.createCell(0).setCellValue(hourlyDetails.getHour());
			row.createCell(1).setCellValue(hourlyDetails.getNumberOfPhysicians());
			row.createCell(2).setCellValue(hourlyDetails.getNumberOfAPPs());
			row.createCell(3).setCellValue(hourlyDetails.getNumberOfScribes());
			row.createCell(4).setCellValue(totalCoverage);
			row.createCell(5).setCellValue(percentPhysician);
			row.createCell(6).setCellValue(hourlyDetails.getExpectedWorkLoad());
			row.createCell(7).setCellValue(hourlyDetails.getCapacityWorkLoad());
			row.createCell(8).setCellValue(differnceBetweenCapacityAndWorkload);
			row.createCell(9).setCellValue(expectedPatientsPerProvider);
			row.createCell(10).setCellValue(coveredPatientsPerProvider);
			row.createCell(11).setCellValue(hourlyDetails.getCostPerHour());
			row.createCell(12).setCellValue(wait);
			row.createCell(13).setCellValue(loss);

			totalExpectedPatient = totalExpectedPatient + hourlyDetails.getExpectedWorkLoad();
			totalCapacityWorkload = totalCapacityWorkload + hourlyDetails.getCapacityWorkLoad();
			totalPatientsWaiting = totalPatientsWaiting + wait;
			totalPatientsLoss = totalPatientsLoss + loss;
			totalCost = totalCost + hourlyDetails.getCostPerHour();
		}

		Row rowName = sheet1.createRow(170);
		Cell cell = rowName.createCell(0);
		cell.setCellValue("Overall Summary");
		cell.setCellStyle(headingCellStyle);

		String[] summary = { "Total Expected Patient", "Total Capacity Workload", "Total Patients Waiting",
				"Total Patients Loss", "Total Cost" };
		Double[] summaryValue = { totalExpectedPatient, totalCapacityWorkload, Math.abs(totalPatientsWaiting),
				Math.abs(totalPatientsLoss), totalCost };

		for (int i = 0; i < summary.length; i++) {
			Row row1 = sheet1.createRow(171 + i);
			Cell cell1 = row1.createCell(0);
			cell1.setCellValue(summary[i]);
			cell1.setCellStyle(headerCellStyle);
			Cell cell2 = row1.createCell(1);
			cell2.setCellValue(summaryValue[i]);
		}

		for (int i = 0; i < columns1.length; i++) {
			sheet1.autoSizeColumn(i);
		}

		// Sheet 2
		ArrayList<DayShift> dayShiftList = new ArrayList<>();
		dayShiftList = getDayShiftList(output, jobDetails);

		Row headerRow2 = sheet2.createRow(0);
		String[] columns2 = { "Day", "Start Time", "End Time", "Shift Length" , "Physician", "App", "Scribe" };

		for (int i = 0; i < columns2.length; i++) {
			Cell cell2 = headerRow2.createCell(i);
			cell2.setCellValue(columns2[i]);
			cell2.setCellStyle(headerCellStyle);
		}

		int rowCount2 = 1;
		for (DayShift dayShift : dayShiftList) {

			Row row = sheet2.createRow(rowCount2++);
			row.createCell(0).setCellValue(dayShift.getDay());
			row.createCell(1).setCellValue(dayShift.getStartTime());
			row.createCell(2).setCellValue(dayShift.getEndTime());
			row.createCell(3).setCellValue(dayShift.getShiftLength());
			row.createCell(4).setCellValue(dayShift.getPhysician());
			row.createCell(5).setCellValue(dayShift.getApp());
			row.createCell(6).setCellValue(dayShift.getScribe());
		}

		for (int i = 0; i < columns2.length; i++) {
			sheet2.autoSizeColumn(i);
		}

		FileOutputStream fos = new FileOutputStream("temp.xlsx");
		workbook.write(fos);
		fos.close();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos); // write excel data to a byte array
		bos.close();
		workbook.close();
		return bos;
	}

	private ArrayList<DayShift> getDayShiftList(Output output, JobDetails jobDetails) {
		int index = 0;
		ArrayList<DayShift> dayShiftList = new ArrayList<>();
		Map<String, DayShift> map = new HashMap<>();
		List<Map<Integer, Map<String, Integer>>> shiftSlots = output.getClinicianHourCount();
		System.out.println(shiftSlots);

		int length = jobDetails.getClinicians().size();
		List<String> clinicianName = new ArrayList<>();
		for (int i = 0; i < length; i++) {
			clinicianName.add(jobDetails.getClinicians().get(i).getName());
			System.out.println(jobDetails.getClinicians().get(i).getExpressions());
		}

		for (Map<Integer, Map<String, Integer>> shiftSlot : shiftSlots) {
			for (Integer key : shiftSlot.keySet()) {
				DayShift shift = new DayShift();
				for (String name : clinicianName) {
					if (shiftSlot.get(key).get(name + "Start") > 0) {
						if (map.containsKey(index + "to" + key)) {
							shift = map.get(index + "to" + key);
							shift.setName(name, shift.getName(name) + shiftSlot.get(key).get(name + "Start"));
						}

						else {
							shift = this.createNewShift(index, key);
							shift.setName(name, shiftSlot.get(key).get(name + "Start"));
						}
						shift = this.createNewShift(index, key);
						shift.setName(name, shiftSlot.get(key).get(name + "Start"));
						map.put(index + "to" + key, shift);
						dayShiftList.add(shift);
						System.out.println(index + "to" + key);
						System.out.println(map.get(index + "to" + key));
					}
//					System.out.println(index + "to" + key);
//					System.out.println(map.get(index + "to" + key));
				}
			}
			index++;
		}

//		ArrayList<DayShift> dayShiftList = new ArrayList<>();
//		for (String key : map.keySet()) {
//			dayShiftList.add(map.get(key));
//		}
		System.out.println(map);
//		System.out.println(dayShiftList);
		return dayShiftList;
	}

	private DayShift createNewShift(int startTime, Integer shiftLength) {
		String[] daysOfWeek = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
		DayShift shift = new DayShift();
		shift.setStartTime(startTime % 24);
		shift.setEndTime((startTime + shiftLength) % 24);
		shift.setDay(daysOfWeek[(int) Math.floor(startTime / 24)]);
		shift.setShiftLength(shiftLength);
		return shift;
	}

}