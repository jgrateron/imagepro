package com.fresco.imagepro;

import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;

//creado por Jairo Grateron jgrateron@gmail.com

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.fresco")
@SpringBootApplication
public class ImageproApplication {

	/**
	 * inicia los perfiles de colores
	 */
	public static void init ()
	{
        ICC_Profile.getInstance(ColorSpace.CS_sRGB).getData();
        ICC_Profile.getInstance(ColorSpace.CS_PYCC).getData();
        ICC_Profile.getInstance(ColorSpace.CS_GRAY).getData();
        ICC_Profile.getInstance(ColorSpace.CS_CIEXYZ).getData();
        ICC_Profile.getInstance(ColorSpace.CS_LINEAR_RGB).getData();		
	}
	public static void main(String[] args) {
		init();
		SpringApplication.run(ImageproApplication.class, args);
	}

}
