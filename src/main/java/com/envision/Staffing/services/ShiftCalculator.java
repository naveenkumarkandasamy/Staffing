package com.envision.Staffing.services;

import java.util.ArrayList;
import java.util.List;

import com.envision.Staffing.model.Shift;
import com.envision.Staffing.model.Workload;


public class ShiftCalculator {

    Workload wl = new Workload();


    public void setWorkloads( Workload w)
    {
        //Initialize workload array 
        wl.fixedworkloadArray = w.fixedworkloadArray;
        wl.workloadArray = w.workloadArray;
    }

 
    public void calculatePhysicianSlots(int numberOfHours)
    {
        int start = 0;
        int flag = 1;
        double factor = 0.75;

        // Check every 12 hour slot Eg : 0-12 , 1-13, 2-14 ..... (Assuming numberOfHours = 12)
        while (start + numberOfHours < wl.sizeOfArray)
        {
            flag = 1;
            double capacityOfCurrentDoctor = 0;
            for (int j = start; j < start + numberOfHours; j++)
            {

                // Checks the total work a physician will be able to complete in a 12-hour
                // slot
                if (wl.physicianCountperhour[j] != 0
                        && (round(wl.fixedworkloadArray[j] - wl.capacityArray[j], 2)) / wl.physicianCountperhour[j] <= 0.1)
                {

                    capacityOfCurrentDoctor = round(capacityOfCurrentDoctor + wl.fixedworkloadArray[j] - wl.capacityArray[j], 2);

                }
                else
                {
                    if ((j - start) % wl.dayDuration == 0)
                        capacityOfCurrentDoctor = round(capacityOfCurrentDoctor + wl.firstHourCapacity, 2);

                    else if ((j - start) % wl.dayDuration == numberOfHours - 1)
                        capacityOfCurrentDoctor = round(capacityOfCurrentDoctor + wl.lastHourCapacity, 2);

                    else
                        capacityOfCurrentDoctor = round(capacityOfCurrentDoctor + wl.midHourCapacity, 2);
                }

       
                //Checks if adding a physician is needed 
                if ((j - start) % wl.dayDuration == (numberOfHours - 1)
                        && ((capacityOfCurrentDoctor / numberOfHours) < factor))
                {
                    flag = 0;
                    break;
                }
            }

            if (flag == 1)
            {
                // A 12 hour slot can be added
                calculateNewWorkloads(start, start + numberOfHours);
                calculateCapacities(start, start + numberOfHours);

                for (int x = start; x < start + numberOfHours; x++)
                {
                    // Increase the count of the number of physicians used each hour for the 12-hour
                    // slot
                    wl.physicianCountperhour[x]++;
                }

                Shift shift = new Shift();
                shift.start_time = start % wl.dayDuration;
                shift.end_time = (start + numberOfHours) % wl.dayDuration;
                shift.day = start / wl.dayDuration;
                shift.no_of_hours = numberOfHours;
                wl.result.add(shift);

            }
            else
            {
                //Check the next 12- hour slot
                start = start + 1;

            }

        }
    }
  
    public  void calculate4hourslots()
    {
        int start = 0;
        boolean flag = false;
        int sizeOfSlot = 4;

        //A four - hour slot is added whenever there are 2 consecutive slots where utilization > given range (110%)
        while (start < wl.sizeOfArray)
        {
            flag = false;
            int j = start;
            if (wl.physicianCountperhour[j] != 0
                    && (round(wl.fixedworkloadArray[j] - wl.capacityArray[j], 2)) / wl.physicianCountperhour[j] <= 0.1)
            {
                flag = true;
            }

            if (flag)
            {
                start = start + 1;
            }
            else
            {
                if ((start + sizeOfSlot) > wl.sizeOfArray - 1)
                {
                    calculateNewWorkloads(start, wl.sizeOfArray);
                    calculateCapacities(start, wl.sizeOfArray);
                }
                else
                {
                    calculateNewWorkloads(start, start + sizeOfSlot);
                    calculateCapacities(start, start + sizeOfSlot);
                }
                for (int x = start; x < start + sizeOfSlot; x++)
                {
                    wl.physicianCountperhour[x]++;
                }


                Shift shift = new Shift();
                shift.start_time = start % wl.dayDuration;
                shift.end_time = (start + sizeOfSlot) % wl.dayDuration;
                shift.no_of_hours = sizeOfSlot;
                shift.day = start / wl.dayDuration;
                wl.result.add(shift);   //Add shift in the list

            }
        }
    }

    public void calculateNewWorkloads(int start, int end)
    {
        //Reduces the workloads as per the capacities of each physician per hour 
        for (int i = start; i < end; i++)
        {
            if (i == start)
            {
                wl.workloadArray[i] = round(wl.workloadArray[i] - wl.firstHourCapacity, 2);
            }

            else if (i == end - 1)
            {
                wl.workloadArray[i] = round(wl.workloadArray[i] - wl.lastHourCapacity, 2);

            }
            else
            {
                wl.workloadArray[i] = round(wl.workloadArray[i] - wl.midHourCapacity, 2);
            }
        }

    }
    public  void calculateCapacities(int start, int end)
    {
        for (int i = start; i < end; i++)
        {
            if (i == start)
            {
                wl.capacityArray[i] = round(wl.capacityArray[i] + wl.firstHourCapacity, 2);
            }

            else if (i == end - 1)
            {
                wl.capacityArray[i] = round(wl.capacityArray[i] + wl.lastHourCapacity, 2);

            }

            else
            {
                wl.capacityArray[i] = round(wl.capacityArray[i] + wl.midHourCapacity, 2);
            }
        }


    }

    public double round(double value, int places)
    {
        long factor = (long)Math.pow(10, places);
        value = value * factor;
        double tmp = Math.round(value);
        return (double)tmp / factor;
    }

    public  List<List<Shift>> printSlots()
    {
        List<List<Shift>> dayToShiftMapping = new ArrayList<List<Shift>>(); //Maps --- Day --->> shift-slots per day 

        for(int i= 0; i <7; i++) // Loop through List with foreach
        {
            List<Shift> shiftList = new ArrayList<Shift>();
            for (Shift s : wl.result)
            {
                if(s.day == i)
                {
                    shiftList.add(s);
                }

            }
            dayToShiftMapping.add(shiftList);
        }
       
        return dayToShiftMapping;
    }

    public  void printWorkloadArray()
    {
        for (int i = 0; i < wl.sizeOfArray; i++)
        {
           System.out.println("WorkloadIndex: " + i + " Value :" + wl.workloadArray[i]);
        }
    }

    public  double[] calculateUtilization()
    {
        for (int i = 0; i < wl.sizeOfArray; i++)
        {
            wl.utilizationArray[i] = (wl.fixedworkloadArray[i]) / (wl.capacityArray[i]);
            System.out.println("UtiLIndex: " + i + " Value :" + wl.utilizationArray[i]);



        }
        return wl.utilizationArray;
    }


}

