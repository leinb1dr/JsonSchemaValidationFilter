package com.danleinbach.sample.validation;

/**
 * Outline for a validation processor that uses Json Schema's to perform validation.
 * <p/>
 * Created: 12/18/13
 *
 * @author Daniel
 */
public interface IJsonSchemaValidationProcessor extends IValidationProcessor {

	/**
	 * Set the root folder location in the classpath to look for schemas
	 *
	 * @param schemaRoot - Root folder location in classpath
	 */
	void setSchemaRoot(String schemaRoot);


	/**
	 * Get the root folder location in the classpath to look for schemas
	 *
	 * @return Root folder for json schemas
	 */
	String getSchemaRoot();

}
