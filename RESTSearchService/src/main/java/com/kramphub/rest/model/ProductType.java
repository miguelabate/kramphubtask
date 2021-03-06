package com.kramphub.rest.model;

import org.codehaus.jackson.annotate.JsonValue;

/**
 * Enum to represent the product type
 * @author miguel
 *
 */
public enum ProductType {
	BOOK("book"), ALBUM("album");
	
	private String typeName;

	ProductType(String typeName) {
		this.typeName = typeName;
	}
	
	@JsonValue
	public String getTypeName() {
		return typeName;
	}
	
}
