package com.CoNickel.chatapp.services;

import com.CoNickel.chatapp.model.UserRepository;
import com.CoNickel.chatapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	public List<User> getUsers() {
		return userRepository.findAll();
	}


	public Optional<User> getUserByUsername(String username) {
		return userRepository.findUserByUsername(username);
	}
	public boolean addUser(User user) {
		userRepository.save(user);

		return true;
	}
}
