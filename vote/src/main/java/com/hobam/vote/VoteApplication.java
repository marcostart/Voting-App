package com.hobam.vote;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

import com.hobam.vote.models.ElectionListe;
import com.hobam.vote.services.ElectionListeService;

@SpringBootApplication
//@EnableJdbcHttpSession
@EnableJpaRepositories
@EntityScan
public class VoteApplication implements CommandLineRunner{
	
	@Autowired
	private ElectionListeService election;

	public static void main(String[] args) {
		SpringApplication.run(VoteApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
//		System.out.println("\n\n\n\nHello everyBody\n\n");
//		List<ElectionListe> elecListe = election.read();
//		elecListe.forEach(emplo -> {
//			System.out.println("Je m'appelle "+emplo.getElectionName()+ " et mon prénom est "+emplo.getImage());
//		});
		
	}

}
