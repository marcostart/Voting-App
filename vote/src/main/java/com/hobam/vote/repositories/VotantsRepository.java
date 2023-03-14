package com.hobam.vote.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hobam.vote.models.Votants;

@Repository
public interface VotantsRepository extends JpaRepository<Votants, Long>{
	List<Votants> findAllByElectionid(Long electionid);

}
