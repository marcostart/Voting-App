package com.hobam.vote.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hobam.vote.models.AllUsers;

@Repository
public interface UserRepository extends JpaRepository<AllUsers, Integer>{
	Optional<AllUsers> findUserByUsername(String username);
	
	AllUsers findUserByEmail(String email);
	AllUsers findUserByResetPasswordToken(String token);
	
	Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);
}
