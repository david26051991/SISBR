package com.dyd.sisbr.service;

import java.util.List;

import com.dyd.sisbr.model.PalabraClave;

public interface PalabraClaveService {

	public void guardarPalabrasClave(List<PalabraClave> listaPalabrasClave);
	
	public List<PalabraClave> obtenerPalabrasClavesByClase(int idClase, int anioIni, int anioFin);
	
	public List<PalabraClave> obtenerPalabrasClavesByRaiz(List<String> listaRaiz);
	
}
