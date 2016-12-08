package com.dyd.sisbr.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dyd.sisbr.dao.PalabraClaveDAO;
import com.dyd.sisbr.model.PalabraClave;
import com.dyd.sisbr.service.PalabraClaveService;

@Service
public class PalabraClaveServiceImpl implements PalabraClaveService{

	@Autowired
	private PalabraClaveDAO palabraClaveDAO;

	@Override
	public void guardarPalabrasClave(List<PalabraClave> listaPalabrasClave) {
		if(listaPalabrasClave != null){
			for(PalabraClave palabraClave: listaPalabrasClave){
				palabraClaveDAO.insertPalabraClave(palabraClave);
			}
		}
	}

	@Override
	public List<PalabraClave> obtenerPalabrasClavesByClase(int idClase, int anioIni, int anioFin) {
		Map<String, Object> param = new HashMap<>();
		param.put("idClase", idClase);
		param.put("anioIni", anioIni);
		param.put("anioFin", anioFin);
		return palabraClaveDAO.selectPalabrasClavesByClase(param);
	}

	@Override
	public List<PalabraClave> obtenerPalabrasClavesByRaiz(List<String> listaRaiz) {
		return palabraClaveDAO.selectPalabrasClavesByRaiz(listaRaiz);
	}
	

}
