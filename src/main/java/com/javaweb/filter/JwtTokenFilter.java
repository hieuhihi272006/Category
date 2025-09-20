package com.javaweb.filter;

import java.io.IOException;import java.net.Authenticator;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.javaweb.model.entity.UserEntity;
import com.javaweb.utils.JwtTokenUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtTokenFilter extends OncePerRequestFilter{

	@Value("${api.prefix}")
	private String prefix;
	
	private final JwtTokenUtil jwtTokenUtil;
	private final UserDetailsService userDetailsService;
	
	public boolean isByPassToken(@NotNull HttpServletRequest request) {
		final List<Pair<String,String>> byPassTokens = Arrays.asList(
				Pair.of(String.format("%s/login", prefix), "POST"),
				Pair.of(String.format("%s/register", prefix), "POST"),
				Pair.of(String.format("%s/search", prefix), "GET"),
				Pair.of(String.format("%s/detailProduct", prefix),"GET")
				
				);
		for(Pair<String,String> byPassToken : byPassTokens) {
			if((request.getServletPath().contains(byPassToken.getFirst())) && (request.getMethod().equals(byPassToken.getSecond()))) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected void doFilterInternal(@NotNull HttpServletRequest request, 
			                        @NotNull HttpServletResponse response, 
			                        @NotNull FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			if(isByPassToken(request)) {
				filterChain.doFilter(request, response);
				return ;
			}
			final String authHeader = request.getHeader("Authorization");
			if(authHeader == null || !authHeader.startsWith("Bearer ")) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED , "Unauthorised");
//				filterChain.doFilter(request, response);
				return;
			}	
			final String token = authHeader.substring(7);
			final String phonNumber = jwtTokenUtil.extractPhoneNumber(token);
			if(phonNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserEntity userDetail = (UserEntity) userDetailsService.loadUserByUsername(phonNumber);
				if(jwtTokenUtil.valtdateToken(token, userDetail)) {
					List<String> roles = jwtTokenUtil.extractRoles(token);
					List<GrantedAuthority> authorities = roles.stream().map(item -> new SimpleGrantedAuthority("ROLE_" + item)).collect(Collectors.toList());
					UsernamePasswordAuthenticationToken authencation = new UsernamePasswordAuthenticationToken(userDetail, null , authorities);
					authencation.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authencation);
				}
			}
			filterChain.doFilter(request, response);
		}catch(Exception e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED , "Unauthorized");
		}
		
	}
	
}
