package com.javaweb.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.javaweb.model.dto.BuyDTO;
import com.javaweb.service.PaymentService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/api/pay/momo")
public class Payment {

	@Autowired
	private PaymentService paymantService;
	
	 
	@PostMapping("/create")
	public ResponseEntity<?> buyProducts(@Valid @RequestBody  List<BuyDTO> buyDTO, BindingResult result){
		try {
			if(result.hasErrors()) {
				List<String> err = result.getFieldErrors().stream().map(FieldError :: getDefaultMessage).toList();
				return ResponseEntity.badRequest().body(err);
			}
			Map<String,Object> cnt =  paymantService.buyProducts(buyDTO);
			return ResponseEntity.ok(cnt);
		}catch(Exception ex) {
			return ResponseEntity.badRequest().body(ex);
		}
		
	}
	
	
	@PostMapping("/ipn")
	public ResponseEntity<String> momoIpn(@RequestBody Map<String,Object> payload){

	    System.out.println("IPN RECEIVED: " + payload);

	    return ResponseEntity.ok("OK");
	}
}
