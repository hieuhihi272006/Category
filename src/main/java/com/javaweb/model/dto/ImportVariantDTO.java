package com.javaweb.model.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ImportVariantDTO {

	private Integer colorId;
	
	private Integer sizeId;
	
	@NotNull(message="Quantity product is required")
	private Integer quantity;
	
	@NotNull(message="Price product is required")
	private BigDecimal price;
	
}
