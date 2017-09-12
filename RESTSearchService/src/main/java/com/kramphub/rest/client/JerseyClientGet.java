package com.kramphub.rest.client;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class JerseyClientGet {

  final static Logger logger = Logger.getLogger(JerseyClientGet.class);
	
  public static void main(String[] args) {
	  logger.info("Requesting json...");
	  JerseyClientGet client = new JerseyClientGet();
//	  client.doGet("https://www.googleapis.com/books/v1/volumes?q=philip+k+dick&maxResults=5");
//	  client.doGet("https://itunes.apple.com/search?term=iron+maide&entity=album&limit=5");
	}
  
  public JsonNode doGet(String url) throws IOException {
	  JsonNode jsonObjResult=null;
		try {

			Client client = Client.create();

			WebResource webResource = client
			   .resource(url);

			ClientResponse response = webResource.accept("application/json")
	                   .get(ClientResponse.class);

			if (response.getStatus() != 200) {
			   throw new RuntimeException("Failed : HTTP error code : "
				+ response.getStatus());
			}

			String output = response.getEntity(String.class);
			
			ObjectMapper mapper = new ObjectMapper();
			jsonObjResult = mapper.readTree(output);
			
			logger.info("Output from Server .... \n");
			logger.info(jsonObjResult);
			return jsonObjResult;
			
		  } catch (IOException e) {
			logger.error(e);
			throw e;
		  }
  }
}