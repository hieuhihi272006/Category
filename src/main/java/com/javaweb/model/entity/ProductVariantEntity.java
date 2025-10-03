package com.javaweb.model.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="product_variant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="quantity")
	private Integer quantity;
	
	@Column(name="price")
	private BigDecimal price;
	
	@Column(name="image_url")
	private String image;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="product_id")
	private ProductEntity product;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="size_id")
	private SizeEntity size;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="color_id")
	private ColorEntity color;
	
	@Builder.Default
	@OneToMany(mappedBy="variant" , fetch =FetchType.LAZY ,cascade =  {CascadeType.PERSIST , CascadeType.MERGE , CascadeType.REMOVE} , orphanRemoval = true )
	private List<ImportDetailEntity> importDetails = new ArrayList<>();
	

}
