package com.fresco.imagepro.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.fresco.imagepro.exception.RespuestaException;

public class Utils {

	public static String writeImageToBytes(int width, int height, Image image) throws IOException {
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Graphics2D g = bi.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		ImageIO.write(bi, "jpg", baos);
		baos.close();
		bi = null;
		g = null;
		byte[] encoded = Base64.getEncoder().encode(baos.toByteArray());
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
		return reader.getFormatName();
	}
	
	public static int parseInt(String value, String parameter) throws RespuestaException {
		try
		{
			int res = Integer.parseInt(value);	
			return res;
		}
		catch(NumberFormatException e)
		{
			throw new RespuestaException("param: " + parameter  + " " + e.getMessage());
		}
	}
}
