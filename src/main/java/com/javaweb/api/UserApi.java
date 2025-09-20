package com.javaweb.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.javaweb.model.dto.UserDTO;
import com.javaweb.model.dto.UserLoginDTO;
import com.javaweb.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/grocery")
public class UserApi {
	@Autowired
	private UserService userService;
	
	@PostMapping(value = "/register")
	public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO , BindingResult result){
		try {
			if(result.hasErrors()) {
				List<String> errors = result.getFieldErrors().stream().map(FieldError :: getDefaultMessage).toList();
				return ResponseEntity.badRequest().body(errors);
			}
			if(!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
				return ResponseEntity.badRequest().body("Password not match");
			}
			userService.register(userDTO);
			return ResponseEntity.ok("Create account access");
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PostMapping(value = "/login")
	public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginDTO userLogin){
		try {
			String token = userService.loginUser(userLogin);
			return ResponseEntity.ok(token);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
