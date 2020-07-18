package com.fresco.imagepro.controller;

//creado por Jairo Grateron jgrateron@gmail.com

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController 
{

	@GetMapping("/")
	public String index()
	{
		return "Hello image processing";
	}
}

