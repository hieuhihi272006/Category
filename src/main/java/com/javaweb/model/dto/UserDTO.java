package com.javaweb.model.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
public class UserDTO {
	
	@NotBlank(message = "UserName can not be blank")
	private String name ;
	
	@NotBlank(message = "Phone number can not be blank")
	private String phoneNumber;	
	
	@NotBlank(message = "Password can not be blank")
	private String password;
	
	@NotBlank(message = "Address can not be blank")
	private String address;
	
	@NotBlank(message = "Retype Password can not be blank")
	private String retypePassword;
	private String email;
}
