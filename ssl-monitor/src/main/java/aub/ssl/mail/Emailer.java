package aub.ssl.mail;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aub.ssl.util.GUtil;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class Emailer {

	// Logger
	private final static Logger logger = LoggerFactory.getLogger(Emailer.class);
	
	private Session session;
	private GUtil gUtil = GUtil.getInstance();
	
	/**
	 * 
	 */
    public Emailer() {
    	Properties prop = new Properties();
    	prop.put("mail.smtp.auth", true);
    	prop.put("mail.smtp.starttls.enable", "true");
    	prop.put("mail.smtp.host", gUtil.getProperty("mail.smtp.host"));
    	prop.put("mail.smtp.port", gUtil.getProperty("mail.smtp.port"));
    	prop.put("mail.smtp.ssl.trust", gUtil.getProperty("mail.smtp.ssl.trust"));
    	
    	session = Session.getInstance(prop, new Authenticator() {
    	    @Override
    	    protected PasswordAuthentication getPasswordAuthentication() {
    	        return new PasswordAuthentication(gUtil.getProperty("mail.smtp.username"), gUtil.getProperty("mail.smtp.password"));
    	    }
    	});
    }


   /**
    * 
    * @param subject
    * @param msg
    */
    public void sendEmail(String subject, String msg) {
    	try {
    		
    		String toEmails = gUtil.getProperty("message.mail.toEmails");
    		String ccEmails = gUtil.getProperty("message.mail.ccEmails");
    		logger.info("**** Sending Subject: " + subject + " ****");
    		logger.info("**** Start Sending Email to: [" + toEmails + "], cc: [" + ccEmails + "] ****");
	    	Message message = new MimeMessage(session);
	    	message.setFrom(new InternetAddress(gUtil.getProperty("mail.smtp.username")));
	    	message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmails));
	    	message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccEmails));
	    	message.setSubject(subject);
	
	    	MimeBodyPart mimeBodyPart = new MimeBodyPart();
	    	mimeBodyPart.setContent(msg, "text/html; charset=utf-8");
	
	    	Multipart multipart = new MimeMultipart();
	    	multipart.addBodyPart(mimeBodyPart);
	
	    	message.setContent(multipart);
	
	    	Transport.send(message);
	    	logger.info("*** Finish Sending Email ***");
    	} catch (MessagingException e) {
    		logger.error(" ERROR when sending Email !!", e);
		}
    }
    
}