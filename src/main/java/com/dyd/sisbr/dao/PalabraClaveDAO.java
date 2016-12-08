package com.dyd.sisbr.dao;

import java.util.List;
import java.util.Map;

import com.dyd.sisbr.model.PalabraClave;

public interface PalabraClaveDAO {

	public List<PalabraClave> selectPalabrasClavesByClase(Map<String, Object> param);
	
	public List<PalabraClave> selectPalabrasClavesByRaiz(List<String> listaRaiz);
	
	public void insertPalabraClave(PalabraClave palabraClave);
		
}
