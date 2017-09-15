package com.kramphub.rest.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.kramphub.rest.model.Product;
import com.kramphub.rest.service.SearchService;

/**
 * Endpoint for the product search service
 * 
 * @author miguel
 *
 */
@Path("/search")
public class SearchResource {

	final static Logger logger = Logger.getLogger(SearchResource.class);

	@GET
	@Path("/{query}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Product> searchProducts(@PathParam("query") String query) {
		SearchService searchService = new SearchService();

		List<Product> result = new ArrayList<Product>();
		result = searchService.doSearch(query);
		return result;
	}

}