package com.fresco.imagepro.service;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fresco.imagepro.exception.RespuestaException;

public interface IResize {
	public ResponseEntity<JsonNode> v1(JsonNode request) throws RespuestaException;
}
