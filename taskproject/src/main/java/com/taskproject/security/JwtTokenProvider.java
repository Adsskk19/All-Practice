package com.taskproject.security;

import java.sql.Date;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.stereotype.Component;

import com.taskproject.exception.APIException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {
	
	public String generateToken(org.springframework.security.core.Authentication authentication) {
		String email = authentication.getName();
		Date currentDate = new Date(0);
		Date expireDate = new Date(currentDate.getTime()+3600000);//milliseconds
		
		String token = Jwts.builder()
				.setSubject(email)
				.setIssuedAt(currentDate)
				.setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS512,"JWTSecretKey")
				.compact();
			return token;	
	}
	
	public String getEmailFromToken(String token) {
		@SuppressWarnings("deprecation")
		Claims claims = Jwts.parser().setSigningKey("JWTSecretKey")
		.parseClaimsJws(token).getBody();
		
		return claims.getSubject();
	}
	
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey("JWTSecretKey")
			.parseClaimsJws(token)
			return true;
		}
		catch(Exception e) {
			throw new APIException("Token issue: "+e.getMessage());
		}
		
	}
	
	
	

}
