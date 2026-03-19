package com.javaweb.model.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartResponse {
	private Long id;
	private String image;
	private String name;
	private Long price;
	private Integer quantity;
	private String color;
	private String size;
	private Long idProduct;
}
