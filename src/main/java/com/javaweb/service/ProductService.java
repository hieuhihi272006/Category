package com.javaweb.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.javaweb.model.dto.ImportDetailDTO;
import com.javaweb.model.response.FormResponse;
import com.javaweb.model.response.ImportDetailResponse;
import com.javaweb.model.response.ProductResponse;

public interface ProductService {
	FormResponse search(Map<String,Object> map , List<Integer> brands);
	ProductResponse detailProduct(Long id);
	Map<String,Object> showSize(String code);
	Map<String,Object> showColorAndSupplierAndBrand();
	void addProducts(List<ImportDetailDTO> importDTO);
	List<ImportDetailResponse> historyImport(LocalDate startDate , LocalDate endDate);
	void editProduct(ProductResponse product);
	

}
