package com.envision.Staffing.job;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import com.envision.Staffing.services.WorkflowService;

public class AutorunJob implements Job {

	@Autowired
	private WorkflowService workflowService;

	Logger log = Logger.getLogger(AutorunJob.class);

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
		String autorunJobId = jobDataMap.getString("jobId");
		try {
			log.info("JobId ::" + autorunJobId);
			workflowService.autorunWorkflowService(autorunJobId);
		} catch (Exception e) {
			log.error("error happened in executing the autorunjob :", e);
		}
	}

}
