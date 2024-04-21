package com.keep.changes.auth;

import javax.naming.AuthenticationException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.keep.changes.config.AppConstants;
import com.keep.changes.exception.ApiException;
import com.keep.changes.exception.ResourceNotFoundException;
import com.keep.changes.role.Role;
import com.keep.changes.role.RoleRepository;
import com.keep.changes.security.jwt.JwtService;
import com.keep.changes.user.User;
import com.keep.changes.user.UserDto;
import com.keep.changes.user.UserRepository;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public AuthenticationResponse register(UserDto userDto) {

		User newUser = this.modelMapper.map(userDto, User.class);
		newUser.setPassword(this.passwordEncoder.encode(userDto.getPassword()));

		Role role = this.roleRepository.findById(AppConstants.NORMAL_USER)
				.orElseThrow(() -> new ResourceNotFoundException("Role", "Id", AppConstants.NORMAL_USER));

		System.out.println("role : " + role);
		newUser.getRoles().add(role);

		User savedUser = this.userRepository.save(newUser);

		System.out.println(savedUser);

		String token = this.jwtService.generateToken(savedUser);

		AuthenticationResponse response = new AuthenticationResponse();
		response.setToken(token);

		return response;

	}

	@Override
	public AuthenticationResponse login(AuthenticationRequest userRequest) throws AuthenticationException {

		try {
			this.authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new ApiException("Invalid Password. Enter correct password.", HttpStatus.BAD_REQUEST, false);
		}

		UserDetails userDetails = this.userDetailsService.loadUserByUsername(userRequest.getUsername());

		String token = this.jwtService.generateToken(userDetails);

		AuthenticationResponse response = new AuthenticationResponse();
		response.setToken(token);

		return response;
	}

}
