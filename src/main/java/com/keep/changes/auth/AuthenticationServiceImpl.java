package com.keep.changes.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.naming.AuthenticationException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keep.changes.config.AppConstants;
import com.keep.changes.exception.ApiException;
import com.keep.changes.exception.ResourceAlreadyExistsException;
import com.keep.changes.exception.ResourceNotFoundException;
import com.keep.changes.role.Role;
import com.keep.changes.role.RoleRepository;
import com.keep.changes.security.jwt.JwtService;
import com.keep.changes.user.User;
import com.keep.changes.user.UserDto;
import com.keep.changes.user.UserRepository;
import com.keep.changes.user.token.Token;
import com.keep.changes.user.token.TokenRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@Service
@Transactional
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
	private TokenRepository tokenRepository;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

//	private final int accessCookieMaxAge = 2 * 60;
//	private final int refreshCookieMaxAge = 5 * 60;

	@Override
	public AuthenticationResponse register(UserDto userDto) {

		Optional<User> userWithEmail = this.userRepository.findByEmail(userDto.getEmail());
		if (userWithEmail.isPresent()) {
			throw new ResourceAlreadyExistsException("User", "Email", userDto.getEmail());
		}

		Optional<User> userWithPhone = this.userRepository.findByPhone(userDto.getPhone());
		if (userWithPhone.isPresent()) {
			throw new ResourceAlreadyExistsException("User", "Phone", userDto.getPhone());
		}

		Token token = this.tokenRepository.findLatestTokenByEmail(userDto.getEmail()).orElseThrow(
				() -> new ApiException("You have not verified your email yet.", HttpStatus.UNAUTHORIZED, false));

		if (token.getVerified() == false) {
			throw new ApiException("You have not verified your email yet.", HttpStatus.UNAUTHORIZED, false);
		}

		User newUser = this.modelMapper.map(userDto, User.class);
		newUser.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
		newUser.setEmail(newUser.getEmail().toLowerCase());

		Role role = this.roleRepository.findById(AppConstants.NORMAL_USER)
				.orElseThrow(() -> new ResourceNotFoundException("Role", "Id", AppConstants.NORMAL_USER));

		newUser.getRoles().add(role);
		newUser.setIsEnabled(true);

//		sendValidationEmail(newUser);

		User savedUser = this.userRepository.save(newUser);

		String accessToken = this.jwtService.generateAccessToken(savedUser);
		String refreshToken = this.jwtService.generateRefreshToken(savedUser);

		AuthenticationResponse response = new AuthenticationResponse();
		response.setAccessToken(accessToken);
		response.setRefreshToken(refreshToken);

		return response;

	}

	@Override
	public AuthenticationResponse login(AuthenticationRequest userRequest) throws AuthenticationException {

		try {
			System.out.println("1 error here");

			this.authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));
			System.out.println(" 2 error here");
		} catch (BadCredentialsException | InternalAuthenticationServiceException e) {
			throw new ApiException("Incorrect User Credentials. Kindly enter correct credentials.",
					HttpStatus.UNAUTHORIZED, false);
		}
		System.out.println("bfore user details");
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(userRequest.getUsername());

		String accessToken = this.jwtService.generateAccessToken(userDetails);
		String refreshToken = this.jwtService.generateRefreshToken(userDetails);

		AuthenticationResponse response = new AuthenticationResponse();
		response.setAccessToken(accessToken);
		response.setRefreshToken(refreshToken);

		System.out.println(response);
		return response;
	}

	@Override
	public void refreshToken(HttpServletRequest request, HttpServletResponse response)
			throws StreamWriteException, DatabindException, IOException {

		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String refreshToken;
		final String userName;

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return;
		}

		refreshToken = authHeader.substring(7);
		userName = this.jwtService.extractUsernameFromToken(refreshToken);
		if (userName != null) {

			UserDetails userDetails = this.userRepository.findByEmail(userName)
					.orElseThrow(() -> new ResourceNotFoundException("User", "Username", userName));

			if (this.jwtService.isValid(refreshToken, userDetails)) {

				String accessToken = this.jwtService.generateAccessToken(userDetails);
				Map<String, String> accessTokenResponse = new HashMap<>();
				accessTokenResponse.put("accessToken", accessToken);

				new ObjectMapper().writeValue(response.getOutputStream(), accessTokenResponse);
			}
		}
	}

	@Override
	public void resetPassword(AuthenticationRequest authRequest) {
		User user = this.userRepository.findByEmail(authRequest.getUsername())
				.orElseThrow(() -> new ResourceNotFoundException("User", "Username", authRequest.getUsername()));
		user.setPassword(this.passwordEncoder.encode(authRequest.getPassword()));
		this.userRepository.save(user);
	}

//	private void setTokenToHttp(String token, int maxAge) {
//		Cookie accessTokenCookie = new Cookie("accessToken", token);
//		accessTokenCookie.setHttpOnly(true);
//		accessTokenCookie.setSecure(true); // Set to true in production
//		accessTokenCookie.setPath("/");
//		accessTokenCookie.setMaxAge(maxAge);
//	}
}
