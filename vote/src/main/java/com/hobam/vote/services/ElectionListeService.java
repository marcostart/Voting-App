package com.hobam.vote.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hobam.vote.models.ElectionListe;
import com.hobam.vote.repositories.ElectionListeRepository;

@Service
public class ElectionListeService {
	
	@Autowired
	private ElectionListeRepository electionRep;
	
	public List<ElectionListe> read(){
		return electionRep.findAll();
	}
	
	public ElectionListe save(ElectionListe el) {
		return electionRep.save(el);
	}
	
	public Optional<ElectionListe> find(final Long id) {
		return electionRep.findById(id);
	}
	
	public ElectionListe updateState(Long id, Integer el) {
		// TODO Auto-generated method stub
		return electionRep.findById(id).map(p -> {
			p.setState(el);
			return electionRep.save(p);
		}).orElseThrow(()->new RuntimeException("Election not found"));
	}
}
