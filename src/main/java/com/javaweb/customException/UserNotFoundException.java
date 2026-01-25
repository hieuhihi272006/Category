package com.javaweb.customException;

public class UserNotFoundException extends RuntimeException{
	public UserNotFoundException(String ex) {
		super(ex);
	}
}
