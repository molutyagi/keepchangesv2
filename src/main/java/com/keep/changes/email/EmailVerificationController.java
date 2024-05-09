package com.keep.changes.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.keep.changes.user.UserDto;
import com.keep.changes.user.UserService;
import com.keep.changes.user.token.TokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/auth/verification")
public class EmailVerificationController {

	@Autowired
	private EmailService emailService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private UserService userService;

	@PostMapping("send-otp")
	public ResponseEntity<?> sendOtp(@Valid @RequestBody OtpDto otpDto
//			, @RequestParam("action") String action
	) {

//		if (action.equals("register-user")) {
//		if (this.userService.getUserByEmail(otpDto.getEmail()) != null) {
//			throw new ResourceAlreadyExistsException("User", "Email", otpDto.getEmail());
//		}
//
//		if (this.userService.getUserByPhone(otpDto.getPhone()) != null) {
//			throw new ResourceAlreadyExistsException("User", "phone", otpDto.getPhone());
//		}

		this.emailService.sendEmail(otpDto.getEmail(), otpDto.getName(), EmailTemplateName.CONFIRM_EMAIL,
				"email-verification");
//		} else if (action.equals("forgot-password")) {
//			UserDto user = this.userService.getUserByEmail(email);
//			this.emailService.sendEmail(email, user.getName(), EmailTemplateName.RESET_PASSWORD, "reset-password");
//
//		}

		return ResponseEntity.ok("OTP sent to your mail successfully");
	}

	@PostMapping("verify-otp")
	public ResponseEntity<?> verifyOtp(@Valid @RequestParam("email") String email, @RequestParam("otp") String otp) {

		if (this.tokenService.verifyToken(otp, email)) {
			return ResponseEntity.ok("Email verified successfully");
		}
		return ResponseEntity.badRequest().body("OTP expired. Get a new OTP and retry again.");
	}

	@PostMapping("reset-password")
	public ResponseEntity<?> resetPassword(@Valid @RequestParam("email") String email,
			@RequestParam("password") String password) {

		UserDto user = this.userService.getUserByEmail(email);
		user.setPassword(password);
		this.userService.patchUpdateUser(user.getId(), user);

		return ResponseEntity.ok("Password updated successfully");
	}
}