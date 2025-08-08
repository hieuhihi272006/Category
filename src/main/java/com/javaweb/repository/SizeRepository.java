package com.javaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaweb.model.entity.SizeEntity;

public interface SizeRepository extends JpaRepository<SizeEntity, Long>{
	boolean existsByName(String name);
	SizeEntity findById(Integer id);
}
