package com.javaweb.model.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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
public class ImportReceiptResponse {
	private String code ;
	private LocalDate date;
	private String supplierName;
	private String name;
	private Integer quantity;
	private BigDecimal totalPrice;
	private List<ImportDetailResponse> listImportDetail;
}
