package com.javaweb.model.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FormResponse {
	private List<ProductResponse> listProduct;
	private List<BrandResponse> listBrands;
}
