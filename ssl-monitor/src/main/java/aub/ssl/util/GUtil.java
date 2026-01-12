package aub.ssl.util;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author M.Alayoubi
 *
 */
public class GUtil {
	
	// Logger
	private final static Logger logger = LoggerFactory.getLogger(GUtil.class);
	// singleton instance
	private static GUtil instance = null;
	// Properties Instance
	private static Properties appProps = new Properties();
	// Map contains Domains
	private final static Map<String, String> domainMap = new HashMap<>();

	/**
	 * 
	 */
	private GUtil() {
		initialize();
	}
	
	/**
	 * Initialize Properties
	 */
	private void initialize() {
		try {
			logger.info("Starting Collecting GoDaddy Domain ...");
			InputStream propertiesFile = getClass().getClassLoader().getResourceAsStream("godaddy.properties");
			appProps.load(propertiesFile);
			
			// fill the a Map Contains all Properties read from Property File
			final AtomicInteger runCount = new AtomicInteger(0);
			appProps.keySet().stream().map(Object::toString).filter(propKey -> propKey.startsWith("ssl")).forEach(propKey -> {
				String propValue = appProps.getProperty(propKey);
				domainMap.put(propKey, propValue.trim());
				
				logger.info("Domain " + runCount.incrementAndGet() + ": " + propValue);
			});
		} catch (Exception e) {
			logger.error("Error when reading godaddy.properties !!", e);
		}
	}

	/**
	 * Singleton
	 * @return
	 */
	public static GUtil getInstance() {
		if (null == instance)
			instance = new GUtil();
		return instance;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getProperty(String key) {
		return appProps.getProperty(key);
	}
	
	/**
	 * 
	 * @return
	 */
	public Collection<String> getDomainSet() {
		return domainMap.values();
	}
	
	/**
	 * 
	 * @param propKey: Message Key exists in "godaddy.properties"
	 * @param params
	 * @return
	 */
	public String getFormattedMessage(String propKey, Object... params) {
		String message = appProps.getProperty(propKey);
		return MessageFormat.format(message, params);
	}
}
