package com.kramphub.rest.resource;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.kramphub.rest.model.Product;
import com.kramphub.rest.model.ProductType;
import com.kramphub.rest.model.Track;
import com.kramphub.rest.service.SearchService;
import com.kramphub.utils.PropertiesFileReader;

@Path("/search")
public class SearchResource {

	final static Logger logger = Logger.getLogger(SearchResource.class);
	
	@GET
	@Path("/{query}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Product> searchProducts(@PathParam("query") String query) {
		SearchService searchService = new SearchService();
		
		ArrayList<Product> result = new ArrayList<Product>();
		result = searchService.doSearch(query);
		return result;
	}

}