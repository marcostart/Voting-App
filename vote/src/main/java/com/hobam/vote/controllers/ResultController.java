package com.hobam.vote.controllers;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hobam.vote.models.Candidats;
import com.hobam.vote.repositories.CandidatsRepository;
import com.hobam.vote.services.CandidatService;
import com.hobam.vote.services.CustomUserDetailsService;
import com.hobam.vote.services.ElectionListeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ResultController {
	
	@Autowired
	private CustomUserDetailsService service;
	
	@Autowired
	private ElectionListeService election;
	
	@Autowired
	private CandidatsRepository candRep;
	
	@Autowired
	private CandidatService candidatServ;
	
	public Collection<? extends GrantedAuthority> check(HttpServletRequest httpServletRequest, HttpSession session) {
		String name = (String) session.getAttribute("USER");
		//System.out.println("\n\n\nIN\n\n\n"+name);
    	UserDetails userDetails = service.loadUserByUsername(name);
    	UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());   // HERE IS THE getAuthorities function
        usernamePasswordAuthenticationToken
                .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
        
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	
		return auth.getAuthorities();
		
	}
	
	@GetMapping("/resultat/{id}")
	public String show(@PathVariable("id") final Long id, Model model, HttpServletRequest httpServletRequest, HttpSession session) {
		
		if (!this.check(httpServletRequest, session).stream()
              .anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
  		
          
			return "error";
		}
	  	List<Candidats> first = candRep.findByElectionidOrderByVoixDesc(id);
	  	Iterator<Candidats> it = first.iterator();
	  	
	  	//System.out.println("\n\n\nINt\n\n\n"+ it.next().getDescription());
	  	model.addAttribute("first", first);
	  	int sum = 0;
	  	for (int i = 0; i<first.size(); i++) {
	  		sum+= it.next().getVoix();
	  	}
	  	model.addAttribute("total", sum);
	  	model.addAttribute("election", election.find(id).get());
  	
		return "userResultats";
	}
}
