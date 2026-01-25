package com.javaweb.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "banner")
@Entity
@Getter
@Setter
public class BannerEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name="title")
	private String title;
	
	@Column(name = "image_url")
	private String imageUrl;
	@Column(name = "link_url")
	private String linkUrl;
}
