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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.googleapis.maps.schema.Bounds;
import com.googleapis.maps.schema.CCTLD;
import com.googleapis.maps.schema.GeoLocation;
import com.googleapis.maps.schema.GeocodingResult;
import com.googleapis.maps.schema.Language;
import com.googleapis.maps.services.GeocodingQuery;
import com.googleapis.maps.services.constant.GoogleMapsApiUrls;
import com.googleapis.maps.services.constant.ParameterNames;

/**
 * The Class DetectLanguageQueryImpl.
 */
public class GeocodingQueryImpl extends BaseGoogleMapsApiQuery<GeocodingResult> implements
	GeocodingQuery {
	
	/**
	 * Instantiates a new detect language query impl.
	 * 
	 * @param applicationId the application id
	 */
	public GeocodingQueryImpl(String applicationId) {
		super(applicationId);
	}
	
	
	/* (non-Javadoc)
	 * @see com.google.code.googlesearch.client.GoogleSearchQuery#reset()
	 */
	@Override
	public void reset() {
		apiUrlBuilder = createGoogleSearchApiUrlBuilder(GoogleMapsApiUrls.GEOCODE_URL);
	}

	/* (non-Javadoc)
	 * @see com.google.code.googlesearch.client.impl.BaseGoogleSearchApiQuery#unmarshall(com.google.gson.JsonElement)
	 */
	@Override
	protected GeocodingResult unmarshall(JsonElement object) {
		Gson gson = getGsonBuilder().create();
		return gson.fromJson(object, GeocodingResult.class);
	}


	@Override
	public GeocodingQuery withAddress(String address) {
		apiUrlBuilder.withParameter(ParameterNames.ADDRESS, address);
		return this;
	}


	@Override
	public GeocodingQuery withAddress(GeoLocation address) {
		apiUrlBuilder.withParameter(ParameterNames.LATLNG, toParameterString(address));
		return this;
	}


	@Override
	public GeocodingQuery withBounds(Bounds bounds) {
		apiUrlBuilder.withParameter(ParameterNames.BOUNDS, toParameterString(bounds.getSouthwest(), bounds.getNortheast()));
		return this;
	}


	@Override
	public GeocodingQuery withLanguage(Language language) {
		apiUrlBuilder.withParameterEnum(ParameterNames.LANGUAGE, language);
		return this;
	}


	@Override
	public GeocodingQuery withRegion(CCTLD region) {
		apiUrlBuilder.withParameterEnum(ParameterNames.REGION, region);
		return this;
	}


	@Override
	public GeocodingQuery withSensor(boolean sensor) {
		apiUrlBuilder.withParameter(ParameterNames.SENSOR, String.valueOf(sensor));
		return this;
	}
}
