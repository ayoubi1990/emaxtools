package aub.ssl.cronjob;



import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aub.ssl.check.SSLDomainChecker;
import aub.ssl.check.SSLRiskHelper;
import aub.ssl.util.GUtil;

/**
 * 
 * @author M.Alayoubi
 *
 */
public class MonitorJob implements Job {

	// Logger
	private final static Logger logger  = LoggerFactory.getLogger(MonitorJob.class);
	
	private GUtil gUtil = GUtil.getInstance();
	
	/**
	 * 
	 */
	public MonitorJob() {}

	/**
	 * this function will start CronTask
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("The Cron Job started ...");
		// to be compared with Expiry Date
		Date currentDate = new Date();
		Collection<String> domainSet = gUtil.getDomainSet();
		SSLDomainChecker sslDomainChecker = new SSLDomainChecker();
		// looping on all Domains
		for (String domain: domainSet) {
			logger.info("Checking SSL Expiry Date of the Domain: " + domain);
			Date expiryDate = sslDomainChecker.checkExpiryDateOfSSL(domain);
			if (null == expiryDate) {
				logger.error("Cannot Extract Date from Domain !!");
				continue;
			}
			
			// get difference between current date and expiry date
			long diffMilliSeconds = expiryDate.getTime() - currentDate.getTime();
			long days = TimeUnit.DAYS.convert(diffMilliSeconds, TimeUnit.MILLISECONDS);
			logger.info(" This domain [" + domain + "] will be expired after " + days + " days.");
			SSLRiskHelper.checkSSLExpirationRisk(days, domain, expiryDate);
		}
	}

}
