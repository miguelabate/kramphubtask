package com.kramphub.rest.model;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

/**
 * Class that represents an artistic product. Book or Album.
 * @author miguel
 *
 */
@JsonPropertyOrder({ "title", "authors", "type" })
public class Product {

	private String title;
	private ArrayList<String> authors;
	private ProductType type;
	
	public Product(String title, ArrayList<String> authors, ProductType type) {
		super();
		this.title = title;
		this.authors = authors;
		this.type = type;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public ArrayList<String> getAuthors() {
		return authors;
	}
	public void setAuthors(ArrayList<String> authors) {
		this.authors = authors;
	}
	public ProductType getType() {
		return type;
	}
	public void setType(ProductType type) {
		this.type = type;
	}
	
}
