/*
 * Copyright 2010-2011 Nabeel Mukhtar 
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

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.googleapis.maps.schema.GeoLocation;
import com.googleapis.maps.schema.PlacesResult;
import com.googleapis.maps.services.PlacesQuery;
import com.googleapis.maps.services.constant.GoogleMapsApiUrls;
import com.googleapis.maps.services.constant.ParameterNames;

/**
 * The Class PlacesQueryImpl.
 */
public class PlacesQueryImpl extends BaseGoogleMapsApiQuery<PlacesResult> implements
	PlacesQuery {
	
	/**
	 * Instantiates a new places query impl.
	 * 
	 * @param applicationId the application id
	 */
	public PlacesQueryImpl(String applicationId) {
		super(applicationId);
	}
	
	
	/* (non-Javadoc)
	 * @see com.google.code.googlesearch.client.GoogleSearchQuery#reset()
	 */
	@Override
	public void reset() {
		apiUrlBuilder = createGoogleSearchApiUrlBuilder(GoogleMapsApiUrls.PLACE_URL);
	}

	/* (non-Javadoc)
	 * @see com.google.code.googlesearch.client.impl.BaseGoogleSearchApiQuery#unmarshall(com.google.gson.JsonElement)
	 */
	@Override
	protected PlacesResult unmarshall(JsonElement object) {
		Gson gson = getGsonBuilder().create();
		return gson.fromJson(object, PlacesResult.class);
	}

	/* (non-Javadoc)
	 * @see com.googleapis.maps.services.PlacesQuery#withClient(java.lang.String)
	 */
	@Override
	public PlacesQuery withClient(String client) {
		apiUrlBuilder.withParameter(ParameterNames.CLIENT, client);
		return this;
	}


	/* (non-Javadoc)
	 * @see com.googleapis.maps.services.PlacesQuery#withLocation(com.googleapis.maps.schema.GeoLocation)
	 */
	@Override
	public PlacesQuery withLocation(GeoLocation location) {
		apiUrlBuilder.withParameter(ParameterNames.LOCATION, toParameterString(location));
		return this;
	}


	/* (non-Javadoc)
	 * @see com.googleapis.maps.services.PlacesQuery#withPrivateKey(java.lang.String)
	 */
	@Override
	public PlacesQuery withPrivateKey(String privateKey) {
//		this.privateKey = privateKey;
		return this;
	}


	/* (non-Javadoc)
	 * @see com.googleapis.maps.services.PlacesQuery#withRadius(double)
	 */
	@Override
	public PlacesQuery withRadius(double radius) {
		apiUrlBuilder.withParameter(ParameterNames.RADIUS, String.valueOf(radius));
		return this;
	}


	/* (non-Javadoc)
	 * @see com.googleapis.maps.services.PlacesQuery#withSensor(boolean)
	 */
	@Override
	public PlacesQuery withSensor(boolean sensor) {
		apiUrlBuilder.withParameter(ParameterNames.SENSOR, String.valueOf(sensor));
		return this;
	}
	
	/* (non-Javadoc)
	 * @see com.googleapis.maps.services.impl.BaseGoogleMapsApiQuery#unmarshallList(com.google.gson.JsonObject)
	 */
	@Override
	protected List<PlacesResult> unmarshallList(JsonObject response) {
		String status = response.get("status").getAsString();
		if (!"OK".equals(status) && !"ZERO_RESULTS".equals(status)) {
			throw createGoogleMapsException(status);
		}
		ArrayList<PlacesResult> list = new ArrayList<PlacesResult>();
		JsonArray results = response.get("results").getAsJsonArray();
		for (JsonElement object : results) {
			PlacesResult element = unmarshall(object);
			list.add(element);
		}
		return list;
	}
}
