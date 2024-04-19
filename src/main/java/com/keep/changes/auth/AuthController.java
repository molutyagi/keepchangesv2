package com.keep.changes.auth;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.keep.changes.user.UserDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/auth/")
public class AuthController {

	@Autowired
	private AuthenticationService authenticationService;

	@PostMapping("login")
	public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {

		AuthenticationResponse token = null;
		try {
			token = this.authenticationService.login(authenticationRequest);
		} catch (AuthenticationException e) {
			System.out.println("Exception : " + e);
			e.printStackTrace();
		}

		return ResponseEntity.ok(token);
	}

	@PostMapping("register")
	public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody UserDto userDto) {

		return ResponseEntity.ok(this.authenticationService.register(userDto));
	}

}