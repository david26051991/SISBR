package com.dyd.sisbr.dao;

import java.util.List;

import com.dyd.sisbr.model.Modulo;

public interface ModuloDAO {

	public List<Modulo> selectModulosPorUsuario(String usuario);
	
	public List<Modulo> selectModulosHijos(Integer idPadre);
	
}
