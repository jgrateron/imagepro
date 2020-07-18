package com.fresco.imagepro.service.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fresco.imagepro.controller.Response;
import com.fresco.imagepro.exception.RespuestaException;
import com.fresco.imagepro.service.ICrop;
import com.fresco.imagepro.utils.MiImagen;
import com.fresco.imagepro.utils.Utils;

@Service
public class CropServiceImpl implements ICrop {

	@Autowired
	private ObjectMapper mapper;

	@Override
	public ResponseEntity<JsonNode> v1(JsonNode request) throws RespuestaException 
	{
		String xx = Utils.getParameterRequest(request,"x");
		String yy = Utils.getParameterRequest(request,"y");
		String width = Utils.getParameterRequest(request,"width");
		String height = Utils.getParameterRequest(request,"height");
		if (xx == null) {
			throw new RespuestaException("Debe incluir x");
		}
		if (yy == null) {
			throw new RespuestaException("Debe incluir y");
		}
		if (width == null) {
			throw new RespuestaException("Debe incluir width");
		}
		if (height == null) {
			throw new RespuestaException("Debe incluir height");
		}
		
		int x = Utils.parseInt(xx, "x");
		int y = Utils.parseInt(yy, "y");
		int w = Utils.parseInt(width, "width");
		int h = Utils.parseInt(height, "height");
		
		String urlimg = Utils.getParameterRequest(request,"urlimg");

		String b64img = Utils.getParameterRequest(request,"b64img");
		
		if (urlimg == null && b64img == null){
			throw new RespuestaException("Debe incluir el urlimg o b64img");
		}
		if (urlimg != null && b64img != null){
			throw new RespuestaException("Debe incluir el urlimg o b64img");
		}
		MiImagen miImagen = null;
		
		if (urlimg != null) {
			miImagen = Utils.getImageFromUrl(urlimg,"urlimg");
		}
		if (b64img != null) {
			miImagen = Utils.getImageFromBase64(b64img, "b64img");
		}
		if (x + w > miImagen.getImage().getWidth()) {
			throw new RespuestaException("(x + width) is outside of Raster");
		}
		if (y + h > miImagen.getImage().getHeight()) {
			throw new RespuestaException("(y + height) is outside of Raster");
		}
		BufferedImage newImg = miImagen.getImage().getSubimage(x, y, w, h);
		String formatName = miImagen.getFormatName();
		miImagen = null;	    
		String imagen;
		try 
		{
			imagen = Utils.writeImageToB64(Math.round(w),Math.round(h),newImg,formatName);
			miImagen = null;
			newImg = null;
		}
		catch (IOException e) {
			throw new RespuestaException(e.toString());
		}
		Response response = new Response(true);
		JsonNode nodeResponse = mapper.valueToTree(response);
		ObjectNode oNode = (ObjectNode) nodeResponse;
		oNode.put("imagen", imagen);
		oNode.put("formatName", formatName);
		imagen = null;
		
		return ResponseEntity.status(HttpStatus.OK).body(nodeResponse);			
	}
}
