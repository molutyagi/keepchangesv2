package com.keep.changes.user;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keep.changes.file.FileService;
import com.keep.changes.payload.response.ApiResponse;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private FileService fileService;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${user-profile.images}")
	private String profileImagePath;

	@Value("${user-cover.images}")
	private String coverImagePath;

//	POST Mapping / Add user
	@PostMapping("add")
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {

		UserDto createdUser = this.userService.createUser(userDto);
		return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
	}

//	Update User
//	PUT Update user
	@PutMapping("user/update_{uId}")
	public ResponseEntity<?> putUpdateUser(@Valid @PathVariable Long uId, @RequestBody UserDto userDto) {

		if (this.authenticateUser(uId) == false) {
			return ResponseEntity.ok(new ApiResponse("Unauthorized request!!", false));
		}

		UserDto updatedUser = this.userService.putUpdateUser(uId, userDto);
		return ResponseEntity.ok(updatedUser);
	}

//	Patch update user
	@PatchMapping("user/update_{uId}")
	public ResponseEntity<?> patchUpdateUser(@Valid @PathVariable Long uId, @RequestBody UserDto partialUser) {

		if (this.authenticateUser(uId) == false) {
			return ResponseEntity.ok(new ApiResponse("Unauthorized request!!", false));
		}

		UserDto patchedUser = this.userService.patchUpdateUser(uId, partialUser);
		return ResponseEntity.ok(patchedUser);
	}

//	Delete User
	@DeleteMapping("user/delete_{uId}")
	public ResponseEntity<?> deleteUser(@PathVariable Long uId) {

		if (this.authenticateUser(uId) == false) {
			return ResponseEntity.ok(new ApiResponse("Unauthorized request!!", false));
		}

		this.userService.deleteUser(uId);
		return ResponseEntity.ok(new ApiResponse("User Deleted Successfully.", true));
	}

//	GET Mapping / Get Users
//	Get all
	@GetMapping(value = { "getall", "", "/", "getall/" })
	public ResponseEntity<List<UserDto>> getAllUsers() {

		return ResponseEntity.ok(this.userService.getAllUsers());

	}

//	Get by Id
	@GetMapping(value = { "user_{uId}", "user_{uId}/" })
	public ResponseEntity<UserDto> getUserById(@PathVariable Long uId) {

		return ResponseEntity.ok(this.userService.getUserById(uId));
	}

//	Get by Email
	@GetMapping(value = { "user/{email}", "user/{email}/" })
	public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {

		return ResponseEntity.ok(this.userService.getUserByEmail(email));
	}

//	Get By Phone
	@GetMapping(value = { "user/phone/{phone}", "user/phone/{phone}/" })
	public ResponseEntity<UserDto> getUserByPhone(@PathVariable String phone) {

		return ResponseEntity.ok(this.userService.getUserByPhone(phone));
	}

//	Get By Name
	@GetMapping(value = { "user/name/{name}", "user/name/{name}/" })
	public ResponseEntity<List<UserDto>> getUserByName(@PathVariable String name) {

		return ResponseEntity.ok(this.userService.getUsersByName(name));
	}

//	Upload User Profile Image
	@PostMapping(value = { "user_{uId}/profile-image", "user_{uId}/profile-image/" })
	public ResponseEntity<?> uploadProfileImage(@Valid @PathVariable Long uId,
			@RequestParam("image") MultipartFile image) throws IOException {

		if (this.authenticateUser(uId) == false) {
			return ResponseEntity.ok(new ApiResponse("Unauthorized user!!", false));
		}

		UserDto userDto = this.userService.getUserById(uId);
		System.out.println("1.----------");
		if (userDto.getDisplayImage() != null && !userDto.getDisplayImage().equals("")
				&& !userDto.getDisplayImage().equals("default.png")) {
			System.out.println("shouldnt be here");
			boolean isDeleted;
			try {
				isDeleted = this.fileService.deleleFile(profileImagePath, userDto.getDisplayImage());
			} catch (IOException e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("1 OOPS!! Something went wrong. Could not update profile image.");
			}

			if (isDeleted == false) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("2 OOPS!! Something went wrong. Could not update profile image.");
			}
		}

		String fileName = null;
		try {
			System.out.println("here");
			fileName = this.fileService.uploadImage(profileImagePath, image);
			System.out.println("completed");
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("3 OOPS!! Something went wrong. Could not update profile image.");
		}
		userDto.setDisplayImage(fileName);

		return ResponseEntity.ok(this.userService.putUpdateUser(uId, userDto));
	}

//	Delete Profile Image
	@DeleteMapping(value = { "user_{uId}/profile-image", "user_{uId}/profile-image/" })
	public ResponseEntity<ApiResponse> deleteUserProfileImage(@PathVariable Long uId) throws IOException {

		if (this.authenticateUser(uId) == false) {
			return ResponseEntity.ok(new ApiResponse("Unauthorized request!!", false));
		}

		UserDto userDto = this.userService.getUserById(uId);
		if (userDto.getDisplayImage() != null && !userDto.getDisplayImage().equals("")
				&& !userDto.getDisplayImage().equals("default.png")) {
			boolean isDeleted;
			try {
				isDeleted = this.fileService.deleleFile(profileImagePath, userDto.getDisplayImage());
			} catch (IOException e) {
				return ResponseEntity.badRequest()
						.body(new ApiResponse("OOPS!! Something went wrong. Could not update profile image.", false));
			}

			if (isDeleted == false) {
				return ResponseEntity.badRequest()
						.body(new ApiResponse("OOPS!! Something went wrong. Could not update profile image.", false));
			}
		}
		userDto.setDisplayImage("default.png");
		this.userService.putUpdateUser(uId, userDto);
		return ResponseEntity.ok(new ApiResponse("Profile Image Deleted Successfully.", true));
	}

//	Get user profile image
	@GetMapping(value = { "user_{uId}/profile-image/{imageName}", "user_{uId}/profile-image/{imageName}/" })

	public void downloadProfileImage(@Valid @PathVariable Long uId, @PathVariable String imageName,
			HttpServletResponse res) throws IOException {
		InputStream is = this.fileService.getResource(profileImagePath, imageName);
		res.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(is, res.getOutputStream());
	}

//	Upload User Cover Image
	@PostMapping(value = { "user_{uId}/cover-image", "user_{uId}/cover-image/" })
	public ResponseEntity<?> uploadCoverImage(@Valid @PathVariable Long uId, @RequestParam("image") MultipartFile image)
			throws IOException {

		if (this.authenticateUser(uId) == false) {
			return ResponseEntity.ok(new ApiResponse("Unauthorized request!!", false));
		}

		UserDto userDto = this.userService.getUserById(uId);

		if (userDto.getCoverImage() != null && !userDto.getCoverImage().equals("")
				&& !userDto.getCoverImage().equals("default.png")) {
			boolean isDeleted = this.fileService.deleleFile(coverImagePath, userDto.getCoverImage());

			if (isDeleted == false) {
				return ResponseEntity.badRequest()
						.body(new ApiResponse("OOPS!! Something went wrong. Could not update cover image.", false));
			}
		}

		String fileName = this.fileService.uploadImage(coverImagePath, image);
		userDto.setCoverImage(fileName);

		return ResponseEntity.ok(this.userService.putUpdateUser(uId, userDto));
	}

//	Delete Cover Image
	@DeleteMapping(value = { "user_{uId}/cover-image", "user_{uId}/cover-image/" })
	public ResponseEntity<ApiResponse> deleteUserCoverImage(@PathVariable Long uId) throws IOException {

		if (this.authenticateUser(uId) == false) {
			return ResponseEntity.ok(new ApiResponse("Unauthorized request!!", false));
		}

		UserDto userDto = this.userService.getUserById(uId);
		if (userDto.getCoverImage() != null && !userDto.getCoverImage().equals("")
				&& !userDto.getCoverImage().equals("default.png")) {
			boolean isDeleted;
			try {
				isDeleted = this.fileService.deleleFile(coverImagePath, userDto.getCoverImage());
			} catch (IOException e) {
				return ResponseEntity.badRequest()
						.body(new ApiResponse("OOPS!! Something went wrong. Could not update cover image.", false));
			}

			if (isDeleted == false) {
				return ResponseEntity.badRequest()
						.body(new ApiResponse("OOPS!! Something went wrong. Could not update cover image.", false));
			}
		}
		userDto.setCoverImage("default.png");
		this.userService.putUpdateUser(uId, userDto);
		return ResponseEntity.ok(new ApiResponse("Cover Image Deleted Successfully.", true));
	}

//	Get user cover image
	@GetMapping(value = { "user_{uId}/cover-image/{imageName}", "user_{uId}/cover-image/{imageName}/" })
	public void downloadcoverImage(@PathVariable Long uId, @PathVariable String imageName, HttpServletResponse res)
			throws IOException {
		InputStream is = this.fileService.getResource(coverImagePath, imageName);
		res.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(is, res.getOutputStream());
	}

//	Post File And User Details in a single Request
	@PatchMapping(value = { "user/update-profile_{uId}", "user/update-profile_{uId}/" })
	public ResponseEntity<?> updateUserAndImage(@Valid @PathVariable Long uId,
			@RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
			@RequestParam(value = "coverImage", required = false) MultipartFile coverImage,
			@RequestParam(value = "userData", required = false) String userData) {

		if (this.authenticateUser(uId) == false) {
			return ResponseEntity.ok(new ApiResponse("Unauthorized request!!", false));
		}

		UserDto userDto = new UserDto();

		if (userData != null) {
			try {
				userDto = objectMapper.readValue(userData, UserDto.class);
			} catch (JsonProcessingException e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Request!");
			}
		}

		if (profileImage != null) {
			try {
				String profileImageName = this.fileService.uploadImage(this.profileImagePath, profileImage);
				userDto.setDisplayImage(profileImageName);
			} catch (IOException e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not update user details. Try again.");
			}
		}

		if (coverImage != null) {
			try {
				String coverImageName = this.fileService.uploadImage(this.coverImagePath, coverImage);
				userDto.setCoverImage(coverImageName);
			} catch (IOException e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not update user details. Try again.");
			}
		}

		return ResponseEntity.ok(this.userService.patchUpdateUser(uId, userDto));
	}

//	check if correct user is asking to change resources
	private boolean authenticateUser(long uId) {
//		String loggedInUser = SecurityContextHolder	.getContext()
//													.getAuthentication()
//													.getName();
//
//		UserDto user = this.userService.getUserById(uId);
//
//		if (!user	.getEmail()
//					.equals(loggedInUser)) {
//			return false;
//		}
		return true;
	}
}
