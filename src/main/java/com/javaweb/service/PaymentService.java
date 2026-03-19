package com.javaweb.service;

import java.util.List;
import java.util.Map;

 

import com.javaweb.model.dto.BuyDTO;

public interface PaymentService {
	Map<String,Object> buyProducts(List<BuyDTO> buyDTO);
}
