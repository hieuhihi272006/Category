package com.javaweb.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.javaweb.model.entity.SupplierEntity;

public interface SupplierRepository extends JpaRepository<SupplierEntity,Long>{
	SupplierEntity findById(Integer id);
}
