package com.fresco.imagepro.service.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
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
import com.fresco.imagepro.service.IRotate;
import com.fresco.imagepro.utils.MiImagen;
import com.fresco.imagepro.utils.Utils;

@Service
public class RotateServiceImpl implements IRotate 
{
	private static final Logger logger = LoggerFactory.getLogger(RotateServiceImpl.class);
	
	@Autowired
	private ObjectMapper mapper;
	
	@Override
	public ResponseEntity<JsonNode> v1(JsonNode request) throws RespuestaException 
	{
		String angle = Utils.getParameterRequest(request,"angle");
		if (angle == null) {
			throw new RespuestaException("Debe incluir angle");
		}
		String color = Utils.getParameterRequest(request,"color");
		if (color == null) {
			throw new RespuestaException("Debe incluir color");
		}
		Color c = null;
		if (!color.isEmpty()) {
			c = new Color(Utils.parseInt(color, "color"));	
		}
		
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
		b64img = null;
		int w = miImagen.getImage().getWidth();
		int h = miImagen.getImage().getHeight();
		double a = Utils.parseDouble(angle,"angle");
		if (a < -360 || a > 360 ) {
			throw new RespuestaException("Angulo incorrecto (-360,360)");
		}
		logger.info("rotate angle: " + a + " color: " + color);		
		BufferedImage newImg = rotate(miImagen.getImage(),a, c);
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
	/**
	 * 
	 * @param bimg
	 * @param angle
	 * @return
	 */
	private BufferedImage rotate(BufferedImage bimg, double angle, Color c) {

	    int w = bimg.getWidth();    
	    int h = bimg.getHeight();

	    BufferedImage rotated = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);  
	    Graphics2D graphic = rotated.createGraphics();
	    if (c != null) {
		    graphic.setColor(c);
		    graphic.fillRect(0, 0, w, h);	    	
	    }
	    AffineTransform transform = new AffineTransform();
	    transform.rotate(Math.toRadians(angle), w/2, h/2);
	    //graphic.rotate(Math.toRadians(angle), w/2, h/2);
	    //graphic.drawImage(bimg, null, 0, 0);
	    graphic.drawRenderedImage(bimg, transform);
	    graphic.dispose();
	    return rotated;
	}
}
