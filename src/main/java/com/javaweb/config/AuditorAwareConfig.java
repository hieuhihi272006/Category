package com.javaweb.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.javaweb.model.entity.UserEntity;
import com.javaweb.repository.UserRepository;

@Component
public class AuditorAwareConfig implements AuditorAware<String>{
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public Optional<String> getCurrentAuditor(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserEntity user = userRepository.findByPhoneNumber(auth.getName()).get();
		
		return Optional.of(user.getName());
	}
}
