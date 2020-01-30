package com.envision.Staffing.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import com.envision.Staffing.services.WorkflowService;

public class AutorunJob implements Job {

	@Autowired
    private WorkflowService workflowService;
	
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        String autorunJobId = jobDataMap.getString("jobId");

        workflowService.autorunWorkflowService(autorunJobId);
	}

}
