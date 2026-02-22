package com.javaweb.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProductDTO {
	private Integer variantId;
	private Integer quantity;
	private Long price;
}
