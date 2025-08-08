package com.javaweb.model.entity;



import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Table(name="import_detail")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImportDetailEntity {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name = "quantity")
	private Integer quantity;
	
	@Column(name="price")
	private BigDecimal price;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "import_id")
	private ImportReceiptEntity importReceipt ;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="supplier_id")
	private SupplierEntity supplier;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_variant_id")
	private ProductVariantEntity variant;
}
