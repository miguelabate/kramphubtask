package com.kramphub.rest.service.test;

import java.io.IOException;
import java.util.Collection;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import com.kramphub.rest.model.Product;
import com.kramphub.rest.model.ProductType;
import com.kramphub.rest.service.AlbumSearchService;
import com.kramphub.rest.service.BookSearchService;
import com.kramphub.rest.test.utils.TestUtils;

public class BookSearchServiceTest {

	@Test
	public void testJsonToModelMappingBooks() throws IOException {
		String jsonResponse = TestUtils
				.convertStreamToString(this.getClass().getResourceAsStream("bookresponse2.json"));
		BookSearchService bookSearchService = new BookSearchService("X", 10);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonObjResult = mapper.readTree(jsonResponse);
		Collection<Product> products = bookSearchService.mapResultJsonNodeToProduct(jsonObjResult);

		// checking result list size
		Assert.assertEquals(20, products.size());
		// just checking the first element
		Product firstProduct = products.iterator().next();
		Assert.assertEquals("Mick Wall", firstProduct.getAuthors().get(0));
		Assert.assertEquals("Black Sabbath: Symptom of the Universe", firstProduct.getTitle());
		Assert.assertEquals(ProductType.BOOK, firstProduct.getType());

		jsonResponse = TestUtils.convertStreamToString(this.getClass().getResourceAsStream("bookresponse.json"));
		jsonObjResult = mapper.readTree(jsonResponse);
		products = bookSearchService.mapResultJsonNodeToProduct(jsonObjResult);
		// checking result list size
		Assert.assertEquals(5, products.size());
		// just checking the first element
		firstProduct = products.iterator().next();
		Assert.assertEquals("Philip K. Dick", firstProduct.getAuthors().get(0));
		Assert.assertEquals("The Exegesis of Philip K Dick", firstProduct.getTitle());
		Assert.assertEquals(ProductType.BOOK, firstProduct.getType());
	}
	
	@Test
	public void testUrlforRequestCreation() throws IOException {
		BookSearchService bookSearchService = new BookSearchService("https://www.googleapis.com/books/v1/volumes", 5);
		
		String url = bookSearchService.buildUrlWithParams("someBook");
		Assert.assertEquals("https://www.googleapis.com/books/v1/volumes?q=someBook&maxResults=5", url);
		
		String url2 = bookSearchService.buildUrlWithParams("the man in the high castle");
		Assert.assertEquals("https://www.googleapis.com/books/v1/volumes?q=the+man+in+the+high+castle&maxResults=5", url2);		
	}

}
