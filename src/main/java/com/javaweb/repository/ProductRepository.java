package com.javaweb.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.javaweb.model.entity.ProductEntity;


public interface ProductRepository extends JpaRepository<ProductEntity , Long> , JpaSpecificationExecutor<ProductEntity>{
	List<ProductEntity> findByName(String name);
//	Optional<ProductEntity> findByProductCode(String code);
	Optional<ProductEntity> findByProductCodeAndIsDelete(String code , boolean isDelete);
	
	@Query(
		    value = "SELECT * FROM product p WHERE BINARY p.product_code = :code AND p.is_delete = false",
		    nativeQuery = true
		)
	Optional<ProductEntity> findByProductCodeExact(@Param("code") String code);
	
	List<ProductEntity> findAllByIsDelete(boolean isDelete);

}
