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
package com.googleapis.maps.services.impl;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.googleapis.maps.common.PagedArrayList;
import com.googleapis.maps.common.PagedList;
import com.googleapis.maps.schema.ListingType;
import com.googleapis.maps.services.AsyncResponseHandler;
import com.googleapis.maps.services.GoogleMapsException;
import com.googleapis.maps.services.GoogleMapsQuery;
import com.googleapis.maps.services.constant.ApplicationConstants;
import com.googleapis.maps.services.constant.GoogleMapsApiUrls.GoogleMapsApiUrlBuilder;

/**
 * The Class BaseGoogleSearchApiQuery.
 */
public abstract class BaseGoogleMapsApiQuery<T> extends GoogleMapsApiGateway implements GoogleMapsQuery<T> {
	
	protected static final Charset UTF_8_CHAR_SET = Charset.forName(ApplicationConstants.CONTENT_ENCODING);

	/** The api url builder. */
	protected GoogleMapsApiUrlBuilder apiUrlBuilder;
    
    /** The parser. */
    protected final JsonParser parser = new JsonParser();
    
    /** The handlers. */
    private List<AsyncResponseHandler<PagedList<T>>> handlers = new ArrayList<AsyncResponseHandler<PagedList<T>>>();
	
	/**
	 * Instantiates a new base google search api query.
	 * 
	 * @param applicationId the application id
	 */
	public BaseGoogleMapsApiQuery(String applicationId) {
		super.setApplicationKey(applicationId);
        requestHeaders = new HashMap<String, String>();

        // by default we compress contents
        requestHeaders.put("Accept-Encoding", "gzip, deflate");
        this.reset();
	}

	/**
	 * Instantiates a new base google search api query.
	 * 
	 * @param applicationId the application id
	 * @param apiVersion the api version
	 */
	public BaseGoogleMapsApiQuery(String applicationId, String apiVersion) {
		this(applicationId);
		super.setApiVersion(apiVersion);
	}
	
	/* (non-Javadoc)
	 * @see com.google.code.stackexchange.client.query.StackOverflowApiQuery#list()
	 */
	@Override
	public PagedList<T> list() {
		InputStream jsonContent = null;
        try {
        	jsonContent = callApiGet(apiUrlBuilder.buildUrl());
        	JsonElement response = parser.parse(new InputStreamReader(jsonContent, UTF_8_CHAR_SET));
        	if (response.isJsonObject()) {
        		PagedList<T> responseList = unmarshallList(response.getAsJsonObject());
        		notifyObservers(responseList);
    			return responseList;
        	}
        	throw new GoogleMapsException("Unknown content found in response:" + response.toString());
        } catch (Exception e) {
            throw new GoogleMapsException(e);
        } finally {
	        closeStream(jsonContent);
	    }
	}

	/**
	 * Unmarshall list.
	 * 
	 * @param response the response
	 * 
	 * @return the paged list< t>
	 */
	protected PagedList<T> unmarshallList(JsonObject response) {
		int status = response.get("responseStatus").getAsInt();
		if (status != 200) {
			throw new GoogleMapsException(String.valueOf(response.get("responseDetails").getAsString()));
		}
		JsonObject data = response.get("responseData").getAsJsonObject();
		PagedArrayList<T> list = new PagedArrayList<T>();
		if (data != null) { 
			JsonArray results = data.get("results").getAsJsonArray();
			for (JsonElement object : results) {
				T element = unmarshall(object);
				list.add(element);
			}
			JsonElement cursor = data.get("cursor");
			if (cursor != null) {
				list.setCursor(new Gson().fromJson(cursor, PagedArrayList.Cursor.class));
			}
		} 
		return list;
	}

	/**
	 * Unmarshall.
	 * 
	 * @param object the object
	 * 
	 * @return the t
	 */
	protected abstract T unmarshall(JsonElement object);

	/* (non-Javadoc)
	 * @see com.google.code.stackexchange.client.query.StackOverflowApiQuery#singleResult()
	 */
	@Override
	public T singleResult() {
		InputStream jsonContent = null;
        try {
        	jsonContent = callApiGet(apiUrlBuilder.buildUrl());
        	JsonElement response = parser.parse(new InputStreamReader(jsonContent, UTF_8_CHAR_SET));
        	if (response.isJsonObject()) {
        		JsonObject json = response.getAsJsonObject();
        		int status = json.get("responseStatus").getAsInt();
        		if (status != 200) {
        			throw new GoogleMapsException(json.get("responseDetails").getAsString());
        		}
        		JsonElement data = json.get("responseData");
        		if (data != null) {
        			return unmarshall(data);
        		}
        	}
        	throw new GoogleMapsException("Unknown content found in response:" + response.toString());
        } catch (Exception e) {
            throw new GoogleMapsException(e);
        } finally {
	        closeStream(jsonContent);
	    }
	}
	
	/**
	 * Notify observers.
	 * 
	 * @param response the response
	 */
	protected void notifyObservers(PagedList<T> response) {
		for(AsyncResponseHandler<PagedList<T>> handler : handlers) {
			handler.handleResponse(response);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.google.code.stackexchange.client.query.StackExchangeApiQuery#addResonseHandler(com.google.code.stackexchange.client.AsyncResponseHandler)
	 */
	public void addResonseHandler(AsyncResponseHandler<PagedList<T>> handler) {
		handlers.add(handler);
	}
	
    /* (non-Javadoc)
     * @see com.google.code.stackexchange.client.impl.StackOverflowApiGateway#marshallObject(java.lang.Object)
     */
    protected String marshallObject(Object element) {
    	return null;
    }
    
	/**
	 * Gets the gson builder.
	 * 
	 * @return the gson builder
	 */
	protected GsonBuilder getGsonBuilder() {
		GsonBuilder builder = new GsonBuilder();
		builder.setDateFormat(ApplicationConstants.RFC822DATEFORMAT);
		builder.registerTypeAdapter(ListingType.class, new JsonDeserializer<ListingType>() {

			@Override
			public ListingType deserialize(JsonElement arg0, Type arg1,
					JsonDeserializationContext arg2) throws JsonParseException {
				return ListingType.fromValue(arg0.getAsString());
			}
			
		});
		
		return builder;
	}
    
	/* (non-Javadoc)
	 * @see com.google.code.googlesearch.client.impl.GoogleSearchApiGateway#unmarshallObject(java.lang.Class, java.io.InputStream)
	 */
	@Override
	protected <V> V unmarshallObject(Class<V> clazz, InputStream xmlContent) {
		return null;
	}
	
	/**
	 * Creates the google search api url builder.
	 * 
	 * @param urlFormat the url format
	 * 
	 * @return the google search api url builder
	 */
	protected GoogleMapsApiUrlBuilder createGoogleSearchApiUrlBuilder(String urlFormat) {
		return new GoogleMapsApiUrlBuilder(urlFormat);
	}

	/* (non-Javadoc)
	 * @see com.google.code.googlesearch.client.GoogleSearchQuery#withLocale(java.util.Locale)
	 */
	@Override
	public GoogleMapsQuery<T> withLocale(Locale locale) {
//		apiUrlBuilder.withParameter(ParameterNames.HOST_LANGUAGE, locale.getLanguage());
		return this;
	}

	/* (non-Javadoc)
	 * @see com.google.code.googlesearch.client.GoogleSearchQuery#withQuery(java.lang.String)
	 */
	@Override
	public GoogleMapsQuery<T> withQuery(String query) {
//		apiUrlBuilder.withParameter(ParameterNames.QUERY, query);
		return this;
	}
}