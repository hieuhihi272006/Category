package com.javaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaweb.model.entity.BannerEntity;

public interface BannerRepository extends JpaRepository<BannerEntity, Long>{

}
