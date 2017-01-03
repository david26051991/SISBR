package com.dyd.sisbr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dyd.sisbr.model.Modulo;
import com.dyd.sisbr.service.MenuService;
import com.dyd.sisbr.util.Utils;

@Controller
public class MenuController {

	@Autowired
	private MenuService menuService;
	
	@RequestMapping(value = { "/inicio"}, method = RequestMethod.GET)
	public String defaultPage(Model model) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User myUser = (User) auth.getPrincipal();
		List<Modulo> listaOpciones = menuService.cargarListaOpciones(myUser.getUsername());
		
		model.addAttribute("username", myUser.getUsername());
		model.addAttribute("listaOpciones", Utils.getJson(listaOpciones));
		return "inicio";
	}
	
}
