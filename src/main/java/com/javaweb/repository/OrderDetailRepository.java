package com.javaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaweb.model.entity.OrderDetailEntity;

public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Long>{

}
