package com.javaweb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.javaweb.model.entity.ProductVariantEntity;

import jakarta.persistence.LockModeType;

public interface ProductVariantRepository extends JpaRepository<ProductVariantEntity, Long>{
	Optional<ProductVariantEntity> findBySize_IdAndColor_IdAndProduct_ProductCode(Integer sizeId , Integer colorId  , String code);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select p from ProductVariantEntity p where p.id = :id")
	ProductVariantEntity findByIdForUpdate(Long id);
}
