package com.javaweb.model.builder;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class ProductBuilder {
	private String name ;
	private Integer rating ;
	private Long priceTo;
	private Long priceFrom;
	private String code;
	private List<Integer> brands;
}
