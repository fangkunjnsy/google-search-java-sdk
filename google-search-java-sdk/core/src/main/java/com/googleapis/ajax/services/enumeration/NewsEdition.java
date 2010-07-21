/*
 * Copyright 2010 Nabeel Mukhtar 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 * 
 */
package com.googleapis.ajax.services.enumeration;

import java.util.HashMap;
import java.util.Map;

import com.googleapis.ajax.common.ValueEnum;

/**
 * The Enum NewsEdition.
 */
public enum NewsEdition implements ValueEnum {
	
	/** The US. */
	US("us"), 
 
 /** The UK. */
 UK("uk");
	
    /** The Constant stringToEnum. */
	private static final Map<String, NewsEdition> stringToEnum = new HashMap<String, NewsEdition>();

	static { // Initialize map from constant name to enum constant
		for (NewsEdition op : values()) {
			stringToEnum.put(op.value(), op);
		}
	}
	
    /** The value. */
    private final String value;
    
    /**
     * Instantiates a new news edition.
     * 
     * @param value the value
     */
    NewsEdition(String value) {
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
	 * @return the news edition
	 */
	public static NewsEdition fromValue(String value) {
		return stringToEnum.get(value);
	}

}
