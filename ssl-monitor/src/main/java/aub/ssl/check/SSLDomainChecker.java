package aub.ssl.check;

import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author M.Alayoubi
 *
 */
public class SSLDomainChecker {

	// Logger
	private final static Logger logger = LoggerFactory.getLogger(SSLDomainChecker.class);

	/**
	 * 
	 */
	public SSLDomainChecker() {}

	/**
	 * 
	 * @param domain
	 * @return
	 */
	public Date checkExpiryDateOfSSL(String domain) {	
		try {
			logger.info("Getting Expiry Date of [" + domain + "]");
			// Result Return
			Date expiryDate = null;
			// configure the SSLContext with a TrustManager
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() }, new SecureRandom());
			SSLContext.setDefault(ctx);
	
			// get Connection of the Proxy Server of this Domain
			String uri = "https://" + domain.trim();
			URL url = new URL(uri);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String arg0, SSLSession arg1) {
					return true;
				}
			});
	
			logger.info("Reason Code of the Proxy: " + conn.getResponseCode());
			// Get SSL Certificate Information
			Certificate[] certs = conn.getServerCertificates();
			for (Certificate cert : certs) {
	
				if (!(cert instanceof X509Certificate))
					continue;
	
				X509Certificate x509Cert = (X509Certificate) cert;
				String subjectDN = x509Cert.getSubjectDN().getName();
				String[] subjectDNArray = subjectDN.split("=");
				// ignore Intermediate/Root Certificate
				if (subjectDNArray.length > 1) {
					String subjectDNName = subjectDNArray[1].split(",")[0].trim(); // split , is used when they have Wildcard Certificate
					if (!subjectDNName.startsWith("*") && !domain.equalsIgnoreCase(subjectDNName))
						continue;
	
					expiryDate = x509Cert.getNotAfter();
					logger.info("Expiry Date: " + expiryDate);
				}
			}
			// Terminate Connection
			conn.disconnect();
			return expiryDate;
		} catch (Exception e) {
			logger.error("Error when checking the SSL validity !!", e);
			SSLRiskHelper.sendAlertWhenDomainNotAccessible(domain);
		}
		
		return null;
	}
	
	/**
	 * 
	 * @author M.Alayoubi
	 *
	 */
	private static class DefaultTrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}
}
