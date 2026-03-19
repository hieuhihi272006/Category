package com.javaweb.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.javaweb.model.entity.OrderBuyEntity;

public interface OrderBuyRepository extends JpaRepository<OrderBuyEntity, Long>{
	List<OrderBuyEntity> findByCreatedAtBetween(LocalDate startDate , LocalDate endDate);
	
	List<OrderBuyEntity> findAllByUser_Id(Integer Id);
}
