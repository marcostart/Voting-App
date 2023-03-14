package com.hobam.vote.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hobam.vote.models.Form.UserForm;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MyErrorController implements ErrorController {

	@RequestMapping("/error")
	public String getErrorPage(HttpServletRequest request, Model model) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		
		
		if (status != null) {
			Integer statusCode = Integer.valueOf(status.toString());
			model.addAttribute("code", statusCode);
			if (statusCode == 403) {
				request.getSession().setAttribute("ERROR", "LOGIN");
				return "redirect:/signin_signup";
			}

		}
		return "error";
	}
}
