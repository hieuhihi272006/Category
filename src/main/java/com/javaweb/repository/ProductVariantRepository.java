package com.javaweb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaweb.model.entity.ProductVariantEntity;

public interface ProductVariantRepository extends JpaRepository<ProductVariantEntity, Long>{
	Optional<ProductVariantEntity> findBySize_IdAndColor_IdAndProduct_ProductCode(Integer sizeId , Integer colorId  , String code);
}
