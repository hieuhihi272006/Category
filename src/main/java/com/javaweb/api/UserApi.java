package com.javaweb.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.model.dto.UserDTO;
import com.javaweb.model.dto.UserLoginDTO;
import com.javaweb.model.entity.UserEntity;
import com.javaweb.model.response.CartResponse;
import com.javaweb.model.response.InformationUserResponse;
import com.javaweb.model.response.OrderBuyResponse;
import com.javaweb.service.UserService;

import jakarta.validation.Valid;

@RestController
//@CrossOrigin("http://localhost:5175")
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
			return ResponseEntity.status(HttpStatus.CREATED).body("Accound created successfully");
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PostMapping(value = "/login")
	public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginDTO userLogin , BindingResult result ){
		try {
			if(result.hasErrors()) {
				List<String> errors = result.getFieldErrors().stream().map(FieldError :: getDefaultMessage).toList();
				return ResponseEntity.badRequest().body(errors);
			}
			String token = userService.loginUser(userLogin);
			return ResponseEntity.ok(token);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	
	@GetMapping(value = "/profile")
	public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserEntity user){
		InformationUserResponse infomation = userService.getProfile(user);
		return ResponseEntity.ok(infomation);
	}
	
	@GetMapping(value = "/order")
	public ResponseEntity<?> getOrder(@AuthenticationPrincipal UserEntity user){
		List<OrderBuyResponse> orderBuyResponses = userService.getOrder(user);
		return ResponseEntity.ok(orderBuyResponses);
	}
	
	@GetMapping(value = "/cart")
	public ResponseEntity<?> getCard(@AuthenticationPrincipal UserEntity user){
		List<CartResponse> cartResponses = userService.getCard(user);
		return ResponseEntity.ok(cartResponses);
	}
	
	@DeleteMapping(value = "cart/{id}")
	public ResponseEntity<?> deleteCart(@PathVariable Long id ){
		userService.deleteCart(id);
		return ResponseEntity.ok("");
	}
}
