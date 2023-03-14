package com.hobam.vote.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hobam.vote.config.UserNotFoundException;
import com.hobam.vote.models.AllUsers;
import com.hobam.vote.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService{
	@Autowired
	private UserRepository userRep;
	
	public List<AllUsers> readAll(){
		return userRep.findAll();
		
	}
	
	public void updateResetPasswordToken(String token, String email) throws UserNotFoundException{
		AllUsers user = userRep.findUserByEmail(email);
		if (user != null) {
			user.setResetPasswordToken(token);
			userRep.save(user);
		} else {
			throw new UserNotFoundException("Aucun utilsateur n'existe avec ce mail !");
		}
	}
	
	public AllUsers getByResetPasswordToken(String token) {
		return userRep.findUserByResetPasswordToken(token);
	}

}
