package com.fresco.imagepro.utils;

import java.awt.image.BufferedImage;

public class MiImagen {

	private BufferedImage image;
	private String formatName;
	
	
	public MiImagen(BufferedImage image, String formatName) {
		super();
		this.image = image;
		this.formatName = formatName;
	}
	public BufferedImage getImage() {
		return image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	public String getFormatName() {
		return formatName;
	}
	public void setFormatName(String formatName) {
		this.formatName = formatName;
	}
	
	
}
