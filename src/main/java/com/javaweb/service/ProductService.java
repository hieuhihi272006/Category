package com.javaweb.service;

import java.util.List;
import java.util.Map;

import com.javaweb.model.response.FormResponse;
import com.javaweb.model.response.ProductResponse;

public interface ProductService {
	FormResponse search(Map<String,Object> map , List<Integer> brands) throws Exception;

	ProductResponse detailProduct(Long id);
	
	String test(Long id) throws Exception;
}
