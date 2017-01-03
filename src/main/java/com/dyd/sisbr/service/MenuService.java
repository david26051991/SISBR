package com.dyd.sisbr.service;

import java.util.List;

import com.dyd.sisbr.model.Modulo;

public interface MenuService {

	public List<Modulo> cargarListaOpciones(String usuario);
}
