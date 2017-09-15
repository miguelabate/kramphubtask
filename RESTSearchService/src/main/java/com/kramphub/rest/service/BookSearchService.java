package com.kramphub.rest.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.kramphub.rest.model.Product;
import com.kramphub.rest.model.ProductType;

/**Concrete class that extend ProductSearhcService implementing the template methods
 * @author miguel
 *
 */
public class BookSearchService extends ProductSearchService {

	final static Logger logger = Logger.getLogger(BookSearchService.class);

	public BookSearchService(String serviceUrl, Integer maxResults) {
		super(serviceUrl, maxResults);
	}

	/**
	 * This method know how the google api response maps to Product model objects
	 */
	@Override
	public Collection<Product> mapResultJsonNodeToProduct(JsonNode jsonNodeProducts) {
		logger.info("Mapping book json node to model.");
		ArrayList<Product> result = new ArrayList<Product>();
		if (jsonNodeProducts.has("items")) {
			Iterator<JsonNode> itItem = jsonNodeProducts.get("items").getElements();
			while (itItem.hasNext()) {
				JsonNode item = itItem.next();
				if (item.has("volumeInfo")) {
					JsonNode volInfo = item.get("volumeInfo");
					String title = "";
					if (volInfo.has("title"))
						title = volInfo.get("title").getValueAsText();

					Iterator<JsonNode> itAuthors = null;
					if (volInfo.has("authors"))
						itAuthors = volInfo.get("authors").getElements();

					ArrayList<String> authors = new ArrayList<String>();
					if (itAuthors != null)
						while (itAuthors.hasNext()) {
							authors.add(itAuthors.next().getTextValue());
						}

					Product product = new Product(title, authors, ProductType.BOOK);
					result.add(product);
				}
			}
		} else {
			return result;
		}
		return result;
	}

	/**
	 * Method that know the google API and ho to generate the url query
	 * https://www.googleapis.com/books/v1/volumes?q=philip+k+dick&maxResults=5
	 */
	@Override
	public String buildUrlWithParams(String query) {
		StringBuilder strBuild = new StringBuilder(this.serviceUrl);
		strBuild.append("?q=");
		try {
			strBuild.append(URLEncoder.encode(query, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error("Error encoding query search param",e);
		}
		strBuild.append("&maxResults=");
		strBuild.append(this.maxResults);
		String urlParams = strBuild.toString();

		logger.info("Generated request for book search: " + urlParams);
		return urlParams;
	}

}
