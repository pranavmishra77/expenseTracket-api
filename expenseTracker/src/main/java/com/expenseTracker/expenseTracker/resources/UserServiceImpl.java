package com.expenseTracker.expenseTracker.resources;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepository userRepository;
	
	@Override
	public User validateUser(String email, String password) throws etAuthException {
		if(email != null) email=email.toLowerCase();
		return userRepository.findByEmailAndPassword(email, password);
	}

	@Override
	public User registerUser(String FirstName, String Lastname, String email, String password) throws etAuthException {
		Pattern pattern = Pattern.compile("^(.+)@(.+)$");
		if (email != null) email= email.toLowerCase();
		if(!pattern.matcher(email).matches())
			throw new etAuthException("Invail enmail");
		Integer count = userRepository.getcountByEmail(email);
		if(count >0)
			throw new etAuthException("email already exist");
		Integer userId = userRepository.create(FirstName, Lastname, email, password);
		return userRepository.findByUserId(userId);
	}

}
