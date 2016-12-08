package com.dyd.sisbr.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MenuController {

	@RequestMapping(value = { "/inicio"}, method = RequestMethod.GET)
	public String defaultPage(Model model) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User myUser = (User) auth.getPrincipal();
		model.addAttribute("username", myUser.getUsername().toString());
		return "inicio";
	}
	
}
