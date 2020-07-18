package com.fresco.imagepro.exception;

//creado por Jairo Grateron jgrateron@gmail.com

public class RespuestaException extends Exception{

	private static final long serialVersionUID = 1L;

	private String message;

	public RespuestaException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
