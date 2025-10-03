package com.javaweb.model.response;

import java.math.BigDecimal;
import java.util.Map;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductVariantResponse {
	private Long id ;
	@NotNull(message="Price is required")
	private BigDecimal price ;
	@NotNull(message="Quantity is required")
	private Integer quantity;
	private String image;
	private Map<String,Object> colors;
	private Map<String,Object> sizes;
	
}
