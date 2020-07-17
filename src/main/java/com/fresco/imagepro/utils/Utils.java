package com.fresco.imagepro.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fresco.imagepro.exception.RespuestaException;

public class Utils {

	public static String writeImageToB64(int width, int height, Image image, String formatName) throws IOException 
	{
		BufferedImage bi = null;
		if ("png".equals(formatName)) {
			bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		}
		else {
			bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Graphics2D g = bi.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		ImageIO.write(bi, formatName, baos);
		baos.close();
		bi = null;
		g = null;
		byte[] encoded = Base64.getEncoder().encode(baos.toByteArray());
		if (encoded == null || encoded.length == 0) {
			throw new IOException("encoded empty");
		}
		return new String(encoded);
	}

	public static String OutputStreamToBase64(ByteArrayOutputStream result) {
		byte[] encoded = Base64.getEncoder().encode(result.toByteArray());
		return new String(encoded);
	}

	/**
	 * 
	 * @param content
	 * @param parameter
	 * @return
	 * @throws IOException
	 */
	public static InputStream getImgFromBase64(String content, String parameter) throws IOException {
		content = content.replaceAll("\\n", "");
		content = content.replaceAll("\\r", "");
		byte[] decodedBytes = Base64.getDecoder().decode(content);
		return new ByteArrayInputStream(decodedBytes);
	}

	/**
	 * 
	 * @param img
	 * @return BufferedImage
	 */
	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}
		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
		// Return the buffered image
		return bimage;
	}

	/**
	 * 
	 * @param stream
	 * @return
	 * @throws IOException
	 */
	public static String getFormat(InputStream stream) throws IOException {
		ImageInputStream iis = ImageIO.createImageInputStream(stream);
		Iterator<?> iter = ImageIO.getImageReaders(iis);
		if (!iter.hasNext()) {
			throw new IOException("Unsupported image format!");
		}
		ImageReader reader = (ImageReader) iter.next();
		iis.close();
		return reader.getFormatName().toLowerCase();
	}
	/**
	 * 
	 * @param value
	 * @param parameter
	 * @return
	 * @throws RespuestaException
	 */
	public static int parseInt(String value, String parameter) throws RespuestaException {
		try
		{
			return Integer.parseInt(value);	
		}
		catch(NumberFormatException e)
		{
			throw new RespuestaException("param: " + parameter  + " " + e.getMessage());
		}
	}
	/**
	 * 
	 * @param value
	 * @param parameter
	 * @return
	 * @throws RespuestaException
	 */
	public static double parseDouble(String value, String parameter) throws RespuestaException {
		try
		{
			return Double.parseDouble(value);	
		}
		catch(NumberFormatException e)
		{
			throw new RespuestaException("param: " + parameter  + " " + e.getMessage());
		}
	}
	/**
	 * 
	 * @param urlimg url de la imagen
	 * @param param nombre de la variable
	 * @return Image
	 * @throws RespuestaException
	 */
	public static MiImagen getImageFromUrl(String urlimg, String param) throws RespuestaException
	{
		try 
		{
			URL urlImg = new URL(urlimg);
			InputStream imgIS = copyInputStream(urlImg.openStream());
			String formatName = getFormat(imgIS);
			imgIS.reset();
			BufferedImage image = ImageIO.read(imgIS);
			if (image == null) {
				throw new RespuestaException(param + " no es una imagen válida");
			}
			MiImagen miImagen = new MiImagen(image,formatName);
			return miImagen;
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
	public static MiImagen getImageFromBase64(String content, String param) throws RespuestaException
	{
		try 
		{
			InputStream imgIS = copyInputStream(Utils.getImgFromBase64(content, param));
			String formatName = getFormat(imgIS);
			imgIS.reset();
			BufferedImage image = ImageIO.read(imgIS);
			if (image == null) {
				throw new RespuestaException(param + " no es una imagen válida");
			}
			MiImagen miImagen = new MiImagen(image,formatName);
			return miImagen;
		} 
		catch (IOException e) {
			throw new RespuestaException(e.toString());
		}
	}
	/**
	 * 
	 * @param request
	 * @param param
	 * @return value
	 */
	public static String getParameterRequest(JsonNode request , String param) {
		JsonNode jparam = request.get(param);
		if (jparam != null) {
			return jparam.asText();
		}
		return null;
	}
	/**
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static InputStream copyInputStream(InputStream in) throws IOException
	{
		if (in == null) {
			return null;
		}
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;

		while ((length = in.read(buffer)) != -1) {
			result.write(buffer, 0, length);
		}
		return new ByteArrayInputStream(result.toByteArray());
	}	
}
