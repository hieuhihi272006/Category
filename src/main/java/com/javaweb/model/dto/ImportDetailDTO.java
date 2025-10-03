package com.javaweb.model.dto;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.EntityListeners;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class ImportDetailDTO {
	
	@JsonProperty("product_code")
	private String productCode;
		
	@NotBlank(message="Name product is required")
	private String name;
	
	private Integer colorId;
	
	private Integer sizeId;
	
	@NotNull(message="Quantity product is required")
	private Integer quantity;
	
	@NotNull(message="Price product is required")
	private BigDecimal price;
	
	private Integer supplierId;
}
