package aub.ssl.check;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aub.ssl.mail.Emailer;
import aub.ssl.util.GUtil;

/**
 * 
 * @author M.Alayoubi
 *
 */
public class SSLRiskHelper {

	private final static Logger logger = LoggerFactory.getLogger(SSLRiskHelper.class);
	
	private static GUtil gUtil = GUtil.getInstance();
	private static Emailer emailer = new Emailer();
	
	/**
	 * This function will check if the expiration date is close, it will send alert
	 * @param days
	 * @param domainCN
	 * @param expiryDate
	 */
	public static void checkSSLExpirationRisk(long days, String domainCN, Date expiryDate) {
		logger.info("Start checking if the SSL expiration date is close ...");
		if (days == 30 || days == 15 || days <= 7) {
			String emailMsgTitle = gUtil.getFormattedMessage("message.mail.expiry.subject", domainCN);
			String emailMsgBody = gUtil.getFormattedMessage("message.mail.expiry.body", domainCN, days, expiryDate);
			logger.info(" The SSL certificate of [" + domainCN + "] will be expired after " + days + " days");
			emailer.sendEmail(emailMsgTitle, emailMsgBody);
		}
		logger.info("End checking if the SSL expiration date is close.");
	}
	
	/**
	 * 
	 * @param domainCN
	 */
	public static void sendAlertWhenDomainNotAccessible(String domainCN) {
		logger.info("Start sending immediate Alert !!");
		String emailMsgTitle = gUtil.getFormattedMessage("message.mail.access.subject", domainCN);
		String emailMsgBody = gUtil.getFormattedMessage("message.mail.access.body", domainCN);
		emailer.sendEmail(emailMsgTitle, emailMsgBody);
		logger.info("End sending immediate Alert.");
	}

}
