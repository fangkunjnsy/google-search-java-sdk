/**
 * 
 */
package com.googleapis.ajax.services.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.googleapis.ajax.common.PagedArrayList;
import com.googleapis.ajax.common.PagedList;
import com.googleapis.ajax.schema.Language;
import com.googleapis.ajax.schema.TranslateLanguageResult;
import com.googleapis.ajax.services.GoogleSearchException;
import com.googleapis.ajax.services.TranslateLanguageQuery;
import com.googleapis.ajax.services.constant.GoogleSearchApiUrls;
import com.googleapis.ajax.services.constant.ParameterNames;
import com.googleapis.ajax.services.enumeration.TranslationFormat;

/**
 * The Class TranslateLanguageQueryImpl.
 */
public class TranslateLanguageQueryImpl extends BaseGoogleSearchApiQuery<TranslateLanguageResult> implements
	TranslateLanguageQuery {
	
	/**
	 * Instantiates a new translate language query impl.
	 * 
	 * @param applicationId the application id
	 */
	public TranslateLanguageQueryImpl(String applicationId) {
		super(applicationId);
	}
	
	
	/* (non-Javadoc)
	 * @see com.google.code.googlesearch.client.GoogleSearchQuery#reset()
	 */
	@Override
	public void reset() {
		apiUrlBuilder = createGoogleSearchApiUrlBuilder(GoogleSearchApiUrls.LANGUAGE_TRANSLATE_URL);
	}

	/* (non-Javadoc)
	 * @see com.google.code.googlesearch.client.TranslateLanguageQuery#withFormat(com.google.code.googlesearch.client.enumeration.TranslationFormat)
	 */
	@Override
	public TranslateLanguageQuery withFormat(TranslationFormat format) {
		apiUrlBuilder.withParameterEnum(ParameterNames.FORMAT, format);
		return this;
	}


	/* (non-Javadoc)
	 * @see com.google.code.googlesearch.client.TranslateLanguageQuery#withLanguagePair(java.lang.String, java.lang.String)
	 */
	@Override
	public TranslateLanguageQuery withLanguagePair(Language sourceLanguage,
			Language targetLanguage) {
		String languagePair = "|" + targetLanguage.value();
		if (sourceLanguage != null) {
			languagePair = sourceLanguage.value() + languagePair;
		}
		apiUrlBuilder.withParameter(ParameterNames.LANGUAGE_PAIR, languagePair);
		return this;
	}

	/**
	 * Unmarshall list.
	 * 
	 * @param response the response
	 * 
	 * @return the paged list< t>
	 */
	protected PagedList<TranslateLanguageResult> unmarshallList(JsonObject response) {
		int status = response.get("responseStatus").getAsInt();
		if (status != 200) {
			throw new GoogleSearchException(String.valueOf(response.get("responseDetails").getAsString()));
		}
		JsonArray dataArray = response.get("responseData").getAsJsonArray();
		PagedArrayList<TranslateLanguageResult> list = new PagedArrayList<TranslateLanguageResult>();
		if (dataArray != null) {
			for (JsonElement element : dataArray) {
	        	if (element.isJsonObject()) {
	        		JsonObject json = element.getAsJsonObject();
	        		status = json.get("responseStatus").getAsInt();
	        		if (status != 200) {
	        			throw new GoogleSearchException(json.get("responseDetails").getAsString());
	        		}
	        		JsonElement data = json.get("responseData");
	        		if (data != null) {
	        			list.add(unmarshall(data));
	        		}
	        	}
			}
		} 
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.google.code.googlesearch.client.impl.BaseGoogleSearchApiQuery#unmarshall(com.google.gson.JsonElement)
	 */
	@Override
	protected TranslateLanguageResult unmarshall(JsonElement object) {
		Gson gson = getGsonBuilder().create();
		return gson.fromJson(object, TranslateLanguageResult.class);
	}
}