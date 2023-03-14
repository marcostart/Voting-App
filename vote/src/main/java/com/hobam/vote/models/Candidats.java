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
public class Candidats {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	private String nom;
	private String prenom;
	private String genre;
	private String naissance;
	private String phone;
	private String email;
	private String description;
	@Column(columnDefinition = "integer default 0")
	private Integer voix;
	
	public Integer getVoix() {
		return voix;
	}
	public void setVoix(Integer voix) {
		this.voix = voix;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
