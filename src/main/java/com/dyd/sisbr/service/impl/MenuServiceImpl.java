package com.dyd.sisbr.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dyd.sisbr.dao.ModuloDAO;
import com.dyd.sisbr.model.Modulo;
import com.dyd.sisbr.service.MenuService;

@Service
public class MenuServiceImpl implements MenuService{

	@Autowired
	private ModuloDAO moduloDAO;
	
	@Override
	public List<Modulo> cargarListaOpciones(String usuario) {
		List<Modulo> listaModulo = moduloDAO.selectModulosPorUsuario(usuario);
		listaModulo = cargarOpcionesHijos(listaModulo);
		return listaModulo;
	}
	
	private List<Modulo> cargarOpcionesHijos(List<Modulo> listaModulo){
		
		for(Modulo modulo : listaModulo){
			List<Modulo> listaHijos = moduloDAO.selectModulosHijos(modulo.getIdModulo());
			if(listaHijos != null && !listaHijos.isEmpty()){
				modulo.setListaHijos(cargarOpcionesHijos(listaHijos));
			}
		}
		return listaModulo;
	}

}
