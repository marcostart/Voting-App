package com.hobam.vote.controllers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.hobam.vote.models.AllUsers;
import com.hobam.vote.models.Role;
import com.hobam.vote.models.Form.UserForm;
import com.hobam.vote.repositories.RoleRepository;
import com.hobam.vote.repositories.UserRepository;
import com.hobam.vote.services.CustomUserDetailsService;
import com.hobam.vote.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class LoginController {
	
	@Autowired
	private UserService userServ;
	
	@Autowired
	private UserRepository userRep;
	
	@Autowired
	private RoleRepository roleRep;
	
	
	 @Autowired
	 private PasswordEncoder passwordEncoder;
	
	
	// Voter's registration
	@GetMapping("/register")
	public String register() {
		return "registeracc";
	}
	
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
	private CustomUserDetailsService service;
	
	//checking connection
  	@PostMapping("/admin_page")
  	public String checkAdminConnection(@ModelAttribute AllUsers user, Model model, HttpServletRequest request) {
  		System.out.println("\n\n\nINt\n\n\n");
  		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getPassword()));
  		
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
//  		AllUsers userd = userRep.findUserByUsername(user.getUsername()).orElseThrow(()->new RuntimeException("User not found"));
//  		String pass = user.getPassword();
//  		System.out.println(userd.getPassword() + " and "+ pass);
//  		System.out.println(userd.getPassword().equals(pass));
//  		if (userd.getPassword().equals(pass))
//  			return "/admin/template_admin/index";
//  		else
//  			return "/admin/loginAdmin";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        request.getSession().setAttribute("USER", user.getUsername());
        if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
        	return "redirect:/admin/dashboard";
        }
        return "error";
        
  		
  	}
  	
  	@PostMapping("/vote/signup")
  	public String registerUser(@ModelAttribute UserForm user, Model model){
  		 
        // add check for username exists in a DB
        if(userRep.existsByUsername(user.getUsername())){
            return "/signInUp";
        }
       
        // add check for email exists in DB
        if(userRep.existsByEmail(user.getEmail())){
            return "/signInUp";
        }
        //System.out.println("\n\n\n\n");
        // create user object
        AllUsers usernew = new AllUsers();
        usernew.setUsername(user.getUsername());
        usernew.setEmail(user.getEmail());
        usernew.setPassword(passwordEncoder.encode(user.getPassword()));
        usernew.setAccountNonLocked(true);

        Role roles = roleRep.findByName("ROLE_USER").get();
        usernew.setRoles(Collections.singleton(roles));

        userRep.save(usernew);
        
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>(); // use list if you wish
        for (Role role : usernew.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        
        return "redirect:/signin_signup"; 
//        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);

    }
  	
  	@PostMapping("/vote/signin")
  	public String checkUserConnection(HttpServletRequest httpServletRequest, @ModelAttribute AllUsers user, Model model, HttpServletRequest request) {
  		//System.out.println("\n\n\nIN\n\n\n"+service);
  		
  		UserDetails userDetails = service.loadUserByUsername(user.getUsername());
  		
  		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());   // HERE IS THE getAuthorities function
        usernamePasswordAuthenticationToken
                .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
  		
//  		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                user.getUsername(), user.getPassword()));
  		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        //SecurityContextHolder.getContext().setAuthentication(authentication);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        request.getSession().setAttribute("USER", user.getUsername());
        if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
        	
            
        	return "redirect:/user/dashboard";
        }
        	
        else if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
        	return "redirect:/admin_page";
        }
        return "error";
        
  		
  	}

}