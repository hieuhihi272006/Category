package com.javaweb.model.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.JoinColumn;

@Getter
@Setter
@Table(name="account")
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements UserDetails {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "fullname")
	private String name ;
	
	@Column(name="phonenumber")
	private String phoneNumber;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "status")
	private Integer status;
	
	@Column(name = "created_at")
	@CreatedDate
	private LocalDate createdAt;
	
	@Column(name="email")
	private String email;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "account_role" , 
									joinColumns = @JoinColumn(name = "account_id" , nullable = false),
									inverseJoinColumns = @JoinColumn(name = "role_id" , nullable = false))
	private List<RoleEntity> roles = new ArrayList<>();

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
		for(RoleEntity it : getRoles() ) {
			if(it.getCode() != null) {
				authorityList.add(new SimpleGrantedAuthority("ROLE_" + it.getCode()));
			}
		}
		return authorityList;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return phoneNumber;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return this.status != 0;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	
}
