package com.hobam.vote.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hobam.vote.models.Form.AdminForm;
import com.hobam.vote.models.Form.UserForm;
import com.hobam.vote.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class VoteApplicationController {
	@Autowired
	private UserService userServ;
	
	//page index for all users
	@GetMapping("/")
	public String goAllUsersPage() {
		return "index";
	}
	
	@GetMapping("/vote/logout")
	private String logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
	    if (session != null) {
	        session.invalidate();
	    }
		return "redirect:/";
	}
	
	@GetMapping("/signin_signup")
  	public String getUserPage(Model model, HttpSession session) {
  		//List<AllUsers> userForm = userServ.readAll();
		//String sessionVar = (String) session.getAttribute("ERROR");
		//model.addAttribute("sessionVar", sessionVar!=null? sessionVar : new String());
  		model.addAttribute("userForm", new UserForm());
  		return "/signInUp";
  	}
	@GetMapping("/admin_page")
  	public String goAdminPage(Model model) {
  		//List<AllUsers> adminForm = userServ.readAll();
  		model.addAttribute("adminForm", new AdminForm());
  		return "/admin/loginAdmin";
  	}
}