package com.javaweb.model.response;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderBuyResponse{
	
	private String code;
	private String status;
	private LocalDate createdAt;
	private String paymentMethod;
	private Long price;

}
