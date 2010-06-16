/**
 * 
 */
package com.google.code.googlesearch.client;

import java.util.HashMap;
import java.util.Map;

import com.google.code.googlesearch.common.ValueEnum;

/**
 * @author nmukhtar
 *
 */
public enum ImageColorization implements ValueEnum {
	GRAY("gray"), COLOR("color");
	
    /** The Constant stringToEnum. */
	private static final Map<String, ImageColorization> stringToEnum = new HashMap<String, ImageColorization>();

	static { // Initialize map from constant name to enum constant
		for (ImageColorization op : values()) {
			stringToEnum.put(op.value(), op);
		}
	}
	
    /** The value. */
    private final String value;
    
    /**
     * Instantiates a new user timeline type.
     * 
     * @param value the value
     */
    ImageColorization(String value) {
        this.value = value;
    }

	@Override
	public String value() {
		return value;
	}
	
	/**
	 * From value.
	 * 
	 * @param value the value
	 * 
	 * @return the user timeline type
	 */
	public static ImageColorization fromValue(String value) {
		return stringToEnum.get(value);
	}

}