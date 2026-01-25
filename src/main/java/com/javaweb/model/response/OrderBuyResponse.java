package com.javaweb.model.response;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderBuyResponse{
	
	private String code;
	private String status;
	private String paymentMethod;
	private String note;
	private BigDecimal price;
	private String address;
	private String nameUser;
	private String numberPhone;
}
