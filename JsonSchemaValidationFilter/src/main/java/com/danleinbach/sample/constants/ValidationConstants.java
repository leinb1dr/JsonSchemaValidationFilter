package com.danleinbach.sample.constants;

/**
 * Various constants for the validation framework.
 * <p/>
 * Created: 12/18/13
 *
 * @author Daniel
 */
public enum ValidationConstants {

	SKIP("skip"),
	RESOURCE("resource:"),
	SCHEMA_ROOT("schema-root"),
	VALIDATION_CLASS("validation-class"),
	SCHEMA_ROOT_DEFAULT("/");

	private final String value;

	private ValidationConstants(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
