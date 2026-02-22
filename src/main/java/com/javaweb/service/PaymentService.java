package com.javaweb.service;

import java.util.Map;

import com.javaweb.model.dto.OrderBuyDTO;

public interface PaymentService {
	Map<String,Object> orderProduct(OrderBuyDTO orderBuyDTO , Integer userId);
}
