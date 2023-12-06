package com.taskproject.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskproject.entity.Users;
import com.taskproject.payload.UserDto;
import com.taskproject.repository.UserRepository;
import com.taskproject.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
//we perform type casting here by converting userDto to Users data.
	
	@Override
	public UserDto createUser(UserDto userDto) {
		Users user = userDtoToEntity(userDto); // converted userDto to Users
		Users savedUser = userRepository.save(user);
		return entityToUserDto(savedUser);
	}
	private Users userDtoToEntity(UserDto userDto) {
		Users users = new Users();
		users.setName(userDto.getName());
		users.setEmail(userDto.getEmail());
		users.setPassword (userDto.getPassword());
		return users;
	}
	private UserDto entityToUserDto(Users savedUser) {
		UserDto userDto = new UserDto();
		userDto.setId(savedUser.getId());
		userDto.setEmail(savedUser.getEmail());
		userDto.setPassword(savedUser.getPassword());
		userDto.setName(savedUser.getName());
		return userDto;
		}
	
	

}
