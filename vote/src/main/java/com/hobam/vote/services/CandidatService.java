package com.hobam.vote.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hobam.vote.models.Candidats;
import com.hobam.vote.repositories.CandidatsRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class CandidatService {
	
	@Autowired
	private CandidatsRepository candRep;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	public Optional<Candidats> find(Long id){
		return candRep.findById(id);
	}
	
	public Candidats updateVoice(Long id) {
		return candRep.findById(id).map(p -> {
			Integer voice = p.getVoix() == null ? 0 : p.getVoix();
			System.out.println("\n\n\nINt\n\n\n"+voice);
			p.setVoix(voice+1);
			return candRep.save(p);
		}).orElseThrow(()->new RuntimeException("Candidat not found"));
	}
	
	
	public List<Candidats> findByElection(Long id){
		return candRep.findAllByElectionid(id);
	}

}
