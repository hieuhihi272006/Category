package com.javaweb.repository;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.javaweb.model.entity.BrandEntity;

public interface BrandRepository extends JpaRepository<BrandEntity , Long> , JpaSpecificationExecutor<BrandEntity>{
	List<BrandEntity> findByIdIn(Set<Integer> setBrandId);
	boolean existsByName(String name);
	BrandEntity findById(Integer id);
}
