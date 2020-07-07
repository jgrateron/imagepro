package com.fresco.imagepro.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fresco.imagepro.service.IRotate;

@Service
public class RotateServiceImpl implements IRotate {

	@Override
	public ResponseEntity<JsonNode> v1(JsonNode request) {
		return null;
	}

}
