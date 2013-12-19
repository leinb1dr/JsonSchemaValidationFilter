package com.danleinbach.sample.constants;

/**
 * Schema location constants for what part of the request the schema belongs.
 * <p/>
 * Created: 12/18/13
 *
 * @author Daniel
 */
public enum SchemaLocationConstants {

	HEADER("header"),
	BODY("body"),
	PARAM("param");

	private final String value;

	private SchemaLocationConstants(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
