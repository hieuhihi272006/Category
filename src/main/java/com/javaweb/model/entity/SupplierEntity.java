package com.javaweb.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name="supplier")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class SupplierEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "name")
	@NotBlank(message = "Name is required")
	private String name;
	
	@Column(name="phone")
	@NotBlank(message = "PhoneNumber is required")
	private String phone;
	
	@Column(name="email")
	private String email;
	
	@Column(name="address")
	@NotBlank(message = "address is required")
	private String address;
	
	@Column(name = "created_at")
    @CreatedDate
    private LocalDateTime creatddate ;
	
	
	@OneToMany(mappedBy="supplier", fetch = FetchType.LAZY)
	private List<ImportDetailEntity> importDetails = new ArrayList<>();
}
