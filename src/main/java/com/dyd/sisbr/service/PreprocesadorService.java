package com.dyd.sisbr.service;

import java.io.File;
import java.util.List;

import com.dyd.sisbr.model.PalabraClave;


public interface PreprocesadorService {
	
	public List<PalabraClave> preprocesamiento(File file);
	
	public List<PalabraClave> preprocesarConsulta(String consulta);
	
//	public void cargarListaStopWords(File archivo1, File archivo2);
	
	public void limpiarListaUnica();
	
	public List<PalabraClave> getListaUnicaToken();
	
	public String borrarStopWords(String texto);
	
	public String borrarStopWords_compuestos(String texto);
	
	public void lematizarTokens(List<PalabraClave> lista);
}
