package com.example.jwtsample.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwtsample.models.AuthenticationRequest;
import com.example.jwtsample.models.AuthenticationResponse;
import com.example.jwtsample.security.JwtUtils;

@RestController
public class JwtSampleController {
	
	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired 
	UserDetailsService userDetailsService;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@GetMapping(value = "/hello")
	public ResponseEntity<String> sayHello() {
		return new ResponseEntity<String>("Hello World", HttpStatus.OK);
	}

	@PostMapping(value = "/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) 
	throws Exception{
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken( 
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		}
		catch(BadCredentialsException e) {
			throw new Exception("Username or password invalid!");
		}
		
		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String jwt = jwtUtils.generateToken(userDetails);
		AuthenticationResponse authResponse = new AuthenticationResponse();
		authResponse.setJwt(jwt);
		
		return new ResponseEntity<AuthenticationResponse>(authResponse, HttpStatus.OK);
	}
	
}


