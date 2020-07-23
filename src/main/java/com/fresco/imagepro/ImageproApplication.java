package com.fresco.imagepro;



//creado por Jairo Grateron jgrateron@gmail.com

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.fresco")
@SpringBootApplication
public class ImageproApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImageproApplication.class, args);
	}

}
