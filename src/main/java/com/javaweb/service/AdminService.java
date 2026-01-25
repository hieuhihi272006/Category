package com.javaweb.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.javaweb.model.dto.ImportDetailDTO;
import com.javaweb.model.response.ImportReceiptResponse;
import com.javaweb.model.response.OrderBuyResponse;
import com.javaweb.model.response.ProductResponse;

public interface AdminService {
	Map<String,Object> showOptional();
	void addProducts(ImportDetailDTO importDTO);
	List<ImportReceiptResponse> historyImport(LocalDate startDate , LocalDate endDate);
	void editProduct(ProductResponse product);
	List<OrderBuyResponse> historyOrder(LocalDate startDate , LocalDate endDate);
	List<ProductResponse> search(String keyword);
	void deleteProduct(Long id);
	ProductResponse searchByCode(String code);
}
