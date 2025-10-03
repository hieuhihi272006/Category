package com.javaweb.model.response;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductResponse {
	private Long id ;
	private String code;
	private String name ;
	private String description ;
	@NotNull(message="Price is required")
	private BigDecimal price;
	private BigDecimal discount_price;
	@NotNull(message="Quantity is required")
	private Integer quantity;
	private Date created_at;
	@NotNull(message="Discount is required")
	private Integer discount;
	private Integer brandId;
	private String brandName;
	private String image;
	@Valid
	private List<ProductVariantResponse> variants;
}
