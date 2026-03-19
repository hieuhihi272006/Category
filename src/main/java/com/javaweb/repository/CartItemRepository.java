package com.javaweb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaweb.model.entity.CartItemEntity;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long>{
	Optional<CartItemEntity> findByCart_IdAndVariant_Id(Integer cartId , Integer variantId);
//	void deleteById(Long id);
	
}
