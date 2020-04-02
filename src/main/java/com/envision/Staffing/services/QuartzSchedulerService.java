package com.envision.Staffing.services;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.envision.Staffing.job.AutorunJob;
import com.envision.Staffing.model.JobDetails;

@Service
public class QuartzSchedulerService {

	@Autowired
	private Scheduler scheduler;

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	Logger log = Logger.getLogger(QuartzSchedulerService.class);
	public void scheduleJob(JobDetails jobDetails) {
		log.info("Entering method for scheduling job :");
		log.info("id"+jobDetails.getId());
		JobDetail jobDetail = buildJobDetail(jobDetails);
		log.info(jobDetail.getKey());
		Trigger trigger = buildJobTrigger(jobDetail, jobDetails);
		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			log.error("Error happened in scheduling job :"+e);
			e.printStackTrace();
		}
	} 
	
	public void rescheduleJob(String id, JobDetails jobDetails) {
		log.info("Entering method for rescheduling job :");
		JobDetail jobDetail = buildJobDetail(jobDetails);
		Trigger trigger = buildJobTrigger(jobDetail, jobDetails);
		TriggerKey triggerkey = TriggerKey.triggerKey(id,"DEFAULT");
		try {
			scheduler.rescheduleJob(triggerkey, trigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			log.error(e);
			e.printStackTrace();
		}
	}

	private JobDetail buildJobDetail(JobDetails jobDetails) {
		
		JobDataMap jobDataMap = new JobDataMap();
		String id =jobDetails.getId();
        log.info( jobDetails.getId());
		jobDataMap.put("jobId",jobDetails.getId());
		log.info("jobid"+jobDetails.getId()); 
		return JobBuilder.newJob(AutorunJob.class) // TODO: Change to respective class when added
				.withIdentity(jobDetails.getId()).withDescription(jobDetails.getName()).usingJobData(jobDataMap)
				.storeDurably() // Whether or not the Job should remain stored after it is orphaned (no Triggers
								// point to it).
				.build();
	}

	private Trigger buildJobTrigger(JobDetail jobDetail, JobDetails jobDetails) {
		return TriggerBuilder.newTrigger().forJob(jobDetail).withIdentity(jobDetail.getKey().getName())
				.withDescription(jobDetail.getDescription()).startAt(new Date())
				.withSchedule(CronScheduleBuilder.cronSchedule(jobDetails.getCronExpression())).build();
	}
}
