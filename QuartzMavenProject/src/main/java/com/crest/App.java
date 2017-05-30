package com.crest;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        JobDetail job = JobBuilder.newJob(QuartzJob.class).build();
//        Trigger simpleTrigger = TriggerBuilder.newTrigger().withIdentity("Simple Trigger").startNow().build();
        Trigger cornTrigger = TriggerBuilder.newTrigger().withIdentity("cron trigger").withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * 1/1 * ? *")).build();
        try {
            Scheduler sc = StdSchedulerFactory.getDefaultScheduler();
            sc.start();
            sc.scheduleJob(job,cornTrigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }
}
