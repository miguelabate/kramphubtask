package com.kramphub.rest.service.exceptions;

/**
 * Custom exception used for handling timeouts when calling services in
 * CompletableFutures
 * 
 * @author miguel
 *
 */
public class SearchTimeoutException extends Exception {

	public SearchTimeoutException() {
		super();
	}

	public SearchTimeoutException(String message) {
		super(message);
	}

}
