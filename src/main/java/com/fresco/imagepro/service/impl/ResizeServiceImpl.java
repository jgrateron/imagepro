package com.fresco.imagepro.service.impl;

//creado por Jairo Grateron jgrateron@gmail.com

import java.awt.Image;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fresco.imagepro.controller.Response;
import com.fresco.imagepro.exception.RespuestaException;
import com.fresco.imagepro.service.IResize;
import com.fresco.imagepro.utils.MiImagen;
import com.fresco.imagepro.utils.Utils;

@Service
public class ResizeServiceImpl implements IResize 
{
	private static int MAXWIDTH = 8192;
	private static int MAXHEIGHT = 4320;
	private static int MAXPORC = 500;
	
	private static final Logger logger = LoggerFactory.getLogger(ResizeServiceImpl.class);
	
	@Autowired
	private ObjectMapper mapper;
	
	/**
	 * 
	 */
	public ResponseEntity<JsonNode> v1(JsonNode request) throws RespuestaException 
	{
		//todos los parametros son opcionales

		String width = Utils.getParameterRequest(request,"width");
		String height = Utils.getParameterRequest(request,"height");
		if (width == null || height == null) {
			throw new RespuestaException("Debe incluir el width y height");
		}
		if (width.isEmpty() && height.isEmpty()) {
			throw new RespuestaException("Debe incluir el width o height no vacios");
		}
		if (width.isEmpty()) {
			width = null;
		}
		if (height.isEmpty()) {
			height = null;
		}
		String typesize = Utils.getParameterRequest(request,"typesize");
		validarTypesize(typesize, width, height);
		
		String stretch = Utils.getParameterRequest(request,"stretch");
		validarStretch(stretch);

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
		float w = miImagen.getImage().getWidth();
		float h = miImagen.getImage().getHeight();
		if (width != null && height != null)
		{
			if ("%".equals(typesize)) {
				float percent = Utils.parseInt(width,"% width") / 100.0f;
				w = w * percent;
				percent = Utils.parseInt(height,"% height") / 100.0f;
				h = h * percent;
			}
			else
			{
				w = Utils.parseInt(width,"width");
				h = Utils.parseInt(height,"height");				
			}
		}
		else
		if (width != null) 
		{
			if ("%".equals(typesize)) {
				float percent = Utils.parseInt(width,"% width") / 100.0f;
				w = w * percent;
			}
			else {
				w = Utils.parseInt(width,"width");
			}
			if ("false".equals(stretch)) {
				float divisor =  miImagen.getImage().getWidth() / w;
				h = Math.round(h / divisor);
			}
		}
		else
		if (height != null) 
		{
			if ("%".equals(typesize)) {
				float percent = Utils.parseInt(height,"% height") / 100.0f;
				h = h * percent;
			}
			else {
				h = Utils.parseInt(height,"height");
			}
			if ("false".equals(stretch)) {
				float divisor =  miImagen.getImage().getHeight() / h;
				w = Math.round(w / divisor);
			}			
		}
		logger.info("resize old w: " +  miImagen.getImage().getWidth() + " h: " +  miImagen.getImage().getHeight() + " new w: " + w + " h: " + h);
		if (Math.round(w) == 0 || Math.round(h) == 0) {
			throw new RespuestaException("Las nuevas dimensiones son iguales a 0");
		}
		if (w > MAXWIDTH) {
			throw new RespuestaException("El valor máximo del nuevo width es " + MAXWIDTH);
		}
		if (h > MAXHEIGHT) {
			throw new RespuestaException("El valor máximo del nuevo height es " + MAXHEIGHT);
		}
		Image newImage =  miImagen.getImage().getScaledInstance(Math.round(w), Math.round(h), Image.SCALE_SMOOTH);
		String formatName = miImagen.getFormatName();
		miImagen = null;	
		String imagen;
		try 
		{
			imagen = Utils.writeImageToB64(Math.round(w),Math.round(h), newImage, formatName);
			newImage = null;

		}
		catch (IOException e) {
			throw new RespuestaException(e.toString());
		}
		Response response = new Response(true);
		JsonNode nodeResponse = mapper.valueToTree(response);
		ObjectNode oNode = (ObjectNode) nodeResponse;
		oNode.put("imagen", imagen);
		oNode.put("formatName", formatName);
		return ResponseEntity.status(HttpStatus.OK).body(nodeResponse);			
	}
	/**
	 * 
	 * @param stretch
	 * @throws RespuestaException
	 */
	private void validarStretch(String stretch) throws RespuestaException {
		if (stretch == null) {
			throw new RespuestaException("Debe incluir el parámetro stretch");
		}
		if (!("true".equals(stretch) || "false".equals(stretch))){
			throw new RespuestaException("El parámetro stretch no es válido (true,false)");
		}
	}
	/**
	 * 
	 * @param typesize
	 * @throws RespuestaException
	 */
	private void validarTypesize(String typesize, String width, String height) throws RespuestaException 
	{
		if (typesize == null) {
			throw new RespuestaException("Debe incluir el parámetro typesize");
		}
		if (!("px".equals(typesize) || "%".equals(typesize))) {
			throw new RespuestaException("El parámetro typesize no es válido (px,%) ");
		}
		if ("%".equals(typesize)) {
			if (width != null) {
				int w = Utils.parseInt(width,"% width");
				if (w > MAXPORC) {
					throw new RespuestaException("El porcentaje máximo de width es " + MAXPORC);
				}
			}
			if (height != null) {
				int h = Utils.parseInt(height,"% height");
				if (h > MAXPORC) {
					throw new RespuestaException("El porcentaje máximo de height es " + MAXPORC);
				}				
			}
		}
		if ("px".equals(typesize)) {
			if (width != null) {
				int w = Utils.parseInt(width,"width");
				if (w > MAXWIDTH) {
					throw new RespuestaException("El valor máximo de width es " + MAXWIDTH);
				}
			}
			if (height != null) {
				int h = Utils.parseInt(height,"% height");
				if (h > MAXHEIGHT) {
					throw new RespuestaException("El valor máximo de height es " + MAXHEIGHT);
				}				
			}			
		}
	}
}
