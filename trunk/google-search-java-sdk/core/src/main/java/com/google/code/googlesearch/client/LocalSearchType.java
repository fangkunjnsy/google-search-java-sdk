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
public enum LocalSearchType implements ValueEnum {
	BLENDED("blended"), KMLONLY("kmlonly"), LOCALONLY("localonly");
	
    /** The Constant stringToEnum. */
	private static final Map<String, LocalSearchType> stringToEnum = new HashMap<String, LocalSearchType>();

	static { // Initialize map from constant name to enum constant
		for (LocalSearchType op : values()) {
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
    LocalSearchType(String value) {
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
	public static LocalSearchType fromValue(String value) {
		return stringToEnum.get(value);
	}

}
