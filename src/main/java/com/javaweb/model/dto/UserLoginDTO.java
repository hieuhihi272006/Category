package com.javaweb.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDTO {
	@NotBlank(message = "PhoneNumber is required")
	private String phoneNumber;
	
	@NotBlank(message = "Password is required")
	private String password;
}
