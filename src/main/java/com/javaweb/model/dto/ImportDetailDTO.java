package com.javaweb.model.dto;

import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class ImportDetailDTO {
	
	@NotBlank(message="Product Code is required")
	private String productCode;
	
	@NotBlank(message="Name product is required")
	private String name;
	
	private Integer supplierId;
	
	private List<ImportVariantDTO> importVariant;
}
