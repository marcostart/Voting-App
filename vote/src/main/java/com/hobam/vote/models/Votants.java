package com.hobam.vote.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Votants {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	private String nom;
	private String prenom;
	private String matricule;
	private String genre;
	private String naissance;
	private String phone;
	private String email;
	@Column(columnDefinition = "integer default 0")
	private Integer haveVote;
	
	public Integer getHaveVote() {
		return haveVote;
	}
	public void setHaveVote(Integer haveVote) {
		this.haveVote = haveVote;
	}
	//foreign key
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "electionid", referencedColumnName = "id", insertable=false, updatable=false)
	private ElectionListe election;
	private int electionid;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getMatricule() {
		return matricule;
	}
	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getNaissance() {
		return naissance;
	}
	public void setNaissance(String naissance) {
		this.naissance = naissance;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public ElectionListe getElection() {
		return election;
	}
	public void setElection(ElectionListe election) {
		this.election = election;
	}
	public int getElectionid() {
		return electionid;
	}
	public void setElectionid(int electionid) {
		this.electionid = electionid;
	}
	
	
	

}
