package com.javaweb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaweb.model.entity.CartEntity;

public interface CartRepository extends JpaRepository<CartEntity, Long>{
	Optional<CartEntity> findByUser_Id(Integer id);
}
