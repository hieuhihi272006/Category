package com.javaweb.model.response;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductResponse {
	
	@NotNull(message = "Product ID is required")
	private Long id ;
	private String code;
	@NotBlank(message = "Product name is required")
	private String name ;
	private String description ;

	@NotNull(message="Quantity is required")
	private Integer quantity;
	private Date created_at;
	
	@NotNull(message = "Brand is required")
	private Integer brandId;
	private String brandName;
	private String image;
	
	private Long minPrice;
	
	private Long originalPrice;
	
	private Integer discountPercent;
	
	private Integer totalQuantity;
	
	private boolean isDelete;
	
	@Valid
	private List<ProductVariantResponse> variants;
	
}
