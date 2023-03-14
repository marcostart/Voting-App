package com.hobam.vote.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hobam.vote.models.AllUsers;
import com.hobam.vote.models.Candidats;
import com.hobam.vote.models.ElectionListe;
import com.hobam.vote.models.Role;
import com.hobam.vote.models.Votants;
import com.hobam.vote.models.Form.AdminForm;
import com.hobam.vote.models.Form.CodevForm;
import com.hobam.vote.models.Form.ElectionForm;
import com.hobam.vote.models.Form.UserForm;
import com.hobam.vote.payload.SignUpDto;
import com.hobam.vote.repositories.CandidatsRepository;
import com.hobam.vote.repositories.RoleRepository;
import com.hobam.vote.repositories.UserRepository;
import com.hobam.vote.repositories.VotantsRepository;
import com.hobam.vote.services.CandidatService;
import com.hobam.vote.services.CustomUserDetailsService;
import com.hobam.vote.services.ElectionListeService;
import com.hobam.vote.services.UserService;
import com.hobam.vote.services.VotantService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
    
	@Autowired
	private CustomUserDetailsService service;
	
	@Autowired
	private ElectionListeService election;
	
	@Autowired
	private CandidatsRepository candRep;
	
	@Autowired
	private VotantService voteServ;
	
	@Autowired
	private VotantsRepository voteRep;
	
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
	
    @GetMapping("/dashboard")
    public String userDashboard(HttpServletRequest httpServletRequest, HttpSession session, Model model) {
    	
    	
    	
//    	System.out.println("\n\n\nINt\n\n\n");
    	if (!this.check(httpServletRequest, session).stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
        	
    		model.addAttribute("erreur", "Veuillez vous reconnecter !");
        	return "error";
        }
    	List<ElectionListe> elecListe = election.read();
    	LocalDateTime myDateObj = LocalDateTime.now();
		model.addAttribute("date", myDateObj);
    	model.addAttribute("elections", elecListe);
    	model.addAttribute("codeVote", new CodevForm());
    	model.addAttribute("df", DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm"));
        return "userDashboard";
    }
   
    
    @PostMapping("/vote")
    public String putVote(@ModelAttribute("code") CodevForm code, Model model, HttpServletRequest httpServletRequest, HttpSession session) {
    	//System.out.println("\n\n\nINt\n\n\n"+code.getId());
    	Long id = Long.valueOf(code.getId());
    	if (!this.check(httpServletRequest, session).stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
    		
    		model.addAttribute("erreur", "Veuillez vous reconnecter !");
        	return "error";
        }
    	List<Votants> votants = voteRep.findAllByElectionid(id);
    	//System.out.println("\n\n\n"+code.getMatricule());
    	if (votants.stream().anyMatch(vot -> vot.getMatricule().equals(code.getMatricule())) ){
    		Iterator<Votants> it = votants.iterator();
    		Votants votant= votants.stream().filter(vot -> vot.getMatricule().equals(code.getMatricule())).findFirst().get();
    		if (votant.getHaveVote()==1) {
    			model.addAttribute("erreur", "Vous n'êtes pas autorisés à voter deux fois");
    			return "error";
    		}
    		httpServletRequest.getSession().setAttribute("VOTANT", votant.getId());
//    		for (int i = 0; i<votants.size(); i++) {
//    	  		System.out.println("\n\n\n"+ it.next().getMatricule());
//    	  	}
    	} else {
    		model.addAttribute("erreur", "Vous n'êtes pas autorisés à voter pour cette élection");
			return "error";
    	}
    	
    	model.addAttribute("election", election.find(id).get());
		if (election.find(id).isPresent() && election.find(id).get().getCode().equals(code.getCode())) {
			List<Candidats> candidats = candRep.findAllByElectionid(id);
			model.addAttribute("candidats", candidats);
			model.addAttribute("code", new CodevForm());
			model.addAttribute("election", election.find(id).get());
			return "userVote";
		}
		else {
			model.addAttribute("erreur", "Code d'élection incorrect");
			return "error";
		}
    	
    }
    
    @PostMapping("/save/vote")
    public String storeVote(@ModelAttribute("code") CodevForm code, Model model, HttpServletRequest httpServletRequest, HttpSession session) {
    	Long id = Long.valueOf(code.getId());
    	if (!this.check(httpServletRequest, session).stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
    		
    		model.addAttribute("erreur", "Veuillez vous reconnecter !");
        	return "error";
        }
    	Integer votant = (Integer) session.getAttribute("VOTANT");
    	Long votId = Long.valueOf(votant);
    	if (candidatServ.find(id).isPresent() && voteServ.find(votId).isPresent()) {
    		candidatServ.updateVoice(id);
    		voteServ.setVote(votId);
    	}
    	
    	return "redirect:/user/dashboard";
    	
    }
    
    @GetMapping("/resultats")
    public String vewResult( Model model, HttpServletRequest httpServletRequest, HttpSession session) {
    	
//    	if (!this.check(httpServletRequest, session).stream()
//                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
//    		
//            
//        	return "error";
//        }
//    	List<Candidats> first = candRep.findByElectionidOrderByVoixDesc(id);
//    	Iterator<Candidats> it = first.iterator();
//    	
//    	//System.out.println("\n\n\nINt\n\n\n"+ it.next().getDescription());
//    	model.addAttribute("first", first);
//    	model.addAttribute("election", election.find(id).get());
    	return "userResultats";
    }
  	
  	
  	
}
