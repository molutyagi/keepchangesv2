package com.keep.changes.user;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keep.changes.user.exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

//	@Autowired
//	private PasswordEncoder passwordEncoder;

//	Create User
	@Override
	@Transactional
	public UserDto createUser(UserDto userDto) {

		userDto.setEmail(userDto.getEmail()
								.toLowerCase());

		User user = this.modelMapper.map(userDto, User.class);

//		user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));

		User savedUser = this.userRepository.save(user);

		return this.modelMapper.map(savedUser, UserDto.class);
	}

//	Put Update User
	@Override
	@Transactional
	public UserDto putUpdateUser(Long uId, UserDto ud) {
		User user = this.userRepository	.findById(uId)
										.orElseThrow(() -> new ResourceNotFoundException("User",
												"Id", uId));

		user.setUpdateUser(uId, ud.getName(), ud.getEmail(), ud.getPassword(), ud.getPhone(),
				ud.getDisplayImage(), ud.getCoverImage(), ud.getAbout());

		User updatedUser = this.userRepository.save(user);
		return this.modelMapper.map(updatedUser, UserDto.class);
	}

//	Patch Update User
	@Override
//	@Transactional
	public UserDto patchUpdateUser(Long uId, UserDto partialUserDto) {

		System.out.println("patch update user");
		User user = this.userRepository	.findById(uId)
										.orElseThrow(() -> new ResourceNotFoundException("User",
												"Id", uId));

		User partialUser = this.modelMapper.map(partialUserDto, User.class);

		System.out.println(partialUser);

		Field[] declaredFields = User.class.getDeclaredFields();
		for (Field field : declaredFields) {
			field.setAccessible(true);
			try {
				Object value = field.get(partialUser);
				System.out.println(field.getName() + " : field--------");
				if (value != null) {
					System.out.println("value---- " + value);

					field.set(user, value);
				}

			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException("error updating product", e);
			}
		}
		this.userRepository.save(user);
		return this.modelMapper.map(user, UserDto.class);
	}

//	Delete User
	@Override
	@Transactional
	public void deleteUser(Long uId) {
		User user = this.userRepository	.findById(uId)
										.orElseThrow(() -> new ResourceNotFoundException("User",
												"Id", uId));

		this.userRepository.delete(user);

	}

//	Get User
//	By Id
	@Override
	@Transactional
	public UserDto getUserById(Long uId) {
		User user = this.userRepository	.findById(uId)
										.orElseThrow(() -> new ResourceNotFoundException("User",
												"Id", uId));

		return this.modelMapper.map(user, UserDto.class);
	}

//	Get All users
	@Override
	@Transactional
	public List<UserDto> getAllUsers() {

		List<User> users = this.userRepository.findAll();

		List<UserDto> userDtos = new ArrayList<>();

		for (User user : users) {
			UserDto userDto = this.modelMapper.map(user, UserDto.class);
			userDtos.add(userDto);
		}
		return userDtos;
	}

//	Get User by Email
	@Override
	@Transactional
	public UserDto getUserByEmail(String email) {
		User user = this.userRepository	.findByEmail(email)
										.orElseThrow(() -> new ResourceNotFoundException("User",
												"Email", email));

		return this.modelMapper.map(user, UserDto.class);
	}

//	Get User By Phone
	@Override
	@Transactional
	public UserDto getUserByPhone(String phone) {
		User user = this.userRepository	.findByPhone(phone)
										.orElseThrow(() -> new ResourceNotFoundException("User",
												"Phone", phone));

		return this.modelMapper.map(user, UserDto.class);

	}

//	Get User By Name
	@Override
	@Transactional
	public List<UserDto> getUsersByName(String name) {
		List<User> users = this.userRepository	.findByNameContaining(name)
												.orElseThrow(() -> new ResourceNotFoundException(
														"User", "name", name));

		List<UserDto> userDtos = new ArrayList<>();
		for (User user : users) {
			UserDto userDto = this.modelMapper.map(user, UserDto.class);
			userDtos.add(userDto);
		}
		return userDtos;
	}

}
