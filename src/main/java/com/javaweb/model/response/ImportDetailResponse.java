package com.javaweb.model.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImportDetailResponse {
	private String name ;
	private Integer quantity;
	private BigDecimal price;
	private String colorName;
	private String sizeName;
	private String supplierName;
}
