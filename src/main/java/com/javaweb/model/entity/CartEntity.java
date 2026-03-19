package com.javaweb.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Table(name="cart")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "user_id",referencedColumnName = "id")
	private UserEntity user;
	
	@Builder.Default
	@OneToMany(mappedBy = "cart" , fetch = FetchType.LAZY ,cascade =  {CascadeType.PERSIST , CascadeType.MERGE , CascadeType.REMOVE} , orphanRemoval = true )
	private List<CartItemEntity> cartItems = new ArrayList<>();
}
