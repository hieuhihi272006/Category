package com.javaweb.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.javaweb.model.entity.ProductEntity;

import jakarta.persistence.criteria.Predicate;

public interface ProductRepository extends JpaRepository<ProductEntity , Long> , JpaSpecificationExecutor<ProductEntity>{
	List<ProductEntity> findByName(String name);
	Optional<ProductEntity> findByProductCode(String code);
	
}
