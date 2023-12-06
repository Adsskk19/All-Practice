package com.taskproject.controller;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskproject.payload.JWTAuthResponse;
import com.taskproject.payload.LoginDto;
import com.taskproject.payload.UserDto;
import com.taskproject.security.JwtTokenProvider;
import com.taskproject.service.UserService;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	//post to store the user in DB
	@PostMapping("/register")
	public ResponseEntity<UserDto>  createUser(@RequestBody UserDto userDto) {
		return new ResponseEntity<>(userService.createUser(userDto),HttpStatus.OK);	
	}
	
	@PostMapping("/login")
	public ResponseEntity<JWTAuthResponse> loginUser(@RequestBody LoginDto loginDto){
		org.springframework.security.core.Authentication authentication =
				authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
						);
		System.out.println(authentication);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = jwtTokenProvider.generateToken(authentication);//get the token
		
		return  ResponseEntity.ok(new JWTAuthResponse(token));
				//ResponseEntity<>(token,HttpStatus.OK);
		
	}

}
