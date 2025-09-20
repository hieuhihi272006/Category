package com.javaweb.model.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

@Setter
@Getter
@Entity
@Table(name="product")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id ;
	@Column(name = "name")
	private String name ;
	@Column(name = "description")
	private String description;
	@Column(name = "rating")
	private Double rating;
	@Column(name = "price")
	private BigDecimal price;
	@Column(name = "quantity")
	private Integer quantity;
	@Column(name = "created_at")
	private Date created_at;
	@Column(name="discount")
	private Integer discount;
	@Column(name="product_code")
	private String productCode;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "brand_id")
	private BrandEntity brand;
	
	@Builder.Default
	@OneToMany(mappedBy = "product" , fetch = FetchType.LAZY ,cascade =  {CascadeType.PERSIST , CascadeType.MERGE , CascadeType.REMOVE} , orphanRemoval = true )
	private List<ProductVariantEntity> productVariants = new ArrayList<>();
	
}
