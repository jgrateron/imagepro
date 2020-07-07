package com.fresco.imagepro.service;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;

public interface ICrop {

	public ResponseEntity<JsonNode> v1(JsonNode request);
}
