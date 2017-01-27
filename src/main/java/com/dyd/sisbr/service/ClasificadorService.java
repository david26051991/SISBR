package com.dyd.sisbr.service;

import java.io.File;
import java.util.List;

import com.dyd.sisbr.model.Clase;
import com.dyd.sisbr.model.Documento;
import com.dyd.sisbr.model.PalabraClave;

public interface ClasificadorService {

	public Documento clasificarDocumento(File file) throws Exception;
	
	public void construirClasificador(List<Clase> listaClases, List<Documento> listaDocumentos,  List<PalabraClave> listaUnicasPalabras);
	
}
