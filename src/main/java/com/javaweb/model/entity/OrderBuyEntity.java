package com.javaweb.model.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="order_form")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class OrderBuyEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id ;
	
	@Column(name = "code")
	private String code;
	
	@Column(name="status")
	private String status;
	
	@Column(name="payment_method")
	private String paymentMethod;
	
	@Column(name="note")
	private String note;
	
	@Column(name="price")
	private Long price;
	
	@CreatedDate
	@Column(name = "created_at")
	private LocalDate createdAt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id")
	 @JsonIgnore  
	private UserEntity user;
	
	@Builder.Default
	@OneToMany(mappedBy = "order" , fetch = FetchType.LAZY ,cascade =  {CascadeType.PERSIST , CascadeType.MERGE , CascadeType.REMOVE} , orphanRemoval = true )
	private List<OrderDetailEntity> orderDetail = new ArrayList<>();
}
