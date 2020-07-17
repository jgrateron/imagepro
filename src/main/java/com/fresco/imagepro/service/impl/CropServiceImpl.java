package com.fresco.imagepro.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fresco.imagepro.service.ICrop;

@Service
public class CropServiceImpl implements ICrop {

	@Override
	public ResponseEntity<JsonNode> v1(JsonNode request) 
	{
		String width = getParameterRequest(request,"width");
		return null;
	}
	/**
	 * 
	 * @param request
	 * @param param
	 * @return value
	 */
	private String getParameterRequest(JsonNode request , String param) {
		JsonNode jparam = request.get(param);
		if (jparam != null) {
			return jparam.asText();
		}
		return null;
	}
}
