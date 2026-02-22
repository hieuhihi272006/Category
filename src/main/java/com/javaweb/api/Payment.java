package com.javaweb.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.javaweb.model.dto.OrderBuyDTO;
import com.javaweb.model.entity.UserEntity;
import com.javaweb.service.PaymentService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/api/pay/momo")
public class Payment {

	@Autowired
	private PaymentService paymantService;
	
	@PostMapping(value = "/order")
	public ResponseEntity<?> orderProduct(@Valid @RequestBody OrderBuyDTO orderBuyDTO,
													  BindingResult result,
													  @AuthenticationPrincipal UserEntity user){
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(FieldError :: getDefaultMessage).toList();
			return ResponseEntity.badRequest().body(errors);
		}
		Map<String , Object> mapResult = paymantService.orderProduct(orderBuyDTO, user.getId());
		return ResponseEntity.ok(mapResult);
	}
	
	
	@PostMapping(value = "/ipn")
	public ResponseEntity<String> momoIpn(@RequestBody Map<String,Object> payload){
		return ResponseEntity.ok("OK");
	}
}
