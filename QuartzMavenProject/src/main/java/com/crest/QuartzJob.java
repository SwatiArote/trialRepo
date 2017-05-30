package com.crest;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * Created by san6685 on 11/27/2016.
 */

public class QuartzJob implements Job {


    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("Hello...World..");
        System.out.println(new Date());
        System.out.println();
    }
}
