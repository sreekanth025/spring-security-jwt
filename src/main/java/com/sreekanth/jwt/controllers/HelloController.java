package com.sreekanth.jwt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sreekanth.jwt.models.AuthenticationRequest;
import com.sreekanth.jwt.models.AuthenticationResponse;
import com.sreekanth.jwt.services.MyUserDetailsService;
import com.sreekanth.jwt.utils.JwtUtil;

@RestController
public class HelloController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	
	@RequestMapping("/hello") 
	public String hello() {
		return "Hello Sreekanth";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
		
		try {
			authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
			);
		}
		catch(BadCredentialsException e) {
//			throw new Exception("Incorrect Username or Password ", e);
			System.out.println("Incorrect Username or Password");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername()); 
		final String jwt = jwtUtil.generateToken(userDetails); 
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
}
