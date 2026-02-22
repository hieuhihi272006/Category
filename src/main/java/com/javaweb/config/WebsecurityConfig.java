package com.javaweb.config;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.javaweb.filter.JwtTokenFilter;

import lombok.RequiredArgsConstructor;

@Configuration 
@EnableWebSecurity
@RequiredArgsConstructor
//@EnableWebMvc
//@EnableMethodSecurity(jsr250Enabled = true)
public class WebsecurityConfig{
	
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
									String.format("%s/products", prefix)).permitAll()
							.requestMatchers(POST , 
									String.format("%s/login", prefix)).permitAll()
							.requestMatchers(POST,
									String.format("%s/register", prefix)).permitAll()
							.requestMatchers(GET , 
									String.format("%s/product/**", prefix)).permitAll()
							.requestMatchers(GET,
									String.format("%s/banners", prefix)).permitAll()
							
							.requestMatchers(POST,"/api/pay/momo/ipn").permitAll()
						
							
							.requestMatchers(POST,
									String.format("%s/cart", prefix)).hasAnyRole("USER")
							.requestMatchers(POST, "/api/pay/momo/order").hasAnyRole("USER")
							
							
							.requestMatchers(GET ,
									String.format("%s/test", prefix)).hasAnyRole("USER")
							
							
							.requestMatchers(GET ,
									String.format("%s/sizes", adminPrefix)).hasAnyRole("ADMIN")
							.requestMatchers(POST,
									String.format("%s/products/imports",adminPrefix)).hasAnyRole("ADMIN")
							.requestMatchers(DELETE,
									String.format("%s/product/**", adminPrefix)).hasAnyRole("ADMIN")
							.requestMatchers(POST,
									String.format("%s/banner", adminPrefix)).hasAnyRole("ADMIN")
							.requestMatchers(GET,
									String.format("%s/products", adminPrefix)).hasAnyRole("ADMIN")
							.requestMatchers(PUT,
									String.format("%s/product/**", adminPrefix)).hasAnyRole("ADMIN")
							.requestMatchers(GET,
									String.format("%s/product/**", adminPrefix)).hasAnyRole("ADMIN")
							.requestMatchers(PUT,
									String.format("%s/product/**", adminPrefix)).hasAnyRole("ADMIN")
							.requestMatchers(GET,
									String.format("%s/order", adminPrefix)).hasAnyRole("ADMIN")
							.requestMatchers(DELETE , 
									String.format("%s/variant/**", adminPrefix)).hasAnyRole("ADMIN")
							.requestMatchers(GET , 
									String.format("%s/options", adminPrefix)).hasAnyRole("ADMIN")
							.requestMatchers(PUT , 
									String.format("%s/product", adminPrefix)).hasAnyRole("ADMIN")
							.requestMatchers(POST, 
									String.format("%s/product/import", adminPrefix)).hasAnyRole("ADMIN")
							.requestMatchers(POST, 
									String.format("%s/product/supplier", adminPrefix)).hasAnyRole("ADMIN")
							.anyRequest()
							.authenticated()
						
							;
					
				});
		return http.build();
	}





}
