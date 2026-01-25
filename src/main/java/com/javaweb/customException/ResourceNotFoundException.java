package com.javaweb.customException;

public class ResourceNotFoundException extends RuntimeException{
	public ResourceNotFoundException(String ex) {
		super(ex);
		}
}
