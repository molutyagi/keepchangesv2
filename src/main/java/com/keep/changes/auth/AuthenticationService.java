package com.keep.changes.auth;

import javax.naming.AuthenticationException;

import com.keep.changes.user.UserDto;

public interface AuthenticationService {

	AuthenticationResponse register(UserDto requestUser);

	AuthenticationResponse login(AuthenticationRequest userRequest) throws AuthenticationException;

}
