package com.kramphub.rest.client;

import org.apache.log4j.Logger;

/**
 * Class used as a bean to log metrics and health data for the request of the
 * upstream services
 * 
 * @author miguel
 *
 */
public class ResponseStatus {

	final static Logger logger = Logger.getLogger(ResponseStatus.class);

	private String requestURL;
	private String status;
	private String duration;

	public ResponseStatus(String requestURL, String status, String duration) {
		super();
		this.requestURL = requestURL;
		this.status = status;
		this.duration = duration;
	}

	public void doLog() {
		logger.info(this.toString());
	}

	@Override
	public String toString() {
		return "{\"requestURL\":" + requestURL + ", \"status\":" + status + ", \"duration\":" + duration + "}";
	}

}
