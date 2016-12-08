package com.dyd.sisbr.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String inicio(
			@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout,
			Model model){
		
		if (error != null) {
			model.addAttribute("error", "Usuario y/o contrase&ntilde;a es incorrecto");
		}

		if (logout != null) {
			model.addAttribute("msg", "You've been logged out successfully.");
		}		  
		return "login";
	}
	
	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String accesssDenied(Model model) {
	  //check if user is login
	  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	  if (!(auth instanceof AnonymousAuthenticationToken)) {
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		model.addAttribute("username", userDetail.getUsername());
	  }
	  return "403";

	}
	
}
