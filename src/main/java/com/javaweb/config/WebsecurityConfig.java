package com.javaweb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import com.javaweb.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import static org.springframework.http.HttpMethod.*;

@Configuration 
@EnableWebSecurity
@RequiredArgsConstructor
@EnableWebMvc
public class WebsecurityConfig {
	
	private final JwtTokenFilter jwtTokenFilter;
	@Value("${api.prefix}")
	private String prefix;
	@Value("${admin.api.prefix}")
	private String adminPrefix;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http
				.csrf(AbstractHttpConfigurer :: disable)
				.addFilterBefore(jwtTokenFilter,UsernamePasswordAuthenticationFilter.class)
				.authorizeHttpRequests(request -> {
					request
							.requestMatchers(GET ,
									String.format("%s/search", prefix)).permitAll()
							.requestMatchers(POST , 
									String.format("%s/login", prefix)).permitAll()
							.requestMatchers(GET ,
									String.format("%s/show_size", adminPrefix)).hasAnyRole("ADMIN").anyRequest().authenticated();		
				});
		return http.build();
	}
}
