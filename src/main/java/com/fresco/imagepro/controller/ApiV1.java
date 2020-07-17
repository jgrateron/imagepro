package com.fresco.imagepro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fresco.imagepro.exception.RespuestaException;
import com.fresco.imagepro.service.ICrop;
import com.fresco.imagepro.service.IResize;
import com.fresco.imagepro.service.IRotate;

@RestController
@RequestMapping("/api/v1")
public class ApiV1 {

	@Autowired
	private IResize resize;
	
	@Autowired
	private ICrop crop;
	
	@Autowired
	private IRotate rotate;
	
	@Autowired
	private ObjectMapper mapper;

	/**
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping("/resize")
	public ResponseEntity<JsonNode> resize(@RequestBody JsonNode request)
	{
		try 
		{
			return resize.v1(request);
		} 
		catch (RespuestaException e) 
		{
			return responseError(e.getMessage());
		}
	}
	/**
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping("/rotate")
	public ResponseEntity<JsonNode> rotate(@RequestBody JsonNode request)
	{
		try 
		{
			return rotate.v1(request);
		} 
		catch (RespuestaException e) {
			return responseError(e.getMessage());
		}
	}
	/**
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping("/crop")
	public ResponseEntity<JsonNode> crop(@RequestBody JsonNode request)
	{
		try
		{
			return crop.v1(request);	
		}
		catch (RespuestaException e) {
			return responseError(e.getMessage());
		}		
	}
	/**
	 * 
	 * @param mensaje
	 * @return
	 */
	private ResponseEntity<JsonNode> responseError(String mensaje)
	{
		Response response = new Response(false);
		JsonNode nodeResponse = mapper.valueToTree(response);
		ObjectNode oNode = (ObjectNode) nodeResponse;
		oNode.put("message", mensaje);
		return ResponseEntity.status(HttpStatus.OK).body(nodeResponse);		
	}		
}
