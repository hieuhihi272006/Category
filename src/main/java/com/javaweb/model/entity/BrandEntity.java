package com.javaweb.model.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="brand")
public class BrandEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id ;
	
	@Column(name = "name")
	private String name ;
	
	@OneToMany(mappedBy = "brand" , fetch = FetchType.LAZY )
	private List<ProductEntity> listProduct;
}
