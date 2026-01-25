package com.javaweb.model.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderBuyDTO {
	
	@NotNull(message="Cart_item null")
	@NotEmpty
	private List<Long> idCartItems;
	private String note;
	@NotBlank(message="Payment Method is required")
	private String paymentMethod;
	
}
