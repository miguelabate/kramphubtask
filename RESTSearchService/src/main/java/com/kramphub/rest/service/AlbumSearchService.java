package com.kramphub.rest.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.kramphub.rest.model.Product;
import com.kramphub.rest.model.ProductType;

/**
 * Concrete class that extend ProductSearhcService implementing the template methods
 * @author miguel
 *
 */
public class AlbumSearchService extends ProductSearchService{

	final static Logger logger = Logger.getLogger(AlbumSearchService.class);

	public AlbumSearchService(String serviceUrl, Integer maxResults) {
		super(serviceUrl, maxResults);
	}
	
	/**
	 * This method know how the itunes api response maps to Product model objects
	 */
	@Override
	public Collection<Product> mapResultJsonNodeToProduct(JsonNode jsonNodeProducts) {
		logger.info("Mapping album json node to model.");
		ArrayList<Product> result = new ArrayList<Product>();
		if(jsonNodeProducts.has("results")) {
			Iterator<JsonNode> itItem = jsonNodeProducts.get("results").getElements();
			while(itItem.hasNext()) {
				JsonNode item = itItem.next();
				String title = "";
				if(item.has("collectionName"))
					title = item.get("collectionName").getValueAsText();
				
				String author = "";
				if(item.has("artistName"))
					author = item.get("artistName").getValueAsText();
				
				ArrayList<String> authors = new ArrayList<String>();
				authors.add(author);
				Product product = new Product(title, authors, ProductType.ALBUM);
				result.add(product);
			}
		}else {
			return result;
		}
		return result;
	}

	/**
	 * Method that know the google API and ho to generate the url query
	 * https://itunes.apple.com/search?term=iron+maide&entity=album&limit=5
	 */
	@Override
	public String buildUrlWithParams(String query) {
		StringBuilder strBuild = new StringBuilder(this.serviceUrl);
		strBuild.append("?term=");
		strBuild.append(query);
		strBuild.append("&limit=");
		strBuild.append(this.maxResults);
		strBuild.append("&entity=album");
		strBuild.append("&media=music");
		String urlParams = strBuild.toString();
		
		logger.info("Generated request for album search: "+urlParams);
		return urlParams;
	}



}
