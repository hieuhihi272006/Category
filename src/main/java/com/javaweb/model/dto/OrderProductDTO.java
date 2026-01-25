package com.javaweb.model.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProductDTO {
	private Integer variantId;
	private Integer quantity;
	private BigDecimal price;
}
