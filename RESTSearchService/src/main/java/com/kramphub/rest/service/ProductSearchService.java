package com.kramphub.rest.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.kramphub.rest.client.JerseyClientGet;
import com.kramphub.rest.model.Product;

/**
 * Abstract class for product searches. Implementing strategy pattern.
 * @author miguel
 *
 */
public abstract class ProductSearchService {

	final static Logger logger = Logger.getLogger(ProductSearchService.class);
	
	protected String serviceUrl;
	protected Integer maxResults;
	
	public ProductSearchService(String serviceUrl, Integer maxResults) {
		this.serviceUrl=serviceUrl;
		this.maxResults=maxResults;
	}

	public ArrayList<Product> search(String query){
		logger.info("Doing request to "+serviceUrl);
		ArrayList<Product> result = new ArrayList<Product>();
		JerseyClientGet client = new JerseyClientGet();
		String urlWithParams = buildUrlWithParams(query);
		JsonNode jsonNodeProducts = null;
		try {
			jsonNodeProducts = client.doGet(urlWithParams);
		} catch (IOException e) {
			logger.error("Exception requesting url "+urlWithParams, e);
		}
		
		result.addAll(mapResultJsonNodeToProduct(jsonNodeProducts));
		
		return result;
	}

	public abstract Collection<Product> mapResultJsonNodeToProduct(JsonNode jsonNodeProducts);

	public abstract String buildUrlWithParams(String query) ;
}
