package com.fresco.imagepro.service.impl;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

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
import com.fresco.imagepro.utils.Utils;

@Service
public class ResizeServiceImpl implements IResize 
{

	private static final Logger logger = LoggerFactory.getLogger(ResizeServiceImpl.class);
	
	@Autowired
	private ObjectMapper mapper;
	
	/**
	 * 
	 */
	public ResponseEntity<JsonNode> v1(JsonNode request) throws RespuestaException 
	{
		//todos los parametros son opcionales

		String width = getParameterRequest(request,"width");
		String height = getParameterRequest(request,"height");
		if (width == null && height == null) {
			throw new RespuestaException("Debe incluir el width o height");
		}
		String typesize = getParameterRequest(request,"typesize");
		validarTypesize(typesize);
		
		String stretch = getParameterRequest(request,"stretch");
		validarStretch(stretch);

		String urlimg = getParameterRequest(request,"urlimg");

		String b64img = getParameterRequest(request,"b64img");

		if (urlimg == null && b64img == null){
			throw new RespuestaException("Debe incluir el urlimg o b64img");
		}
		if (urlimg != null && b64img != null){
			throw new RespuestaException("Debe incluir el urlimg o b64img");
		}
		BufferedImage image = null;
		
		if (urlimg != null) {
			image = getImageFromUrl(urlimg,"urlimg");
		}
		if (b64img != null) {
			image = getImageFromBase64(b64img, "b64img");
		}
		
		float w = image.getWidth();
		float h = image.getHeight();
		if (width != null && height != null)
		{
			w = Utils.parseInt(width,"width");
			h = Utils.parseInt(height,"height");
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
				float divisor = image.getWidth() / w;
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
				float divisor = image.getHeight() / h;
				w = Math.round(w / divisor);
			}			
		}
		logger.info("old w: " + image.getWidth() + " h: " + image.getHeight() + " new w: " + w + " h: " + h);
		if (Math.round(w) == 0 || Math.round(h) == 0) {
			throw new RespuestaException("Las nuevas dimensiones son iguales a 0");
		}
		Image newImage = image.getScaledInstance(Math.round(w), Math.round(h), Image.SCALE_SMOOTH);
		String imagen;
		try 
		{
			imagen = Utils.writeImageToBytes(Math.round(w),Math.round(h),newImage);
		}
		catch (IOException e) {
			throw new RespuestaException(e.toString());
		}
		
		Response response = new Response(true);
		JsonNode nodeResponse = mapper.valueToTree(response);
		ObjectNode oNode = (ObjectNode) nodeResponse;
		oNode.put("imagen", imagen);
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
	/**
	 * 
	 * @param typesize
	 * @throws RespuestaException
	 */
	private void validarTypesize(String typesize) throws RespuestaException {
		if (typesize == null) {
			throw new RespuestaException("Debe incluir el parámetro typesize");
		}
		if (!("px".equals(typesize) || "%".equals(typesize))) {
			throw new RespuestaException("El parámetro typesize no es válido (px,%) ");
		}
	}
	/**
	 * 
	 * @param urlimg url de la imagen
	 * @param param nombre de la variable
	 * @return Image
	 * @throws RespuestaException
	 */
	private BufferedImage getImageFromUrl(String urlimg, String param) throws RespuestaException
	{
		try 
		{
			URL urlImg = new URL(urlimg);
			BufferedImage image = ImageIO.read(urlImg);
			if (image == null) {
				throw new RespuestaException(param + " no es una imagen válida");
			}
			return image;
		} 
		catch (IOException e) {
			throw new RespuestaException(e.toString());
		}
	}
	/**
	 * 
	 * @param content contenido de la imagen en base64
	 * @param param nombre del parametro
	 * @return Image
	 * @throws RespuestaException
	 */
	private BufferedImage getImageFromBase64(String content, String param) throws RespuestaException
	{
		try 
		{
			InputStream imgIS = Utils.getImgFromBase64(content, param);
			BufferedImage image = ImageIO.read(imgIS);
			if (image == null) {
				throw new RespuestaException("urlimg es una imagen inválida");
			}
			return image;
		} 
		catch (IOException e) {
			throw new RespuestaException(e.toString());
		}
	}
}