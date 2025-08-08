package com.javaweb.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.javaweb.model.entity.ColorEntity;

public interface ColorRepository extends JpaRepository<ColorEntity, Long>{
	List<ColorEntity> findAll();
	boolean existsByName(String name);
	ColorEntity findById(Integer id);
}
