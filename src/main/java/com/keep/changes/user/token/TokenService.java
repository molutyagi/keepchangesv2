package com.keep.changes.user.token;

public interface TokenService {

	String generateAndSaveActivationToken(String email);

	Boolean verifyToken(String otp, String email);

	void deleteToken(Long tId);

}
