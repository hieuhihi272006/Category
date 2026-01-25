package com.javaweb.model.builder;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductBuilder {
	private String name ;
	private Integer rating ;
	private Long priceTo;
	private Long priceFrom;
	private String code;
	private List<Integer> brands;
}
