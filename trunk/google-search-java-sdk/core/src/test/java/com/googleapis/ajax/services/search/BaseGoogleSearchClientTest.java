/**
 * 
 */
package com.googleapis.ajax.services.search;

import java.util.Collection;

import junit.framework.TestCase;

/**
 * The Class BaseGoogleSearchClientTest.
 */
public class BaseGoogleSearchClientTest extends TestCase {
    
    /** The Constant RESOURCE_MISSING_MESSAGE. */
	protected static final String RESOURCE_MISSING_MESSAGE = "Please define a test %s in TestConstants.properties file.";
	
	/**
	 * Assert not null or empty.
	 * 
	 * @param message the message
	 * @param value the value
	 */
	protected static void assertNotNullOrEmpty(String message, String value) {
		assertNotNull(message, value);
		assertFalse(message, "".equals(value));
	}
	
	/**
	 * Assert not null or empty.
	 * 
	 * @param message the message
	 * @param value the value
	 */
	protected static void assertNotNullOrEmpty(String message, Collection<?> value) {
		assertNotNull(message, value);
		assertFalse(message, value.isEmpty());
	}
}
