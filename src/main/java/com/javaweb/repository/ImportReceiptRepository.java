package com.javaweb.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaweb.model.entity.ImportReceiptEntity;

public interface ImportReceiptRepository extends JpaRepository<ImportReceiptEntity, Long>{
	List<ImportReceiptEntity> findByCreatedAtBetween(LocalDate startDate,LocalDate endDate);
}
