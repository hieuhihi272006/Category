package com.javaweb.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.javaweb.model.entity.RoleEntity;
import com.javaweb.model.entity.UserEntity;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
	
	@Value("${jwt.expiration}")
	private Long expiration;
	@Value("${jwt.secretKey}")
	private String secretKey;
	
	private Key getSingInKey() {
		byte[] bytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(bytes);
	}
	
	public String generateToken(UserEntity user) throws Exception{
		Map<String,Object> claims = new HashMap<>();
		claims.put("phoneNumber", user.getPhoneNumber());
		List<String> roles = new ArrayList<>();
		for(RoleEntity it : user.getRoles()) {
			if(it.getCode() != null) {
				roles.add(it.getCode());
			}
		}
		claims.put("roles" , roles);
		try {
			String token = Jwts.builder()
								.setClaims(claims)
								.setSubject(user.getPhoneNumber())
								.setExpiration(new Date(System.currentTimeMillis() + expiration*1000L))
								.signWith(getSingInKey() , SignatureAlgorithm.HS256)
								.compact();
			return token;
		}catch(Exception e) {
			throw new Exception("Cannot create jwt token , error " + e.getMessage());
		}
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
					.setSigningKey(getSingInKey())
					.build()
					.parseClaimsJws(token)
					.getBody();
	}
	
	public <T> T extractClaim(String token , Function<Claims , T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	public String extractPhoneNumber(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public boolean isTokenExpired(String token) {
		Date expirationDate = extractClaim(token, Claims :: getExpiration);
		return expirationDate.before(new Date());
		
	}
	public boolean valtdateToken(String token , UserDetails userDetail) {
		String phoneNumber = extractPhoneNumber(token);
		return (phoneNumber.equals(userDetail.getUsername()) && !isTokenExpired(token));	
	}
	
	public List<String> extractRoles(String token){
		Claims claim = extractAllClaims(token);
		Object roles = claim.get("roles");
		if(roles instanceof List<?>) {
			return ((List<?>) roles).stream()
					.map(Object :: toString)
					.collect(Collectors.toList());
		}
		return new ArrayList<>();
	}
}
