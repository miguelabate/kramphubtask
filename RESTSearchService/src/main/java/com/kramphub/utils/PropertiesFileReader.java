package com.kramphub.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class in charge of reading properties from the configFile passed as parameter
 * It switches according to the environment passed as parameter
 * For example:
 * mvn jetty:run -Denv=prod -DconfigFile=./config.properties
 * 
 * With a config file like:
 * 
 *  maxResults.dev=5
 *	timeout.dev=60
 *	bookSearchServiceURL.dev=https://www.googleapis.com/books/v1/volumes
 *	albumSearchServiceURL.dev=https://itunes.apple.com/search
 *	
 *	maxResults.prod=10
 *	timeout.prod=85
 *	bookSearchServiceURL.prod=https://www.googleapis.com/books/v1/volumes
 *	albumSearchServiceURL.prod=https://itunes.apple.com/search
 *
 * @author miguel
 *
 */
public class PropertiesFileReader {
	Properties prop;;
	String environment;
	
	public PropertiesFileReader() {
		readProperties();
		String env = System.getProperty("env");
		if(env==null) environment = "dev";
		else environment=env;
	}

	public Integer getMaxResults() {
		return Integer.valueOf(prop.getProperty("maxResults."+environment));
	}
	
	public Integer getTimeout() {
		return Integer.valueOf(prop.getProperty("timeout."+environment));
	}
	
	public String getBookSearchServiceURL() {
		return prop.getProperty("bookSearchServiceURL."+environment);
	}
	
	public String getAlbumSearchServiceURL() {
		return prop.getProperty("albumSearchServiceURL."+environment);
	}
	
	private void readProperties() {
		prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(System.getProperty("configFile"));

			// load a properties file
			prop.load(input);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
