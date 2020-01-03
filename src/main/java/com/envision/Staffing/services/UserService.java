package com.envision.Staffing.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.envision.Staffing.model.User;
import com.envision.Staffing.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public List<User> getAllUsers() {
		List<User> userList = (List<User>) userRepository.findAll();

		if (userList.size() > 0) {
			return userList;
		} else {
			return new ArrayList<User>();
		}
	}

	public User getUserById(Integer id) {
		Optional<User> user = userRepository.findById(id);

		if (user.isPresent()) {
			return user.get();
		} else {
			return null;
			// throw new RecordNotFoundException("No user record exist for given id");
		}
	}

	public User createOrUpdateUser(User entity) {
		Optional<User> user = userRepository.findById(entity.getId());

		if (user.isPresent()) {
			User newEntity = user.get();
			newEntity.setEmail(entity.getEmail());
			newEntity.setName(entity.getName());
			newEntity = userRepository.save(newEntity);
			return newEntity;
		} else {
			entity = userRepository.save(entity);
			return entity;
		}
	}

	public void deleteUserById(Integer id) {
		Optional<User> user = userRepository.findById(id);

		if (user.isPresent()) {
			userRepository.deleteById(id);
		} else {
			// throw new RecordNotFoundException("No user record exist for given id");
		}
	}
}
