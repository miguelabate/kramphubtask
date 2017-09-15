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
import com.kramphub.rest.test.utils.TestUtils;

public class AlbumSearchServiceTest {

	@Test
	public void testJsonToModelMappingAlbums() throws IOException {
		String jsonResponse = TestUtils
				.convertStreamToString(this.getClass().getResourceAsStream("albumsresponse.json"));
		AlbumSearchService albumSearchService = new AlbumSearchService("X", 5);//for now we dont care about the url. Not making the real request.
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonObjResult = mapper.readTree(jsonResponse);
		Collection<Product> products = albumSearchService.mapResultJsonNodeToProduct(jsonObjResult);

		//checking result list size
		Assert.assertEquals(5, products.size());
		//just checking the first element
		Product firstProduct = products.iterator().next();
		Assert.assertEquals("Iron Maiden", firstProduct.getAuthors().get(0));
		Assert.assertEquals("Flight 666: The Original Soundtrack (Live)", firstProduct.getTitle());
		Assert.assertEquals(ProductType.ALBUM, firstProduct.getType());
	}
	
	@Test
	public void testUrlforRequestCreation() throws IOException {
		AlbumSearchService bss = new AlbumSearchService("https://itunes.apple.com/search", 5);
		
		String url = bss.buildUrlWithParams("myQuery");
		Assert.assertEquals("https://itunes.apple.com/search?term=myQuery&limit=5&entity=album&media=music", url);
		
		String url2 = bss.buildUrlWithParams("the beatles");
		Assert.assertEquals("https://itunes.apple.com/search?term=the+beatles&limit=5&entity=album&media=music", url2);		
	}
}
