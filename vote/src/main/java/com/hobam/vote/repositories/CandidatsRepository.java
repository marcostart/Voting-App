package com.hobam.vote.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hobam.vote.models.Candidats;

@Repository
public interface CandidatsRepository extends JpaRepository<Candidats, Long>{
	List<Candidats> findAllByElectionid(Long electionid);
	
	List<Candidats> findByElectionidOrderByVoixDesc(Long electionid);
}
