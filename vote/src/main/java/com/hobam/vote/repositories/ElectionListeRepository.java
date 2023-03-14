package com.hobam.vote.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hobam.vote.models.ElectionListe;

@Repository
public interface ElectionListeRepository extends JpaRepository<ElectionListe, Long> {

}
