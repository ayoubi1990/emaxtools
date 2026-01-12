package aub.ssl.main;


import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aub.ssl.cronjob.MonitorJob;
import aub.ssl.util.GUtil;

/**
 * 
 * @author M.Alayoubi
 *
 */
public class MonitorLauncher {

	// Logger
	private final static Logger logger = LoggerFactory.getLogger(MonitorLauncher.class);
	
	private static boolean stop = false;

	/**
	 * 
	 */
	public MonitorLauncher() {}

	
	/**
	 * This method is used for Windows Service to Start the application as a Service
	 * @param args
	 */
	public static void start(String[] args) {
        try {
        	logger.info("****** SSL Monitoring System started ******");
    		// default Cron Interval 24 hours
    		Integer cronHourInterval = 24;
    		String cronHourProp = GUtil.getInstance().getProperty("cron.hour.interval");
    		if (cronHourProp != null)
    			cronHourInterval = Integer.valueOf(cronHourProp.trim());
    		
    		logger.info("This Job will be running each: " + cronHourInterval + " Hour.");
    		JobDetail monitorJob = JobBuilder.newJob(MonitorJob.class).withIdentity("monitorJob", "ess").build();
    		Trigger monitorTrigger = TriggerBuilder.newTrigger().withIdentity("monitorTrigger", "ess")
    				.withSchedule(SimpleScheduleBuilder.repeatHourlyForever(cronHourInterval)).build();
    		
    		Scheduler godaddyScheduler = new StdSchedulerFactory().getScheduler();
    		godaddyScheduler.start();
    		godaddyScheduler.scheduleJob(monitorJob, monitorTrigger);
    		
    		while (!stop)
    			Thread.sleep(5000);
    		
    		// Shutdown the Process (scheduler)
    		godaddyScheduler.shutdown();
        } catch (Exception e) {
			logger.error("Exception occured in the Main Process !!", e);
		}
    }
	
	/**
	 * This method is used for Windows Service to Stop the running application as a Service
	 * @param args
	 */
	public static void stop(String[] args) {
		logger.info("Calling Stop Method.");
        stop = true;
    }
	
	/**
	 * Main Method
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		if ("start".equals(args[0])) {
            start(args);
        } else if ("stop".equals(args[0])) {
            stop(args);
        }
	}
}
