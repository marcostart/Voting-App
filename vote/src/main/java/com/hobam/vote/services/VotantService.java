package com.hobam.vote.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hobam.vote.models.Votants;
import com.hobam.vote.repositories.VotantsRepository;

@Service
public class VotantService {
	
	@Autowired
	private VotantsRepository voteRep;
	
	public Optional<Votants> find(Long id){
		return voteRep.findById(id);
	}
	
	public Votants setVote(Long id) {
		return voteRep.findById(id).map(p -> {
			p.setHaveVote(1);
			return voteRep.save(p);
		}).orElseThrow(()->new RuntimeException("Votant not found"));
	}

}
