package com.fresco.imagepro.controller;

public class Response 
{
	private boolean success;
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public Response(boolean success) {
		super();
		this.success = success;
	}
}
