package com.envision.Staffing.services;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.envision.Staffing.model.Day;
import com.envision.Staffing.model.Payload;
import com.envision.Staffing.model.Shift;
import com.envision.Staffing.model.Workload;
import com.envision.Staffing.services.ShiftCalculator;
@Service
public class ShiftPlanningService {
	
	  public String[] days = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
      "Saturday" };
	
	public void getShiftPlan(Payload pl) throws IOException {

	
		List<List<Shift>> res = new ArrayList<List<Shift>>();
	    String path = "DCM_OUTPUT/Shifts.txt";
	    String costpath = "DCM_OUTPUT/Cost_Summary.txt";
	    String utilPath = "DCM_OUTPUT/Utilization_Summary.txt";
	    Workload wL = new Workload();
	    Payload d = new Payload ( pl.day );
	    Day d1 = new Day (d.day[0].name,  d.day[0].workload);
	    Day d2 = new Day ( d.day[1].name,  d.day[1].workload );
	    Day d3 = new Day ( d.day[2].name,  d.day[2].workload );
	    Day d4 = new Day ( d.day[3].name,  d.day[3].workload );
	    Day d5 = new Day ( d.day[4].name, d.day[4].workload );
	    Day d6 = new Day ( d.day[5].name,  d.day[5].workload );
	    Day d7 = new Day ( d.day[6].name, d.day[6].workload );
	    
	    Workload w1 = new Workload();
	    
	    w1.fixedworkloadArray = d1.workload;
	    w1.workloadArray = d1.workload;
	    w1.day = d1.name;
	    
	    Workload w2 = new Workload();
	    w2.fixedworkloadArray = d2.workload;
	    w2.workloadArray = d2.workload;
	    w2.day = d2.name;
	    Workload w3 = new Workload();
	    w3.fixedworkloadArray = d3.workload;
	    w3.workloadArray = d3.workload;
	    w3.day = d3.name;
	    Workload w4 = new Workload();
	    w4.fixedworkloadArray = d4.workload;
	    w4.workloadArray = d4.workload;
	    w4.day = d4.name;
	    Workload w5 = new Workload();
	    w5.fixedworkloadArray = d5.workload;
	    w5.workloadArray = d5.workload;
	    w5.day = d5.name;
	    Workload w6 = new Workload();
	    w6.fixedworkloadArray = d6.workload;
	    w6.workloadArray = d6.workload;
	    w6.day = d6.name;
	    Workload w7 = new Workload();
	    w7.fixedworkloadArray = d7.workload;
	    w7.workloadArray = d7.workload;
	    w7.day = d7.name;
	    Workload[] wl = { w1, w2, w3, w4, w5, w6, w7};

	    Workload work = new Workload();
	    int k = 0;

	    //Fetch workload array
	    for (int ii = 0; ii < 7; ii++)
	    {
	        for (int jj = 0; jj < 24; jj++)
	        {
	            work.fixedworkloadArray[k] = wl[ii].fixedworkloadArray[jj];
	            work.workloadArray[k] = wl[ii].fixedworkloadArray[jj];
	            k++;
	        }
	    }

	    int[] noOfTwelve= {0,0,0,0,0,0,0 };
	    int[] noOfTen={ 0,0,0,0,0,0,0 };
	    int[] noOfEight={ 0,0,0,0,0,0,0 };
	    int[] noOfFour= { 0, 0, 0, 0, 0, 0, 0 };
	    int[] totalHours = { 0, 0, 0, 0, 0, 0, 0 };
	    int[] cost = { 0, 0, 0, 0, 0, 0, 0 };

	    ShiftCalculator shiftCalculator = new ShiftCalculator();
	    shiftCalculator.setWorkloads(work);
	    shiftCalculator.calculatePhysicianSlots(12);
	    shiftCalculator.calculatePhysicianSlots(10);
	    shiftCalculator.calculatePhysicianSlots(8);
	    shiftCalculator.calculate4hourslots();

	    double[] utilizationArray = shiftCalculator.calculateUtilization();
	    List<List<Shift>> dayToshiftsmapping = shiftCalculator.printSlots();

	    

	    FileWriter sw = new FileWriter(path, true);
	   
	    sw.write("Shifts");
	    sw.write("\n");
	    sw.write("------------------------------------------------------------------------\n");
	    sw.write("Day | Clinician | Shift Start Time | Shift End Time | Shift Hour Length\n");
	    
	    for (int i = 0; i < 7; i++)
	    {
	  
	            sw.flush();
	            sw.write("------------------------------------------------------------------------");
	            sw.write("\n");
	            for (Shift s : dayToshiftsmapping.get(i))
	            {

	                sw.write(wl[i].day + " | " + "Physician" + " | " + s.start_time + " | " + s.end_time + " | " + s.no_of_hours+"\n");
	                if (s.no_of_hours == 12)
	                    noOfTwelve[i]++;
	                else if (s.no_of_hours == 8)
	                    noOfEight[i]++;
	                else if (s.no_of_hours == 10)
	                    noOfTen[i]++;
	                else if (s.no_of_hours == 4)
	                    noOfFour[i]++;
	            }

	            totalHours[i] += (12*noOfTwelve[i]) + (8*noOfEight[i]) + (10*noOfTen[i]) + (4*noOfFour[i]);
	            cost[i] = 320 * totalHours[i];
	            //sw.WriteLine("------------------------------------------------");
	        
	    }

	    
	    sw.close();
	    
	    //Calculation of Costs

	    FileWriter sw1 = new FileWriter(costpath, true);
	   

	        sw1.flush();
	        sw1.write("Cost Summary");
	        sw1.write("\n");
	        sw1.write("-------------------------------------------------\n");
	        sw1.write("Day | Clinician | Total Hours | Total Cost (in $)\n");
	        sw1.write("-------------------------------------------------");
	        sw1.write("\n");

	        for (int x = 0; x < 7; x++)
	        {
	            sw1.write(wl[x].day + " | Physician | " + totalHours[x] + " | " + cost[x]+"\n");
	        }
	    
	sw1.close();

	
	 //Calculation of utilization

   FileWriter sw2 = new FileWriter(utilPath);
    
   sw2.flush();
   sw2.write("Utilization Summary");
   sw2.write("\n");
   sw2.write("-------------------------------------------------\n");
   sw2.write("Acceptable utilization level -> 75% <= Utilization <= 110%\n");
        sw2.write("\n");
        sw2.write("----------------------------------------\n");
        sw2.write("Day | Hour | Utilization per Hour (in %)\n");
 
 
        int start = 0;
        int z; 
        for (int x = 0; x < 7; x++)
        {
            sw2.write("----------------------------------------");
            sw2.write("\n");
            for ( z = start; z < start+24; z++)
            {
                int end = z + 1;
                double util = shiftCalculator.round(utilizationArray[z] * 100, 2) ;
                sw2.write(days[x] + " | " + (z%24) + ".00 - " + (end%24) + ".00 | " + util+"\n");
            }

            start = start + 24; 

        }
    
        sw2.close();
  //  return dayToshiftsmapping;
	}
}
