package com.kramphub.rest.service.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import com.kramphub.rest.model.Product;
import com.kramphub.rest.service.BookSearchService;

import sun.misc.IOUtils;

public class BookSearchServiceTest {

	@Test
    public void testJsonToModelMappingBooks() throws IOException {
		String jsonResponse =  convertStreamToString( this.getClass().getResourceAsStream("bookresponse.json"));
		BookSearchService bss = new BookSearchService("X", 10);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonObjResult = mapper.readTree(jsonResponse);
		Collection<Product> products = bss.mapResultJsonNodeToProduct(jsonObjResult);
		
		System.out.println(products);
    }
	
	public String convertStreamToString(InputStream is) throws IOException {
	    if (is != null) {
	        Writer writer = new StringWriter();

	        char[] buffer = new char[1024];
	        try {
	            Reader reader = new BufferedReader(
	                    new InputStreamReader(is, "UTF-8"));
	            int n;
	            while ((n = reader.read(buffer)) != -1) {
	                writer.write(buffer, 0, n);
	            }
	        } finally {
	            is.close();
	        }
	        return writer.toString();
	    } else {        
	        return "";
	    }
	}
}
