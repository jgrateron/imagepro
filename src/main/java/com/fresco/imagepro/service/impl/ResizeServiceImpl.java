package com.fresco.imagepro.service.impl;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

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
import com.fresco.imagepro.utils.ImageProUtils;

@Service
public class ResizeServiceImpl implements IResize 
{

	@Autowired
	private ObjectMapper mapper;
	
	/**
	 * 
	 */
	public ResponseEntity<JsonNode> v1(JsonNode request) throws RespuestaException 
	{
		//todos los parametros son opcionales
		JsonNode jwidth = request.get("width");
		JsonNode jheight = request.get("height");
		JsonNode jmode = request.get("mode");
		JsonNode jurlimg = request.get("urlimg");
		JsonNode jb64img = request.get("b64img");
		
		String width = null;
		if (jwidth != null) {
			width = jwidth.asText(); 
		}
		String height = null;
		if (jheight != null) {
			height = jheight.asText(); 
		}
		if (width == null && height == null) {
			throw new RespuestaException("Debe incluir el width o height");
		}
		String mode = null;
		if (jmode != null) {
			mode = jmode.asText();
		}
		String urlimg = null;
		if (jurlimg != null) {
			urlimg = jurlimg.asText(); 
		}
		String b64img = null;
		if (jb64img != null) {
			b64img = jb64img.asText(); 
		}
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
		
		int w = image.getWidth();
		int h = image.getHeight();
		if (width != null && height != null)
		{
			w = Integer.parseInt(width);
			h = Integer.parseInt(height);
		}
		else
		if (width != null) {
			w = Integer.parseInt(width);
		}
		else
		if (height != null) {
			h = Integer.parseInt(height);
		}
		Image newImage = image.getScaledInstance(Math.round(w), Math.round(h), Image.SCALE_SMOOTH);
		String imagen;
		try 
		{
			imagen = ImageProUtils.writeImageToBytes(w,h,newImage);
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
			InputStream imgIS = ImageProUtils.getImgFromBase64(content, param);
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
