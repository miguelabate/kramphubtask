package com.kramphub.rest.client;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.common.base.Stopwatch;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class JerseyClientGet {

	final static Logger logger = Logger.getLogger(JerseyClientGet.class);

	/**Main method created for manual testing
	 * @param args
	 */
	public static void main(String[] args) {
		logger.info("Requesting json...");
		JerseyClientGet client = new JerseyClientGet();
		// client.doGet("https://www.googleapis.com/books/v1/volumes?q=philip+k+dick&maxResults=5");
		// client.doGet("https://itunes.apple.com/search?term=iron+maide&entity=album&limit=5");
	}

	public JsonNode doGet(String url) throws IOException {
		JsonNode jsonObjResult = null;
		Stopwatch stopwatch = null;
		ClientResponse response = null;
		try {

			Client client = Client.create();

			WebResource webResource = client.resource(url);
			stopwatch = Stopwatch.createStarted();

			response = webResource.accept("application/json").get(ClientResponse.class);

			stopwatch.stop();
			if (response.getStatus() != 200) {
				logMetrics(url, stopwatch, response);
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			String output = response.getEntity(String.class);

			ObjectMapper mapper = new ObjectMapper();
			jsonObjResult = mapper.readTree(output);

			logMetrics(url, stopwatch, response);

			logger.info("Received JSON response from server. Elapsed time: " + stopwatch);
			return jsonObjResult;

		} catch (IOException e) {
			logMetrics(url, stopwatch, response);
			logger.error(e);
			throw e;
		}
	}

	/**Method to log metrics and health. Using log4j for now.
	 * @param url
	 * @param stopwatch
	 * @param response
	 */
	private void logMetrics(String url, final Stopwatch stopwatch, ClientResponse response) {
		new ResponseStatus(url, String.valueOf(response.getStatus()), stopwatch.toString()).doLog();
	}
}