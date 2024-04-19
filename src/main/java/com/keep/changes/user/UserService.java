package com.keep.changes.user;

import java.util.List;

public interface UserService {

//	Add User
	UserDto createUser(UserDto userDto);

//	Put Update User
	UserDto putUpdateUser(Long uId, UserDto userDto);

//	Patch Update User
	UserDto patchUpdateUser(Long uId, UserDto userDto);

//	Delete User
	void deleteUser(Long uId);

//	delete profile image
	boolean deleteProfileImage(long uId);

//	delete cover image
	boolean deleteCoverImage(long uId);

//	Get Users
	UserDto getUserById(Long uId);

	List<UserDto> getAllUsers();

	UserDto getUserByEmail(String email);

	UserDto getUserByPhone(String phone);

	List<UserDto> getUsersByName(String name);

}
